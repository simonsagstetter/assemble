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
    getGetAllProjectAssignmentsByEmployeeIdQueryKey,
    getGetAllProjectAssignmentsByProjectIdQueryKey, getGetOwnProjectAssignmentsQueryKey,
    useDeleteProjectAssignmentById
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { useSearchParams } from "next/navigation";

type ProjectAssignmentDeleteFormProps = {
    assignment: ProjectAssignmentDTO
}

export default function ProjectAssignmentDeleteForm( { assignment }: ProjectAssignmentDeleteFormProps ) {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();
    const form = useForm();
    const { mutate, isPending, isSuccess, isError } = useDeleteProjectAssignmentById();
    const params = useSearchParams();
    const origin = params.get( "origin" ) || "project";

    const handleDeleteProjectAssignment = () => {
        mutate(
            {
                id: assignment.id
            },
            {
                onSuccess: async () => {
                    toast.success( "Success", {
                        description: "Assignment was deleted",
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectAssignmentsByProjectIdQueryKey( assignment.project.id ),
                        refetchType: "all"
                    } )

                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectAssignmentsByEmployeeIdQueryKey( assignment.employee.id ),
                        refetchType: "all"
                    } )

                    await queryClient.invalidateQueries( {
                        queryKey: getGetOwnProjectAssignmentsQueryKey()
                    } );

                    if ( origin === "project" ) router.push( `/app/manage/projects/${ assignment.project.id }?tab=team` )
                    else router.push( `/app/manage/employees/${ assignment.employee.id }?tab=projects` )

                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: "Assignment could not be deleted." } )
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
        value={ { isSuccess, isPending, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "project-assignment-delete-form" }
                  onSubmit={ form.handleSubmit( handleDeleteProjectAssignment ) }
                  className="space-y-8">
                <FieldGroup className={ "p-8 my-0" }>
                    <p className={ "text-xl font-semibold text-center" }>
                        Are you sure you want to delete this assignment?
                    </p>
                    <ErrorMessage/>
                </FieldGroup>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "project-assignment-delete-form" } label={ "Delete" } variant={ "destructive" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
