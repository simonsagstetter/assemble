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
import { FieldDescription, FieldLegend, FieldSet } from "@/components/ui/field";
import { User } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import ChangePasswordForm from "@/components/account/ChangePasswordForm";
import {
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue
} from "@/components/custom-ui/record-detail/detail";

type AccountDetailProps = {
    userDetails: User;
}
export default function AccountDetail( { userDetails: { firstname, lastname, username, email } }: AccountDetailProps ) {
    return <Card className={ "p-0 border-0 shadow-none rounded-none" }>
        <CardHeader>
            <CardTitle>Account</CardTitle>
            <CardDescription>Manage your account.</CardDescription>
        </CardHeader>
        <CardContent className={ "space-y-10" }>
            <FieldSet>
                <FieldLegend>User Details</FieldLegend>
                <FieldDescription>These details are managed by your organization.</FieldDescription>
                <DetailSection>
                    <DetailRow>
                        <Detail>
                            <DetailLabel>Firstname</DetailLabel>
                            <DetailValue>{ firstname }</DetailValue>
                        </Detail>
                        <Detail>
                            <DetailLabel>Lastname</DetailLabel>
                            <DetailValue>{ lastname }</DetailValue>
                        </Detail>
                    </DetailRow>
                    <DetailRow>
                        <Detail>
                            <DetailLabel>Username</DetailLabel>
                            <DetailValue>{ username }</DetailValue>
                        </Detail>
                        <Detail>
                            <DetailLabel>E-Mail</DetailLabel>
                            <DetailValue>{ email }</DetailValue>
                        </Detail>
                    </DetailRow>
                </DetailSection>
            </FieldSet>
            <ChangePasswordForm/>
        </CardContent>
    </Card>
}
