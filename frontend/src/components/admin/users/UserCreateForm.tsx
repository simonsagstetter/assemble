/*
 * assemble
 * UserCreateForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { type UserCreateFormData, UserCreateSchema } from "@/types/users/user.types";
import { getGetAllUsersQueryKey, useCreateUser } from "@/api/rest/generated/query/user-management/user-management";
import {
    FieldDescription,
    FieldGroup,
    FieldLegend,
    FieldSet
} from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { toast } from "sonner";
import { FieldValidationError } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { ScrollArea } from "@/components/ui/scroll-area";
import { EmployeeLookupField, RolesLookupField } from "@/components/admin/users/form/custom-fields";
import { InputField, SwitchField } from "@/components/custom-ui/form/fields";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FormActions } from "@/components/custom-ui/form/actions";
import { FormActionContext } from "@/store/formActionStore";
import { IdentityFragment } from "@/components/admin/users/form/fragments";
import useModalContext from "@/hooks/useModalContext";

export default function UserCreateForm() {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm<UserCreateFormData>( {
        resolver: zodResolver( UserCreateSchema ),
        defaultValues: {
            username: "",
            password: "",
            firstname: "",
            lastname: "",
            email: "",
            roles: [],
            enabled: true,
            employeeId: ""
        }
    } );

    const { isSubmitting } = form.formState;

    const createUser = useCreateUser();

    const { isPending, isSuccess, isError } = createUser;


    const handleCreateUser = ( data: UserCreateFormData ) => {
        createUser.mutate( {
                data: {
                    username: data.username.toLowerCase(),
                    password: data.password === "" ? undefined : data.password,
                    firstname: data.firstname,
                    lastname: data.lastname,
                    email: data.email.toLowerCase(),
                    roles: data.roles,
                    enabled: data.enabled,
                    employeeId: data.employeeId === "" ? undefined : data.employeeId
                }
            },
            {
                onSuccess: async ( user ) => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey()
                    } )
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "User " + user.username + " was created",
                        action: {
                            label: "View User",
                            onClick: () => router.push( "/app/admin/users/" + user.id )
                        }
                    } );
                    if ( modalContext ) {
                        handleCancel();
                    }
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;

                        if ( error.status === 400 && "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof UserCreateFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        }

                        if ( error.status === 409 && "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: "Username already exists." } )
                        }

                        if ( error.status === 404 && "message" in data && data.message ) {
                            form.setError( "employeeId", { type: "manual", message: data.message } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            } )
    }

    const handleCancel = () => {
        if ( modalContext ) {
            modalContext.setOpen( false );
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.push( "/app/admin/users" );
        }
    }

    return <FormActionContext.Provider
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "user-create-form" } onSubmit={ form.handleSubmit( handleCreateUser ) }
                  className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "h-[65vh] my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldLegend>Identity</FieldLegend>
                            <FieldDescription>Basic information about the user</FieldDescription>
                            <IdentityFragment/>
                        </FieldSet>
                        <Separator/>
                        <FieldSet>
                            <FieldLegend>Resources</FieldLegend>
                            <FieldDescription>Connect user to other resources</FieldDescription>
                            <FieldGroup>
                                <EmployeeLookupField fieldName={ "employeeId" } formControl={ form.control }
                                                     disabled={ isPending || isSubmitting || isSuccess }/>
                            </FieldGroup>
                        </FieldSet>
                        <Separator/>
                        <FieldSet>
                            <FieldLegend>Authentication & Authorization</FieldLegend>
                            <FieldDescription>Choose status, password and roles</FieldDescription>
                            <FieldGroup>
                                <SwitchField fieldName={ "enabled" }
                                             formControl={ form.control }
                                             label={ "Enabled Status" }
                                             disabled={ isPending || isSubmitting || isSuccess }
                                >
                                    Enable or disable the new user account
                                </SwitchField>
                                <InputField fieldName={ "password" }
                                            formControl={ form.control }
                                            label={ "Password" }
                                            placeholder={ "Choose a password..." }
                                            type={ "password" }
                                            autoComplete={ "new-password" }
                                            disabled={ isPending || isSubmitting || isSuccess }
                                >
                                    Leave this field empty if you want the password to be generated.
                                    Password Requirements:<br/>
                                    - Between 8 and 20 characters<br/>
                                    - At least one digit<br/>
                                    - At least one lowercase letter<br/>
                                    - At least one uppercase letter<br/>
                                    - At least one special character
                                </InputField>
                                <RolesLookupField fieldName={ "roles" } formControl={ form.control }
                                                  disabled={ isPending || isSubmitting || isSuccess }/>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "User was created successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-create-form" } label={ "New" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}