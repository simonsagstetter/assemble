/*
 * assemble
 * UserEditForm.tsx
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
import { type UserUpdateFormData, UserUpdateSchema } from "@/types/users/user.types";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useUpdateUser
} from "@/api/rest/generated/query/user-management/user-management";
import { Field, FieldDescription, FieldError, FieldGroup, FieldLabel, FieldSet } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { toast } from "sonner";
import { FieldValidationError, UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Spinner } from "@/components/ui/spinner";
import { useRouter } from "@bprogress/next/app";
import { use } from "react";
import { ModalContext } from "@/components/custom-ui/Modal";
import { ScrollArea } from "@radix-ui/react-scroll-area";

type UserEditFormProps = {
    user: UserAdmin,
    modal?: boolean
}

export default function UserEditForm( { user, modal = false }: UserEditFormProps ) {
    const modalContext = use( ModalContext );
    const { username, firstname, lastname, email } = user;
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm<UserUpdateFormData>( {
        resolver: zodResolver( UserUpdateSchema ),
        defaultValues: {
            username,
            firstname,
            lastname,
            email,
        }
    } );

    const { isSubmitting, errors } = form.formState;

    const updateUser = useUpdateUser();

    const { isPending, isError, isSuccess } = updateUser;


    const handleUpdateUser = ( data: UserUpdateFormData ) => {
        updateUser.mutate( {
                id: user.id,
                data: {
                    username: data.username.toLowerCase(),
                    firstname: data.firstname,
                    lastname: data.lastname,
                    email: data.email.toLowerCase()
                }
            },
            {
                onSuccess: async ( user ) => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserByIdQueryKey( user.id ),
                        refetchType: "all"
                    } )
                    form.clearErrors();
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

                        if ( error.status === 400 && "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof UserUpdateFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        }

                        if ( error.status === 409 && "message" in data && data.message ) {
                            form.setError( "username", { type: "manual", message: "Username already exists." } )
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
            else router.replace( "/app/admin/users" );
        }
    }

    return <form id={ "user-edit-form" } onSubmit={ form.handleSubmit( handleUpdateUser ) } className="space-y-8">
        <ScrollArea className={ `${ modal ? "my-0" : "" }` }>
            <FieldGroup className={ "py-4 px-8" }>
                <FieldSet>
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
                                        disabled={ isPending || isSubmitting }
                                    />
                                    <FieldDescription>
                                        This field is required and must have a unique value
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
                                            disabled={ isPending || isSubmitting }
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
                                            disabled={ isPending || isSubmitting }
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
                                        disabled={ isPending || isSubmitting }
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
                    form={ "user-edit-form" }
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
