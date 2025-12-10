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

import { useForm } from "react-hook-form";
import { LoginForm, LoginFormSchema } from "@/types/auth/auth.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { submitLogin } from "@/services/rest/auth/auth";
import { toast } from "sonner";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { FieldGroup } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon } from "lucide-react";
import { Spinner } from "@/components/ui/spinner";
import { useRouter, useSearchParams } from "next/navigation";
import { LOGIN_REDIRECT_PATH } from "@/config/auth/auth.config";
import { useEffect } from "react";
import { useProgress } from "@bprogress/next";
import { isValidRoutePath } from "@/utils/url";

export default function Login() {
    const router = useRouter();
    const progress = useProgress();
    const searchParams = useSearchParams();
    const form = useForm( {
        resolver: zodResolver( LoginFormSchema ),
        defaultValues: { username: "", password: "" }
    } )

    const { isSubmitting, errors } = form.formState;


    const handleLoginSubmit = async ( formData: LoginForm ) => {
        progress.start();
        try {
            const { data, status } = await submitLogin( formData );
            if ( status === 200 ) {
                toast.success( "Login successful" );
                if ( searchParams.has( "next" ) && isValidRoutePath( searchParams.get( "next" )! ) ) {
                    router.push( searchParams.get( "next" )! );
                } else {
                    router.push( LOGIN_REDIRECT_PATH );
                }
            } else if ( status === 401 ) {
                form.setError( "root", { message: data.message, type: "manual" } );
            } else {
                form.setError( "root", { message: "An unknown error occurred.", type: "manual" } );
                toast.error( "An unknown error occurred" );
            }
        } catch {
            form.setError( "root", { message: "Unable to reach the server. Please try again later.", type: "manual" } );
            toast.error( "Unable to reach the server. Please try again later." );
        } finally {
            progress.stop();
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
            <Form { ...form }>
                <form onSubmit={ form.handleSubmit( handleLoginSubmit ) }>
                    <FieldGroup>
                        <FormField
                            control={ form.control }
                            name="username"
                            render={ ( { field } ) => (
                                <FormItem>
                                    <FormLabel>Username</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Type in your username" { ...field }
                                               autoComplete={ "username" }
                                               disabled={ isSubmitting } autoFocus/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            ) }
                        />
                        <FormField
                            control={ form.control }
                            name="password"
                            render={ ( { field } ) => (
                                <FormItem>
                                    <FormLabel>Password</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Type in your password"
                                               { ...field }
                                               type={ "password" }
                                               autoComplete={ "current-password" }
                                               disabled={ isSubmitting }/>
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
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
            </Form>
        </CardContent>
    </Card>
}