/*
 * assemble
 * Login.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { Controller, useForm } from "react-hook-form";
import { LoginForm, LoginFormSchema } from "@/types/auth/auth.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { submitLogin } from "@/services/rest/auth/auth";
import { toast } from "sonner";
import { Field, FieldError, FieldGroup, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon } from "lucide-react";
import { Spinner } from "@/components/ui/spinner";
import { useRouter } from "@bprogress/next/app";
import { LOGIN_REDIRECT_PATH } from "@/config/auth/auth.config";
import { useEffect } from "react";
import { isValidRoutePath } from "@/utils/url";
import { useSearchParams } from "next/navigation";
import { LoginRequest } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";

export default function Login() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const form = useForm<LoginRequest>( {
        resolver: zodResolver( LoginFormSchema ),
        defaultValues: { username: "", password: "" }
    } )

    const { isSubmitting, errors } = form.formState;


    const handleLoginSubmit = async ( formData: LoginForm ) => {
        try {
            const { data, status } = await submitLogin( formData );
            if ( status === 200 ) {
                toast.success( "Login successful" );
                if ( searchParams.has( "next" ) && isValidRoutePath( searchParams.get( "next" )! ) ) {
                    router.push( searchParams.get( "next" )! );
                } else {
                    router.push( LOGIN_REDIRECT_PATH );
                }
            } else if ( status === 401 || status === 403 || status === 400 || status === 423 ) {
                form.setError( "root", { message: data.message, type: "manual" } );
            } else {
                form.setError( "root", { message: "An unknown error occurred.", type: "manual" } );
                toast.error( "An unknown error occurred" );
            }
        } catch {
            form.setError( "root", { message: "Unable to reach the server. Please try again later.", type: "manual" } );
            toast.error( "Unable to reach the server. Please try again later." );
        }
    }

    useEffect( () => {
        if ( searchParams.has( "referrer" ) && searchParams.get( "referrer" )! === "SESSION_INVALID" ) {
            form.setError( "root", { message: "Your session has expired. Please login again.", type: "manual" } )
        }
    }, [ searchParams, form ] )

    return <Card className="w-full min-w-[600px]">
        <CardHeader className="text-center">
            <CardTitle>Assemble</CardTitle>
            <CardDescription>Log into your account</CardDescription>
        </CardHeader>
        <CardContent>
            <form onSubmit={ form.handleSubmit( handleLoginSubmit ) }>
                <FieldGroup>
                    <Controller
                        name={ "username" }
                        control={ form.control }
                        render={ ( { field, fieldState } ) => (
                            <Field data-invalid={ fieldState.invalid }>
                                <FieldLabel htmlFor={ "username-field" }>Password</FieldLabel>
                                <Input
                                    { ...field }
                                    id={ "username-field" }
                                    aria-invalid={ fieldState.invalid }
                                    placeholder="Type in your username"
                                    type={ "text" }
                                    autoComplete={ "username" }
                                    disabled={ isSubmitting }
                                />
                                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                </FieldError> }
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
                                    placeholder="Type in your password"
                                    type={ "password" }
                                    autoComplete={ "current-password-password" }
                                    disabled={ isSubmitting }
                                />
                                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                </FieldError> }
                            </Field>
                        ) }
                    />
                    { errors.root && (
                        <FieldGroup>
                            <Alert variant="destructive">
                                <AlertCircleIcon/>
                                <AlertTitle>Login failed!</AlertTitle>
                                <AlertDescription>
                                    { errors.root.message }
                                </AlertDescription>
                            </Alert>
                        </FieldGroup>
                    ) }
                    <Button type="submit" variant="default" disabled={ isSubmitting }>
                        { isSubmitting ?
                            <>
                                { "Processing" }
                                <Spinner/>
                            </> : "Login" }
                    </Button>
                </FieldGroup>
            </form>
        </CardContent>
    </Card>
}