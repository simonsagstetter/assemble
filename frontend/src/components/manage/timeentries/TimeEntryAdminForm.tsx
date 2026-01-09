/*
 * assemble
 * TimeEntryAdminForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";


import {
    TimeEntryDTO,
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useState } from "react";
import { useRouter } from "@bprogress/next/app";
import useModalContext from "@/hooks/useModalContext";
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { TimeEntrySchema } from "@/types/timeentries/timeentry.types";
import { FormActionContext } from "@/store/formActionStore";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { EmployeeLookupField } from "@/components/manage/employees/form/custom-fields";
import { TimeEntryFragment } from "@/components/manage/timeentries/form/fragments";
import { getTimeEntryDefaultValues } from "@/components/manage/timeentries/form/values";
import { useTimeEntryCalculations } from "@/components/manage/timeentries/form/utils";
import { useTimeEntryAdminFormHandlers } from "@/components/manage/timeentries/form/handlers";

type TimeEntryAdminFormProps = {
    timeentry?: TimeEntryDTO;
}

export default function TimeEntryAdminForm( { timeentry }: TimeEntryAdminFormProps ) {
    const isNew = timeentry === undefined;
    const router = useRouter();
    const modalContext = useModalContext();
    const queryClient = useQueryClient();

    const [ employeeIdIsSelected, setEmployeeIdIsSelected ] = useState<string>(
        isNew ? "" : timeentry?.employee.id ?? ""
    );


    const form = useForm( {
        resolver: zodResolver( TimeEntrySchema ),
        defaultValues: getTimeEntryDefaultValues( timeentry )
    } );

    const { total } = useTimeEntryCalculations( form );

    const { handleSubmit, handleCancel, ctxValue } = useTimeEntryAdminFormHandlers( {
        isNew,
        form,
        timeentry,
        queryClient,
        router,
        modalContext,
        isOwnTimeEntry: false
    } );

    const formId = "time-entry-admin-form";
    const { isSubmitting } = form.formState;


    return <FormActionContext.Provider
        value={ { ...ctxValue, handleCancel } }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleSubmit ) }
                  className={ "space-y-8" }
            >
                <ScrollArea
                    className={ `${ modalContext ? employeeIdIsSelected ? "h-[65vh] my-0" : "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldLegend>Employee</FieldLegend>
                            <FieldDescription>Select the employee you want to create a time entry for</FieldDescription>
                            <FieldGroup>
                                <EmployeeLookupField fieldName={ "employeeId" }
                                                     formControl={ form.control }
                                                     disabled={ isSubmitting || ctxValue.isError || ctxValue.isPending || ctxValue.isSuccess }
                                                     excludeValues={ [] }
                                                     onSelectHandler={ setEmployeeIdIsSelected }
                                />

                            </FieldGroup>
                        </FieldSet>
                        { employeeIdIsSelected ?
                            <TimeEntryFragment total={ total }
                                               projectId={ timeentry?.project.id ?? undefined }
                                               employeeId={ form.getValues( "employeeId" ) }
                                               isRestrictedSearch={ false }
                            />
                            : null }
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