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

import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    ProjectAssignmentDTO,
    ProjectDTO
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { ProjectAssignmentCreateSchema } from "@/types/projects/project.types";
import {
    useCreateProjectAssignment
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { ProjectLookupField } from "@/components/projects/form/custom-fields";
import { CurrencyField, SwitchField } from "@/components/custom-ui/form/fields";
import { EmployeeDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { EmployeeLookupField } from "@/components/employees/form/custom-fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getCreateProjectAssignmentInvalidations } from "@/components/projects/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type ProjectAssignmentFormProps = {
    project?: ProjectDTO,
    employee?: EmployeeDTO,
    assignments: ProjectAssignmentDTO[]
}

export default function ProjectAssignmentCreateForm(
    { assignments, project, employee }: Readonly<ProjectAssignmentFormProps>
) {
    const isProject = project !== undefined && employee === undefined;
    const formId = isProject ? "employee-assignment-form" : "project-assignment-form";

    const excludeValues = assignments
        .map( ( assignment ) => isProject ? assignment.employee.id : assignment.project.id );

    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( ProjectAssignmentCreateSchema ),
        defaultValues: {
            employeeId: employee?.id ?? "",
            projectId: project?.id ?? "",
            active: true,
            hourlyRate: 0
        }
    } );

    const { isSubmitting } = form.formState;

    const { isPending, isError, isSuccess, mutate } = useCreateProjectAssignment();

    const disabled = isPending || isSuccess || isSubmitting;

    const handleCancel = () => {
        router.back();
    }

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            data
        } ),
        onSuccessCallbackAction: handleCancel,
        setInvalidations: ( data ) => getCreateProjectAssignmentInvalidations( data ),
        setToastSuccessOptions: ( data ) => ( {
            description: ( isProject ? "Employee " : "Project " ) + " was assigned",
            action: {
                label: "View",
                onClick: () => isProject ? router.push(
                    `/app/manage/projects/${ data.project.id }?tab=team`
                ) : router.push( `/app/manage/employees/${ data.employee.id }?tab=projects` )
            }
        } )
    } )

    return <FormBuilder form={ form }
                        formId={ formId }
                        status={ {
                            isPending,
                            isError,
                            isSuccess,
                        } }
                        options={ {
                            actionLabel: "New",
                            successMessage: "Assignment was created successfully",
                            disabledOnSuccess: true
                        } }
                        submitHandler={ handleSubmit }
                        cancelHandler={ handleCancel }
    >
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
    </FormBuilder>
}