/*
 * assemble
 * UserUpdateEmployeeForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { use } from "react";
import { ModalContext } from "@/components/custom-ui/Modal";
import { useRouter } from "@bprogress/next/app";
import { useQueryClient } from "@tanstack/react-query";
import { Controller, useForm } from "react-hook-form";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useUpdateUserEmployee,
} from "@/api/rest/generated/query/user-management/user-management";
import { UserUpdateEmpployeeFormData, UserUpdateEmpployeeSchema, } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { Field, FieldDescription, FieldError, FieldGroup, FieldLabel, FieldSet } from "@/components/ui/field";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { Separator } from "@/components/ui/separator";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { toast } from "sonner";
import EmployeeSearch from "@/components/admin/users/EmployeeSearch";

type UpdateEmployeeFormProps = {
    user: UserAdmin,
    modal?: boolean
}

export default function UserUpdateEmployeeForm( { user, modal = false }: UpdateEmployeeFormProps ) {
    const modalContext = use( ModalContext );
    const router = useRouter();
    const queryClient = useQueryClient();
    const form = useForm<UserUpdateEmpployeeFormData>( {
        resolver: zodResolver( UserUpdateEmpployeeSchema ),
        defaultValues: {
            employeeId: ""
        }
    } );
    const { errors, isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserEmployee();

    const handleUpdateEmployee = ( data: UserUpdateEmpployeeFormData ) => {
        mutate(
            {
                id: user.id,
                data: {
                    employeeId: data.employeeId ? data.employeeId : undefined
                }
            },
            {
                onSuccess: async ( user ) => {
                    form.clearErrors();
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserByIdQueryKey( user.id ),
                        refetchType: "all"
                    } )
                    toast.success( "Success", {
                        description: "User " + user.username + " was updated",
                        action: {
                            label: "View User",
                            onClick: () => router.push( "/app/admin/users/" + user.id )
                        }
                    } );
                    if ( modal ) {
                        handleCancel();
                    }
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( ( error.status === 404 || error.status === 400 ) && "message" in data && data.message ) {
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
        if ( modal ) {
            modalContext.setOpen( false );
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.replace( "/app/admin/users" );
        }
    }

    return <form id={ "user-update-employee-form" } onSubmit={ form.handleSubmit( handleUpdateEmployee ) }
                 className="space-y-8">
        <ScrollArea className={ `${ modal ? "my-0" : "" }` }>
            <FieldGroup className={ "py-4 px-8" }>
                <FieldSet>
                    <FieldGroup>
                        <Controller
                            name={ "employeeId" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid }>
                                    <FieldLabel htmlFor={ "employeeId-field" }>Employee</FieldLabel>
                                    <EmployeeSearch field={ field }
                                                    disabled={ isPending || isSubmitting || isSuccess }/>
                                    <FieldDescription>
                                        Connect a user to a an employee. Leave this field empty if you want to
                                        disconnect
                                        the current employee.
                                    </FieldDescription>
                                    { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                    </FieldError> }
                                </Field>
                            ) }
                        />
                    </FieldGroup>
                </FieldSet>
                { isError && errors.root && (
                    <FieldGroup>
                        <Alert variant="destructive">
                            <AlertCircleIcon/>
                            <AlertTitle>Oops! We encountered an error.</AlertTitle>
                            <AlertDescription>
                                { errors.root.message }
                            </AlertDescription>
                        </Alert>
                    </FieldGroup>
                ) }
                { isSuccess && (
                    <FieldGroup>
                        <Alert variant="default">
                            <CheckCircle2Icon/>
                            <AlertTitle>
                                { "User was updated successfully" }
                            </AlertTitle>
                        </Alert>
                    </FieldGroup>
                ) }
            </FieldGroup>
        </ScrollArea>
        <Separator className={ "my-0" }/>
        <Field orientation={ "horizontal" } className={ "p-8" }>
            <Button type="button"
                    variant="secondary"
                    className={ "flex-1/3 grow cursor-pointer" }
                    onClick={ handleCancel }
                    disabled={ isPending || isSubmitting }
            >
                { isSuccess ? "Go back" : "Cancel" }
            </Button>
            <Button type="submit"
                    form={ "user-update-employee-form" }
                    variant="default"
                    disabled={ isPending || isSubmitting }
                    className={ "flex-2/3 grow cursor-pointer" }
            >
                { isPending ?
                    <>
                        { "Processing" }
                        <Spinner/>
                    </>
                    : "Save"
                }
            </Button>
        </Field>
    </form>
}

