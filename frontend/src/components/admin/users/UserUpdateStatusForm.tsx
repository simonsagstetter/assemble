/*
 * assemble
 * UserUpdateStatusForm.tsx
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
    useUpdateUserStatus
} from "@/api/rest/generated/query/user-management/user-management";
import { type UserStatusFormData, UserStatusSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import {
    Field,
    FieldContent,
    FieldDescription,
    FieldError,
    FieldGroup,
    FieldLabel,
    FieldSet
} from "@/components/ui/field";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { Separator } from "@/components/ui/separator";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { Switch } from "@/components/ui/switch";
import { toast } from "sonner";

type UserStatusFormProps = {
    user: UserAdmin,
    modal?: boolean
}

export default function UserUpdateStatusForm( { user, modal = false }: UserStatusFormProps ) {
    const modalContext = use( ModalContext );
    const router = useRouter();
    const queryClient = useQueryClient();
    const { enabled, locked } = user;
    const form = useForm<UserStatusFormData>( {
        resolver: zodResolver( UserStatusSchema ),
        defaultValues: {
            enabled,
            locked
        }
    } );
    const { errors, isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserStatus();

    const handleUpdateStatus = ( data: UserStatusFormData ) => {
        mutate(
            {
                id: user.id,
                data: {
                    enabled: data.enabled,
                    locked: data.locked
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
                    if ( error.status === 404 && error.response?.data ) {
                        const data = error.response.data;
                        form.setError( "root", { type: "manual", message: data.message } );
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

    return <form id={ "user-status-form" } onSubmit={ form.handleSubmit( handleUpdateStatus ) } className="space-y-8">
        <ScrollArea className={ `${ modal ? "my-0" : "" }` }>
            <FieldGroup className={ "py-4 px-8" }>
                <FieldSet>
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
                            name={ "locked" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid } orientation={ "horizontal" }>
                                    <FieldContent>
                                        <FieldLabel htmlFor={ "locked-field" }>Locked Status</FieldLabel>
                                        <FieldDescription>
                                            Locked users cannot log in but can still be connected to data
                                        </FieldDescription>
                                        { fieldState.invalid &&
                                            <FieldError errors={ [ fieldState.error ] }>
                                            </FieldError>
                                        }

                                    </FieldContent>
                                    <Switch
                                        id={ "locked-field" }
                                        className={ "cursor-pointer" }
                                        name={ field.name }
                                        checked={ field.value }
                                        onCheckedChange={ field.onChange }
                                        disabled={ isPending || isSubmitting || isSuccess }/>
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
                    form={ "user-status-form" }
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
