/*
 * assemble
 * ProjectDeleteForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { ProjectDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import {
    useDeleteProjectById
} from "@/api/rest/generated/query/projects/projects";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getDeleteProjectInvalidations } from "@/components/projects/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type ProjectDeleteFormProps = {
    project: ProjectDTO
}

export default function ProjectDeleteForm( { project }: Readonly<ProjectDeleteFormProps> ) {
    const router = useRouter();

    const form = useForm();

    const { mutate, isPending, isSuccess, isError } = useDeleteProjectById();

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            id: project.id,
            data
        } ),
        onSuccessCallbackAction: () => router.push( "/app/manage/projects" ),
        setInvalidations: () => getDeleteProjectInvalidations( project ),
        setToastSuccessOptions: () => ( {
            description: "Project " + project.no + " was deleted"
        } )
    } )


    return <FormBuilder form={ form }
                        formId={ "project-delete-form" }
                        status={ {
                            isSuccess,
                            isPending,
                            isError
                        } }
                        options={ {
                            actionLabel: "Delete",
                            actionVariant: "destructive",
                            successMessage: "Project was deleted successfully"
                        } }
                        submitHandler={ handleSubmit }
                        cancelHandler={ () => router.back() }
    >
        <FieldGroup className={ "p-8 my-0" }>
            <p className={ "text-xl font-semibold text-center" }>
                Are you sure you want to delete project <strong>{ project.no }</strong>?
            </p>
            <ErrorMessage/>
        </FieldGroup>
    </FormBuilder>
}
