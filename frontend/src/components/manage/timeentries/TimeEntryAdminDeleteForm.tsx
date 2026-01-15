/*
 * assemble
 * TimeEntryAdminDeleteForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useQueryClient } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import {
    getGetAllTimeEntriesByEmployeeIdQueryKey,
    getGetAllTimeEntriesByProjectIdQueryKey, getGetAllTimeEntriesQueryKey,
    getGetOwnTimeEntriesQueryKey,
    useDeleteTimeEntryById
} from "@/api/rest/generated/query/timeentries/timeentries";
import { toast } from "sonner";
import { invalidateQueries } from "@/utils/query";
import { getGetProjectByIdQueryKey } from "@/api/rest/generated/query/projects/projects";
import { getGetEmployeeQueryKey } from "@/api/rest/generated/query/employees/employees";
import { FormActionContext } from "@/store/formActionStore";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";

type TimeEntryAdminDeleteFormProps = {
    timeentry: TimeEntryDTO;
}

export default function TimeEntryAdminDeleteForm( { timeentry }: TimeEntryAdminDeleteFormProps ) {
    const queryClient = useQueryClient();
    const router = useRouter();
    const formId = "time-entry-delete-form";
    const form = useForm();
    const { mutate, isPending, isSuccess, isError } = useDeleteTimeEntryById();

    const handleDeleteTimeEntry = () => {
        mutate(
            {
                id: timeentry.id
            },
            {
                onSuccess: async () => {
                    toast.success( "Success", {
                        description: "Timeentry was deleted",
                    } )
                    await invalidateQueries(
                        queryClient,
                        [
                            getGetOwnTimeEntriesQueryKey() as unknown as readonly string[],
                            getGetOwnTimeEntriesQueryKey( { aroundDate: timeentry.date } ) as unknown as readonly string[],
                            getGetProjectByIdQueryKey( timeentry.project.id ),
                            getGetEmployeeQueryKey( timeentry.employee.id ),
                            getGetAllTimeEntriesByProjectIdQueryKey( timeentry.project.id ),
                            getGetAllTimeEntriesByEmployeeIdQueryKey( timeentry.employee.id ),
                            getGetAllTimeEntriesQueryKey()
                        ]
                    );
                    handleCancel();
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: "Timeentry could not be deleted." } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            }
        )
    }

    const handleCancel = () => {
        router.back();
    }

    return <FormActionContext.Provider
        value={ { isSuccess, isPending, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleDeleteTimeEntry ) }
                  className="space-y-8">
                <FieldGroup className={ "p-8 my-0" }>
                    <p className={ "text-xl font-semibold text-center" }>
                        Are you sure you want to delete timeentry <strong>{ timeentry.no }</strong>?
                    </p>
                    <ErrorMessage/>
                </FieldGroup>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ "Delete" } variant={ "destructive" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
