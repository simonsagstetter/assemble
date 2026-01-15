/*
 * assemble
 * ProjectAssignmentCreateForm.tsx
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
    FieldValidationError,
    ProjectAssignmentDTO,
    ProjectDTO
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FormActionContext } from "@/store/formActionStore";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { ProjectAssignmentCreateFormData, ProjectAssignmentCreateSchema } from "@/types/projects/project.types";
import {
    getGetAllProjectAssignmentsByEmployeeIdQueryKey,
    getGetAllProjectAssignmentsByProjectIdQueryKey, getGetOwnProjectAssignmentsQueryKey,
    useCreateProjectAssignment
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { ProjectLookupField } from "@/components/manage/projects/form/custom-fields";
import { CurrencyField, SwitchField } from "@/components/custom-ui/form/fields";
import { EmployeeDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { EmployeeLookupField } from "@/components/manage/employees/form/custom-fields";

type ProjectAssignmentFormProps = {
    project?: ProjectDTO,
    employee?: EmployeeDTO,
    assignments: ProjectAssignmentDTO[]
}

export default function ProjectAssignmentCreateForm(
    { assignments, project, employee }: ProjectAssignmentFormProps
) {
    const isProject = project !== undefined && employee === undefined;
    const formId = isProject ? "employee-assignment-form" : "project-assignment-form";

    const excludeValues = assignments
        .map( ( assignment ) => isProject ? assignment.employee.id : assignment.project.id );

    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( ProjectAssignmentCreateSchema ),
        defaultValues: {
            employeeId: employee?.id ?? "",
            projectId: project?.id ?? "",
            active: true,
            hourlyRate: 0.0
        }
    } );

    const { isSubmitting } = form.formState;

    const { isPending, isError, isSuccess, mutate } = useCreateProjectAssignment();

    const disabled = isPending || isSuccess || isSubmitting;

    const handleCreateProjectAssignment = async ( data: ProjectAssignmentCreateFormData ) => {
        mutate(
            {
                data: {
                    ...data,
                }
            },
            {
                onSuccess: async ( assignment ) => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectAssignmentsByProjectIdQueryKey( assignment.project.id )
                    } );
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectAssignmentsByEmployeeIdQueryKey( assignment.employee.id )
                    } );
                    await queryClient.invalidateQueries( {
                        queryKey: getGetOwnProjectAssignmentsQueryKey()
                    } );

                    form.clearErrors();
                    toast.success( "Success", {
                        description: ( isProject ? "Employee " : "Project " ) + " was assigned",
                        action: {
                            label: "View",
                            onClick: () => isProject ? router.push(
                                `/app/manage/projects/${ assignment.project.id }?tab=team`
                            ) : router.push( `/app/manage/employees/${ assignment.employee.id }?tab=projects` )
                        }
                    } );
                    handleCancel();
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( error.status === 400 && "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof ProjectAssignmentCreateFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        } else if ( error.status === 409 && "message" in data && data.message ) {
                            form.setError( isProject ? "employeeId" : "projectId", {
                                type: "manual",
                                message: data.message
                            } )
                        } else if ( error.status === 404 && "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: data.message } )
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
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ formId }
                  onSubmit={ form.handleSubmit( handleCreateProjectAssignment ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldLegend>Project Assignment Details</FieldLegend>
                            <FieldDescription>Assignments are used to identify if a employee can add billable
                                time entries on a specific project</FieldDescription>
                            <FieldGroup>
                                { isProject ?
                                    <EmployeeLookupField fieldName={ "employeeId" }
                                                         formControl={ form.control }
                                                         disabled={ disabled }
                                                         excludeValues={ excludeValues }
                                    />
                                    :
                                    <ProjectLookupField fieldName={ "projectId" }
                                                        formControl={ form.control }
                                                        disabled={ disabled }
                                                        excludeValues={ excludeValues }
                                    />
                                }
                                <CurrencyField fieldName={ "hourlyRate" }
                                               formControl={ form.control }
                                               label={ "Hourly Rate" }
                                               placeholder={ "e.g. 29,50" }
                                               disabled={ disabled }
                                >
                                    Optional. Leave empty if you want to use the default hourly rate of the project.
                                </CurrencyField>
                                <SwitchField fieldName={ "active" }
                                             formControl={ form.control }
                                             label={ "Status" }
                                             disabled={ disabled }
                                >
                                    Employees wont be able to create time entries on inactive projects.
                                </SwitchField>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage
                            message={ ( isProject ? "Employee" : "Project" ) + " was assigned to the project successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}