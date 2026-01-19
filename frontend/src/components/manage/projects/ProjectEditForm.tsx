/*
 * assemble
 * ProjectEditForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import {
    FieldValidationError,
    ProjectDTO,
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FormActionContext } from "@/store/formActionStore";
import { useRouter } from "@bprogress/next/app";
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import {
    getGetAllProjectsQueryKey,
    getGetProjectByIdQueryKey,
    useUpdateProject
} from "@/api/rest/generated/query/projects/projects";
import { FormActions } from "@/components/custom-ui/form/actions";
import useModalContext from "@/hooks/useModalContext";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@radix-ui/react-menu";
import { ProjectFormFragment } from "@/components/manage/projects/form/fragments";
import { zodResolver } from "@hookform/resolvers/zod";
import { ProjectUpdateFormData, ProjectUpdateSchema } from "@/types/projects/project.types";
import { invalidateQueries } from "@/utils/query";
import { toast } from "sonner";

type ProjectEditFormProps = {
    project: ProjectDTO;
}

export default function ProjectEditForm( { project }: Readonly<ProjectEditFormProps> ) {
    const router = useRouter();
    const queryClient = useQueryClient();
    const modalContext = useModalContext();

    const formId = "project-edit-form";
    const form = useForm( {
        resolver: zodResolver( ProjectUpdateSchema ),
        defaultValues: {
            name: project.name,
            active: project.active,
            type: project.type,
            stage: project.stage,
            category: project.category,
            description: project.description,
            color: project.color,
        }
    } );

    const { mutate, isPending, isSuccess, isError } = useUpdateProject();


    const handleSubmit = async ( data: ProjectUpdateFormData ) => {
        mutate(
            {
                id: project.id,
                data: {
                    ...data,
                }
            },
            {
                onSuccess: async () => {
                    await invalidateQueries( queryClient, [
                        getGetProjectByIdQueryKey( project.id ),
                        getGetAllProjectsQueryKey()
                    ] );
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "Project " + project.no + " was updated",
                        action: {
                            label: "View Project",
                            onClick: () => router.push( "/app/manage/projects/" + project.id )
                        }
                    } );
                    handleCancel();
                },
                onError: ( error ) => {
                    if ( ( error.status === 400 || error.status === 409 ) && error.response?.data ) {
                        const data = error.response.data;
                        if ( "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof ProjectUpdateFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        } else if ( "message" in data && data.message ) {
                            form.setError( "name", { type: "manual", message: data.message } )
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

    const formContext = {
        disableOnSuccess: false,
        isPending,
        isSuccess,
        isError,
        handleCancel
    } satisfies FormActionContext

    return <FormActionContext.Provider value={ formContext }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleSubmit ) } className={ "space-y-8" }>
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <ProjectFormFragment/>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Project was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
