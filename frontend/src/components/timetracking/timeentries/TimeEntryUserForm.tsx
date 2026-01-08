/*
 * assemble
 * TimeEntryUserForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { FormActionContext } from "@/store/formActionStore";
import { FormProvider, useForm } from "react-hook-form";
import { useRouter } from "@bprogress/next/app";
import { useQueryClient } from "@tanstack/react-query";
import useModalContext from "@/hooks/useModalContext";
import {
    TimeEntryDTO,
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ScrollArea } from "@/components/ui/scroll-area";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { FieldGroup } from "@/components/ui/field";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    TimeEntrySchema
} from "@/types/timeentries/timeentry.types";
import { useSearchParams } from "next/navigation";
import useUserContext from "@/hooks/useUserContext";
import { TimeEntryFragment } from "@/components/manage/timeentries/form/fragments";
import { getTimeEntryDefaultValues } from "@/components/manage/timeentries/form/values";
import { useTimeEntryCalculations } from "@/components/manage/timeentries/form/utils";
import { useTimeEntryUserFormHandlers } from "@/components/manage/timeentries/form/handlers";

type TimeEntryMultiFormProps = {
    employeeId?: string;
    timeentry?: TimeEntryDTO;
}

export default function TimeEntryUserForm( { employeeId, timeentry }: TimeEntryMultiFormProps ) {
    const { isManager, isAdmin, isSuperUser } = useUserContext();
    const hasPriviledge = isManager || isAdmin || isSuperUser;
    const isNew = timeentry === undefined && employeeId != null;

    const router = useRouter();
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const searchParams = useSearchParams()
    const dateParam = searchParams.get( "date" ) ?? "";

    const form = useForm( {
        resolver: zodResolver( TimeEntrySchema ),
        defaultValues: getTimeEntryDefaultValues( timeentry, employeeId, dateParam )
    } );

    const { total } = useTimeEntryCalculations( form );

    const { handleSubmit, handleCancel, ctxValue } = useTimeEntryUserFormHandlers( {
        isNew,
        form,
        hasPriviledge,
        timeentry,
        queryClient,
        router,
        modalContext,
        isOwnTimeEntry: true
    } );

    const formId = "time-entry-user-form";

    return <FormActionContext.Provider
        value={ { ...ctxValue, handleCancel } }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleSubmit ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-[65vh] my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <TimeEntryFragment projectId={ timeentry?.project.id ?? undefined }
                                           employeeId={ isNew ? employeeId : timeentry!.employee.id } total={ total }/>
                        <ErrorMessage/>
                        <SuccessMessage message={ `Time entry was ${ isNew ? "created" : "updated" } successfully` }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ isNew ? "New" : "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}