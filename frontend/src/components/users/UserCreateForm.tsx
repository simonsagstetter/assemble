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
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { type UserCreateFormData, UserCreateSchema } from "@/types/users/user.types";
import { useCreateUser } from "@/api/rest/generated/query/user-management/user-management";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { useRouter } from "@bprogress/next/app";
import { RolesLookupField } from "@/components/users/form/custom-fields";
import { InputField, SwitchField } from "@/components/custom-ui/form/fields";
import { IdentityFragment } from "@/components/users/form/fragments";
import { UnlinkedEmployeeLookupField } from "@/components/employees/form/custom-fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getCreateUserInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

export default function UserCreateForm() {
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

    const {
        mutate,
        isError,
        isPending,
        isSuccess
    } = useCreateUser();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
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
        } ),
        setInvalidations: () => getCreateUserInvalidations(),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "User " + data.username + " was created",
            action: {
                label: "View User",
                onClick: () => router.push( "/app/admin/users/" + data.id )
            }
        } )
    } );

    return <FormBuilder form={ form }
                        formId={ "create-user-form" }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        status={ {
                            isPending,
                            isError,
                            isSuccess
                        } }
                        options={ {
                            maxScrollHeightClassName: "h-[65vh]",
                            disabledOnSuccess: true,
                            actionLabel: "New",
                            successMessage: "User was created successfully",
                        } }

    >
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
                <UnlinkedEmployeeLookupField fieldName={ "employeeId" } formControl={ form.control }
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
    </FormBuilder>
}