/*
 * assemble
 * TimeEntryDeleteForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { useDeleteOwnTimeEntryById, } from "@/api/rest/generated/query/timeentries/timeentries";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getDeleteTimeEntryInvalidations } from "@/components/timeentries/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type TimeEntryDeleteFormProps = {
    timeentry: TimeEntryDTO;
}

export default function TimeEntryDeleteForm( { timeentry }: Readonly<TimeEntryDeleteFormProps> ) {
    const router = useRouter();
    const form = useForm();
    const { mutate, isPending, isSuccess, isError } = useDeleteOwnTimeEntryById();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: () => ( {
            id: timeentry.id
        } ),
        setInvalidations: () => getDeleteTimeEntryInvalidations( timeentry ),
        onSuccessCallbackAction: () => router.push( "/app/timetracking/calendar" ),
        setToastSuccessOptions: () => ( {
            description: "Time entry deleted"
        } )
    } )

    return <FormBuilder form={ form } formId={ "time-entry-admin-delete-form" }
                        status={ { isSuccess, isError, isPending } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Delete",
                            actionVariant: "destructive",
                            disabledOnSuccess: true,
                            successMessage: "Time entry was deleted successfully"
                        } }
    >
        <p className={ "text-xl font-semibold text-center" }>
            Are you sure you want to delete timeentry <strong>{ timeentry.no }</strong>?
        </p>
    </FormBuilder>
}
