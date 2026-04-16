/*
 * assemble
 * ProjectAssignmentDeleteForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { ProjectAssignmentDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import {
    useDeleteProjectAssignmentById
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { useSearchParams } from "next/navigation";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getDeleteProjectAssignmentInvalidations } from "@/components/projects/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type ProjectAssignmentDeleteFormProps = {
    assignment: ProjectAssignmentDTO
}

export default function ProjectAssignmentDeleteForm( { assignment }: Readonly<ProjectAssignmentDeleteFormProps> ) {
    const router = useRouter();
    const form = useForm();
    const { mutate, isPending, isSuccess, isError } = useDeleteProjectAssignmentById();
    const params = useSearchParams();
    const origin = params.get( "origin" ) || "project";

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: () => ( {
            id: assignment.id
        } ),
        onSuccessCallbackAction: () => {
            if ( origin === "project" ) router.push( `/app/manage/projects/${ assignment.project.id }?tab=team` )
            else router.push( `/app/manage/employees/${ assignment.employee.id }?tab=projects` )
        },
        setInvalidations: () => getDeleteProjectAssignmentInvalidations( assignment ),
        setToastSuccessOptions: () => ( {
            description: "Assignment was deleted"
        } )
    } )

    return <FormBuilder form={ form }
                        formId={ "project-assignment-delete-form" }
                        status={ {
                            isSuccess,
                            isPending,
                            isError
                        } }
                        options={ {
                            actionLabel: "Delete",
                            actionVariant: "destructive",
                            successMessage: "Assignment was deleted successfully"
                        } }
                        submitHandler={ handleSubmit }
                        cancelHandler={ () => router.back() }

    >
        <FieldGroup className={ "p-8 my-0" }>
            <p className={ "text-xl font-semibold text-center" }>
                Are you sure you want to delete this assignment?
            </p>
            <ErrorMessage/>
        </FieldGroup>
    </FormBuilder>
}
