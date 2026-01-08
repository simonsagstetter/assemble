/*
 * assemble
 * ProjectCreateForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";


import useModalContext from "@/hooks/useModalContext";
import { useQueryClient } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import {
    FieldValidationError, ProjectDTOColor,
    ProjectDTOStage,
    ProjectDTOType
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FormActionContext } from "@/store/formActionStore";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import {
    ProjectCreateFormData,
    ProjectCreateSchema
} from "@/types/projects/project.types";
import { getGetAllProjectsQueryKey, useCreateProject } from "@/api/rest/generated/query/projects/projects";
import { InputField, SelectField, SwitchField, TextareaField } from "@/components/custom-ui/form/fields";
import { colorClasses, colorMobileClasses } from "@/config/calendar/calendar.config";

export default function ProjectCreateForm() {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( ProjectCreateSchema ),
        defaultValues: {
            name: "",
            active: true,
            type: ProjectDTOType.EXTERNAL,
            stage: ProjectDTOStage.PROPOSAL,
            category: "",
            description: "",
            color: ProjectDTOColor.PURPLE,
        }
    } )

    const { isSubmitting } = form.formState;

    const { mutate, isError, isSuccess, isPending } = useCreateProject();
    const disabled = isPending || isSubmitting || isSuccess;

    const handleCreateProject = async ( data: ProjectCreateFormData ) => {
        mutate(
            {
                data: {
                    ...data,
                }
            },
            {
                onSuccess: async ( project ) => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectsQueryKey()
                    } )
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "Project " + project.no + " was created",
                        action: {
                            label: "View Project",
                            onClick: () => router.push( "/app/manage/projects/" + project.id )
                        }
                    } );
                    if ( modalContext ) {
                        handleCancel();
                    }
                },
                onError: ( error ) => {
                    if ( ( error.status === 400 || error.status === 409 ) && error.response?.data ) {
                        const data = error.response.data;
                        if ( "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof ProjectCreateFormData, {
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
        if ( modalContext ) {
            modalContext.setOpen( false );
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.push( "/app/manage/projects" );
        }
    }

    return <FormActionContext.Provider
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "project-create-form" }
                  onSubmit={ form.handleSubmit( handleCreateProject ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldLegend>Details</FieldLegend>
                            <FieldDescription>Detailed information about the new project</FieldDescription>
                            <FieldGroup>
                                <div className={ "grid grid-cols-2 gap-16" }>
                                    <InputField fieldName={ "name" }
                                                formControl={ form.control }
                                                label={ "Name" }
                                                placeholder={ "e.g. BERLINSTR 200 SANIERUNG" }
                                                disabled={ disabled }
                                                autoFocus
                                    >
                                        This field is required and must be unique.
                                    </InputField>
                                    <SwitchField fieldName={ "active" }
                                                 formControl={ form.control }
                                                 label={ "Status" }
                                                 disabled={ disabled }
                                    >
                                        Employees wont be able to create time entries on inactive projects.
                                    </SwitchField>
                                </div>
                                <div className={ "grid grid-cols-2 gap-16" }>
                                    <SelectField fieldName={ "type" }
                                                 formControl={ form.control }
                                                 label={ "Project Type" }
                                                 placeholder={ "Choose a type" }
                                                 options={ [
                                                     { label: "External", value: ProjectDTOType.EXTERNAL },
                                                     { label: "Internal", value: ProjectDTOType.INTERNAL },
                                                 ] }
                                                 disabled={ disabled }
                                    />
                                    <SelectField fieldName={ "stage" }
                                                 formControl={ form.control }
                                                 label={ "Project Stage" }
                                                 placeholder={ "Choose a stage" }
                                                 options={ [
                                                     { label: "Proposal", value: ProjectDTOStage.PROPOSAL },
                                                     { label: "Negotiation", value: ProjectDTOStage.NEGOTIATION },
                                                     { label: "Assigned", value: ProjectDTOStage.ASSIGNED },
                                                     { label: "Implementation", value: ProjectDTOStage.IMPLEMENTATION },
                                                     { label: "Final Billing", value: ProjectDTOStage.FINAL_BILLING },
                                                     { label: "Completed", value: ProjectDTOStage.COMPLETED },
                                                     { label: "Closed", value: ProjectDTOStage.CLOSED },
                                                 ] }
                                                 disabled={ disabled }
                                    />
                                </div>
                                <div className={ "grid grid-cols-2 gap-16" }>
                                    <InputField fieldName={ "category" }
                                                formControl={ form.control }
                                                label={ "Category" }
                                                placeholder={ "e.g. Sanierung" }
                                                disabled={ disabled }
                                    />
                                    <SelectField fieldName={ "color" }
                                                 formControl={ form.control }
                                                 label={ "Project Color" }
                                                 placeholder={ "Choose a color" }
                                                 options={ Object.keys( colorClasses ).map( key => {
                                                     const value = ProjectDTOColor[ key.toUpperCase() as keyof typeof ProjectDTOColor ];
                                                     return {
                                                         label: key,
                                                         value,
                                                         elem: <>
                                                             <span
                                                                 className={ "size-2 rounded-full " + colorMobileClasses[ key as keyof typeof colorClasses ] }>
                                                             </span>
                                                             { value.toLowerCase() }
                                                         </>
                                                     }
                                                 } ) }
                                                 disabled={ disabled }>
                                        These colors are used to visualize the project in the calendar.
                                    </SelectField>
                                </div>
                                <TextareaField fieldName={ "description" }
                                               formControl={ form.control }
                                               label={ "Description" }
                                               placeholder={ "e.g. The project is about the construction of a new building in Berlin." }
                                               disabled={ disabled }
                                />
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Project was created successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "project-create-form" } label={ "New" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}