/*
 * assemble
 * handlers.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { UseFormReturn } from "react-hook-form";
import { TimeEntryFormInput, TimeEntryFormOutput } from "@/types/timeentries/timeentry.types";
import {
    ErrorResponse, FieldValidationError, TimeEntryCreateDTO,
    TimeEntryDTO, TimeEntryUpdateDTO,
    ValidationErrorResponse
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { QueryClient, UseMutationResult } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { ModalContextType } from "@/components/custom-ui/Modal";
import {
    getGetAllTimeEntriesByEmployeeIdQueryKey,
    getGetAllTimeEntriesByProjectIdQueryKey, getGetAllTimeEntriesQueryKey,
    getGetOwnTimeEntriesQueryKey, getGetOwnTimeEntryByIdQueryKey, getGetTimeEntryByIdQueryKey,
    useCreateOwnTimeEntry,
    useCreateTimeEntry,
    useUpdateOwnTimeEntry, useUpdateTimeEntry
} from "@/api/rest/generated/query/timeentries/timeentries";
import { ErrorType } from "@/services/rest/axios-instance";
import { invalidateQueries } from "@/utils/query";
import { getGetProjectByIdQueryKey } from "@/api/rest/generated/query/projects/projects";
import { getGetEmployeeQueryKey } from "@/api/rest/generated/query/employees/employees";
import { toast } from "sonner";

interface UseTimeEntryFormHandlersProps {
    isNew: boolean;
    form: UseFormReturn<TimeEntryFormInput, unknown, TimeEntryFormOutput>;
    timeentry?: TimeEntryDTO;
    queryClient: QueryClient;
    router: ReturnType<typeof useRouter>;
    modalContext: ModalContextType | null;
    isOwnTimeEntry?: boolean;
    hasPriviledge?: boolean;
}

type CreateMutation = UseMutationResult<TimeEntryDTO, ErrorType<ValidationErrorResponse | ErrorResponse>, {
    data: TimeEntryCreateDTO
}, unknown>

type UpdateMutation = UseMutationResult<TimeEntryDTO, ErrorType<ValidationErrorResponse | ErrorResponse>, {
    id: string;
    data: TimeEntryUpdateDTO;
}, unknown>

interface MutationHandlers {
    create: CreateMutation,
    update: UpdateMutation,
}

function useTimeEntryAdminFormHandlers(
    {
        isNew,
        form,
        timeentry,
        queryClient,
        router,
        modalContext,
        isOwnTimeEntry = false,
        hasPriviledge = true
    }: UseTimeEntryFormHandlersProps
) {
    const create = useCreateTimeEntry(),
        update = useUpdateTimeEntry();


    return useTimeEntryFormHandlers( {
        isNew,
        form,
        timeentry,
        queryClient,
        router,
        modalContext,
        isOwnTimeEntry,
        hasPriviledge,
        create,
        update
    } );
}

function useTimeEntryUserFormHandlers(
    {
        isNew,
        form,
        timeentry,
        queryClient,
        router,
        modalContext,
        hasPriviledge = false
    }: UseTimeEntryFormHandlersProps
) {
    const create = useCreateOwnTimeEntry(),
        update = useUpdateOwnTimeEntry();

    return useTimeEntryFormHandlers( {
        isNew,
        form,
        timeentry,
        queryClient,
        router,
        modalContext,
        hasPriviledge,
        create,
        update
    } );
}

function useTimeEntryFormHandlers(
    {
        isNew,
        form,
        timeentry,
        queryClient,
        router,
        modalContext,
        isOwnTimeEntry = false,
        hasPriviledge,
        create, update
    }: UseTimeEntryFormHandlersProps & MutationHandlers
) {


    const onSuccess = async ( data: TimeEntryDTO ) => {
        await invalidateQueries( queryClient, [
            getGetOwnTimeEntriesQueryKey(),
            getGetOwnTimeEntryByIdQueryKey( data.id ),
            getGetTimeEntryByIdQueryKey( data.id ),
            getGetProjectByIdQueryKey( data.project.id ),
            getGetEmployeeQueryKey( data.employee.id ),
            getGetAllTimeEntriesByProjectIdQueryKey( data.project.id ),
            getGetAllTimeEntriesByEmployeeIdQueryKey( data.employee.id ),
            getGetAllTimeEntriesQueryKey()
        ] );

        form.clearErrors();
        toast.success( "Success", {
            description: `${ isNew ? "Timeentry was created" : "Timeentry was updated" }`,
            action: hasPriviledge ? {
                label: "View",
                onClick: () => router.push( `/app/manage/timeentries/${ data.id }` )
            } : null
        } );

        if ( modalContext ) {
            handleCancel();
        }
    };

    const onError = ( error: ErrorType<ValidationErrorResponse | ErrorResponse> ) => {
        if ( error.response?.data ) {
            const data = error.response.data;
            if ( error.status === 400 && "errors" in data && data.errors ) {
                data.errors.forEach( ( error: FieldValidationError ) => {
                    form.setError( error.fieldName as keyof TimeEntryFormInput, {
                        type: "manual",
                        message: error.errorMessage
                    } );
                } );
            } else if ( [ 404, 403 ].includes( error?.status ?? 0 ) && "message" in data && data.message ) {
                form.setError( "root", { type: "manual", message: data.message } );
            }
        } else {
            form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
        }
    };

    const handleSubmit = async ( data: TimeEntryFormOutput ) => {
        if ( isNew ) {
            create.mutate( { data }, { onSuccess, onError } );
        } else {
            update.mutate( { id: timeentry!.id, data }, { onSuccess, onError } );
        }
    };

    const handleCancel = () => {
        if ( modalContext ) {
            modalContext.setOpen( false );
        }
        router.back();
    };

    return {
        handleSubmit,
        handleCancel,
        create,
        update,
        ctxValue: isNew ? {
            isError: create.isError,
            isPending: create.isPending,
            isSuccess: create.isSuccess,
            disableOnSuccess: true
        } : {
            isError: update.isError,
            isPending: update.isPending,
            isSuccess: update.isSuccess,
            disableOnSuccess: !isOwnTimeEntry
        }
    };
}

export {
    useTimeEntryAdminFormHandlers,
    useTimeEntryUserFormHandlers
}