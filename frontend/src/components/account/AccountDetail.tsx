/*
 * assemble
 * AccountDetail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Field, FieldDescription, FieldGroup, FieldLabel, FieldLegend, FieldSet } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { User } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import ChangePasswordForm from "@/components/account/ChangePasswordForm";
import { Separator } from "@/components/ui/separator";

type AccountDetailProps = {
    userDetails: User;
}
export default function AccountDetail( { userDetails: { firstname, lastname, username, email } }: AccountDetailProps ) {
    return <Card>
        <CardHeader>
            <CardTitle>Account</CardTitle>
            <CardDescription>Manage your account.</CardDescription>
        </CardHeader>
        <CardContent className={ "space-y-10" }>
            <FieldSet>
                <FieldLegend>User Details</FieldLegend>
                <FieldDescription>These details are managed by your organization.</FieldDescription>
                <FieldGroup className={ "flex flex-row " }>
                    <Field>
                        <FieldLabel>First name</FieldLabel>
                        <Input type={ "text" } defaultValue={ firstname } readOnly disabled/>
                    </Field>
                    <Field>
                        <FieldLabel>Last name</FieldLabel>
                        <Input type={ "text" } defaultValue={ lastname } readOnly disabled/>
                    </Field>
                </FieldGroup>
                <FieldGroup className={ "flex flex-row" }>
                    <Field>
                        <FieldLabel>Username</FieldLabel>
                        <Input type={ "text" } defaultValue={ username } readOnly disabled/>
                    </Field>
                    <Field>
                        <FieldLabel>Email</FieldLabel>
                        <Input type={ "email" } defaultValue={ email } readOnly disabled/>
                    </Field>
                </FieldGroup>
            </FieldSet>
            <Separator/>
            <ChangePasswordForm/>
        </CardContent>
    </Card>
}
