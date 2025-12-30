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
import useModalContext from "@/hooks/useModalContext";
import { useQueryClient } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import { toast } from "sonner";
import { FormActionContext } from "@/store/formActionStore";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import {
    getGetAllProjectsQueryKey,
    getGetProjectByIdQueryKey,
    useDeleteProjectById
} from "@/api/rest/generated/query/projects/projects";

type ProjectDeleteFormProps = {
    project: ProjectDTO
}

export default function ProjectDeleteForm( { project }: ProjectDeleteFormProps ) {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();
    const form = useForm();
    const { mutate, isPending, isSuccess, isError } = useDeleteProjectById();

    const handleDeleteProject = () => {
        mutate(
            {
                id: project.id
            },
            {
                onSuccess: async () => {
                    toast.success( "Success", {
                        description: "Project " + project.no + " was deleted",
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectsQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetProjectByIdQueryKey( project.id ),
                        refetchType: "none"
                    } );
                    if ( modalContext ) {
                        modalContext.setOpen( false );
                    }
                    setTimeout( () => router.push( "/app/manage/projects" ), 200 );
                    router.back();
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: "Project could not be deleted." } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            }
        )
    }

    const handleCancel = () => {
        if ( modalContext ) {
            modalContext.setOpen( false );
        }
        router.back();
    }

    return <FormActionContext.Provider
        value={ { isSuccess, isPending, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "project-delete-form" } onSubmit={ form.handleSubmit( handleDeleteProject ) }
                  className="space-y-8">
                <FieldGroup className={ "p-8 my-0" }>
                    <p className={ "text-xl font-semibold text-center" }>
                        Are you sure you want to delete project <strong>{ project.no }</strong>?
                    </p>
                    <ErrorMessage/>
                </FieldGroup>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "project-delete-form" } label={ "Delete" } variant={ "destructive" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
