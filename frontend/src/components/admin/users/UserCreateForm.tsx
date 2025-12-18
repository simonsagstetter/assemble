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
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { type UserCreateFormData, UserCreateSchema } from "@/types/users/user.types";
import { getGetAllUsersQueryKey, useCreateUser } from "@/api/rest/generated/query/user-management/user-management";
import {
    Field,
    FieldContent,
    FieldDescription,
    FieldError,
    FieldGroup,
    FieldLabel,
    FieldLegend,
    FieldSet
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import EmployeeSearch from "@/components/admin/users/EmployeeSearch";
import { Button } from "@/components/ui/button";
import { Switch } from "@/components/ui/switch";
import RoleSearch from "@/components/admin/users/RoleSearch";
import { Separator } from "@/components/ui/separator";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { toast } from "sonner";
import { FieldValidationError } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Spinner } from "@/components/ui/spinner";
import { useRouter } from "@bprogress/next/app";
import { use } from "react";
import { ModalContext } from "@/components/custom-ui/Modal";
import { ScrollArea } from "@/components/ui/scroll-area";

type UserCreateFormProps = {
    modal?: boolean
}

export default function UserCreateForm( { modal = false }: UserCreateFormProps ) {
    const modalContext = use( ModalContext );
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

    const { isSubmitting, errors } = form.formState;

    const createUser = useCreateUser();

    const { isPending, isError, isSuccess } = createUser;


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
                    if ( modal ) {
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
                            form.setError( "username", { type: "manual", message: "Username already exists." } )
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
        if ( modal ) {
            modalContext.setOpen( false );
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.push( "/app/admin/users" );
        }
    }

    return <form id={ "user-create-form" } onSubmit={ form.handleSubmit( handleCreateUser ) }
                 className="space-y-8">
        <ScrollArea className={ `${ modal ? "h-[65vh] my-0" : "" }` }>
            <FieldGroup className={ "py-4 px-8" }>
                <FieldSet>
                    <FieldLegend>Identity</FieldLegend>
                    <FieldDescription>Basic information about the user</FieldDescription>
                    <FieldGroup>
                        <Controller
                            name={ "username" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid }>
                                    <FieldLabel htmlFor={ "username-field" }>Username</FieldLabel>
                                    <Input
                                        { ...field }
                                        id={ "username-field" }
                                        aria-invalid={ fieldState.invalid }
                                        placeholder={ "e.g. musterpersonmax" }
                                        autoFocus
                                        disabled={ isPending || isSubmitting || isSuccess }
                                    />
                                    <FieldDescription>
                                        This field is required and must be a unique
                                    </FieldDescription>
                                    { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                    </FieldError> }
                                </Field>
                            ) }
                        />
                        <div className={ "grid grid-cols-2 gap-16" }>
                            <Controller
                                name={ "firstname" }
                                control={ form.control }
                                render={ ( { field, fieldState } ) => (
                                    <Field data-invalid={ fieldState.invalid }>
                                        <FieldLabel htmlFor={ "firstname-field" }>Firstname</FieldLabel>
                                        <Input
                                            { ...field }
                                            id={ "firstname-field" }
                                            aria-invalid={ fieldState.invalid }
                                            placeholder={ "e.g. Max" }
                                            disabled={ isPending || isSubmitting || isSuccess }
                                        />
                                        <FieldDescription>
                                            This field is required
                                        </FieldDescription>
                                        { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                        </FieldError> }
                                    </Field>
                                ) }
                            />
                            <Controller
                                name={ "lastname" }
                                control={ form.control }
                                render={ ( { field, fieldState } ) => (
                                    <Field data-invalid={ fieldState.invalid }>
                                        <FieldLabel htmlFor={ "lastname-field" }>Lastname</FieldLabel>
                                        <Input
                                            { ...field }
                                            id={ "lastname-field" }
                                            aria-invalid={ fieldState.invalid }
                                            placeholder={ "e.g. Mustermann" }
                                            disabled={ isPending || isSubmitting || isSuccess }
                                        />
                                        <FieldDescription>
                                            This field is required
                                        </FieldDescription>
                                        { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                        </FieldError> }
                                    </Field>
                                ) }
                            />
                        </div>
                        <Controller
                            name={ "email" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid }>
                                    <FieldLabel htmlFor={ "email-field" }>E-Mail</FieldLabel>
                                    <Input
                                        { ...field }
                                        id={ "email-field" }
                                        aria-invalid={ fieldState.invalid }
                                        placeholder={ "e.g. max.musterperson@example.com" }
                                        disabled={ isPending || isSubmitting || isSuccess }
                                    />
                                    <FieldDescription>
                                        This field is required
                                    </FieldDescription>
                                    { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                    </FieldError> }
                                </Field>
                            ) }
                        />
                    </FieldGroup>
                </FieldSet>
                <Separator/>
                <FieldSet>
                    <FieldLegend>Resources</FieldLegend>
                    <FieldDescription>Connect user to other resources</FieldDescription>
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
                                        assign a employee later.
                                    </FieldDescription>
                                    { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                    </FieldError> }
                                </Field>
                            ) }
                        />
                    </FieldGroup>
                </FieldSet>
                <Separator/>
                <FieldSet>
                    <FieldLegend>Authentication & Authorization</FieldLegend>
                    <FieldDescription>Choose status, password and roles</FieldDescription>
                    <FieldGroup>
                        <Controller
                            name={ "enabled" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid } orientation={ "horizontal" }>
                                    <FieldContent>
                                        <FieldLabel htmlFor={ "enabled-field" }>Enabled Status</FieldLabel>
                                        <FieldDescription>
                                            Enable oder disable the new user account
                                        </FieldDescription>
                                        { fieldState.invalid &&
                                            <FieldError errors={ [ fieldState.error ] }>
                                            </FieldError>
                                        }

                                    </FieldContent>
                                    <Switch
                                        id={ "enabled-field" }
                                        className={ "cursor-pointer" }
                                        name={ field.name }
                                        checked={ field.value }
                                        onCheckedChange={ field.onChange }
                                        disabled={ isPending || isSubmitting || isSuccess }/>
                                </Field>
                            ) }
                        />
                        <Controller
                            name={ "password" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid }>
                                    <FieldLabel htmlFor={ "password-field" }>Password</FieldLabel>
                                    <Input
                                        { ...field }
                                        id={ "password-field" }
                                        aria-invalid={ fieldState.invalid }
                                        placeholder="Choose a password..."
                                        type={ "password" }
                                        autoComplete={ "new-password" }
                                        disabled={ isPending || isSubmitting || isSuccess }
                                    />
                                    <FieldDescription>
                                        Leave this field empty if you want the password to be generated.
                                        Passwort Requirements:<br/>
                                        - Between 8 and 20 characters<br/>
                                        - At least one digit<br/>
                                        - At least one lowercase letter<br/>
                                        - At least one uppercase letter<br/>
                                        - At least one special character
                                    </FieldDescription>
                                    { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                    </FieldError> }
                                </Field>
                            ) }
                        />
                        <Controller
                            name={ "roles" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid }>
                                    <FieldLabel htmlFor={ "roles-field" }>Roles</FieldLabel>
                                    <RoleSearch field={ field }
                                                disabled={ isPending || isSubmitting || isSuccess }/>
                                    <FieldDescription>
                                        This field is required
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
                                { "User was created successfully" }
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
                    className={ "grow flex-1/3 cursor-pointer" }
                    onClick={ handleCancel }
                    disabled={ isPending || isSubmitting }
            >
                { isSuccess ? "Go back" : "Cancel" }
            </Button>
            <Button type="submit"
                    form={ "user-create-form" }
                    variant="default"
                    disabled={ isPending || isSubmitting || isSuccess }
                    className={ "flex-2/3 grow cursor-pointer" }
            >
                { isPending ?
                    <>
                        { "Processing" }
                        <Spinner/>
                    </>
                    : "New"
                }
            </Button>
        </Field>
    </form>
}
