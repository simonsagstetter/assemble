/*
 * assemble
 * UserDetail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { UserAdmin, UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Card, CardAction, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import SessionDetail from "@/components/admin/session/SessionDetail";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Detail, DetailLabel, DetailRow, DetailSection, DetailValue } from "@/components/custom-ui/detail";
import { format } from "date-fns";
import { ButtonGroup } from "@/components/ui/button-group";
import {
    DropdownMenu,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import {
    ChevronDownIcon,
    PencilIcon,
} from "lucide-react";
import { Separator } from "@/components/ui/separator";
import UserActions from "@/components/admin/users/UserActions";

type UserDetailProps = {
    userDetails: UserAdmin
}

export default function UserDetail( { userDetails }: UserDetailProps ) {
    return <Card className="w-full border-0 shadow-none rounded-none">
        <CardHeader className={ "px-8 py-4" }>
            <small className="text-xs uppercase leading-0 pt-1">user</small>
            <CardTitle className="text-2xl leading-6">{ userDetails.username }</CardTitle>
            <CardDescription className={ "leading-6" }>
                <div className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm">
                    <div>
                        <span>Employee</span>
                        <p>
                            { userDetails.employee != null ? <Link
                                    href={ `/app/manage/employees/${ userDetails.employee.id }` }
                                    className={ "hover:underline" }
                                >{ userDetails.employee.fullname }</Link>
                                : "-" }
                        </p>
                    </div>
                    <div>
                        <span>Email</span>
                        <p>{ userDetails.email }</p>
                    </div>
                    <div>
                        <span>Enabled</span>
                        <p>{ userDetails.enabled ? "Yes" : "No" }</p>
                    </div>
                    <div>
                        <span>Locked</span>
                        <p>{ userDetails.locked ? "Yes" : "No" }</p>
                    </div>
                </div>
            </CardDescription>
            <CardAction className="space-x-2">
                <ButtonGroup>
                    <Button variant="outline" className={ "p-0" }>
                        <Link href={ `/app/admin/users/${ userDetails.id }/edit` }
                              className={ "px-4 py-3 flex flex-row items-center gap-2" }>
                            <PencilIcon className={ "size-3" }/> Edit
                        </Link>
                    </Button>
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="outline" className="!pl-2">
                                <ChevronDownIcon/>
                            </Button>
                        </DropdownMenuTrigger>
                        <UserActions id={ userDetails.id }
                                     hasEmployee={ userDetails.employee != null }
                                     align="end"
                                     className="[--radius:1rem]"/>
                    </DropdownMenu>
                </ButtonGroup>
            </CardAction>
        </CardHeader>
        <Separator></Separator>
        <CardContent className={ "px-8" }>
            <Tabs defaultValue="details">
                <TabsList>
                    <TabsTrigger value="details">Details</TabsTrigger>
                    <TabsTrigger value="session">Session</TabsTrigger>
                </TabsList>
                <TabsContent value="details" className={ "py-2" }>
                    <Accordion type={ "single" } defaultValue={ "details" }>
                        <AccordionItem value={ "details" }>
                            <AccordionTrigger>Details</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Firstname</DetailLabel>
                                            <DetailValue>{ userDetails.firstname }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Lastname</DetailLabel>
                                            <DetailValue>{ userDetails.lastname }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Email</DetailLabel>
                                            <DetailValue>{ userDetails.email }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Employee</DetailLabel>
                                            <DetailValue>{ userDetails.employee != null ?
                                                <Link
                                                    href={ `/app/manage/employees/${ userDetails.employee.id }` }
                                                    className={ "hover:underline text-blue-800" }
                                                >
                                                    { userDetails.employee.fullname }
                                                </Link>
                                                : "-" }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Enabled</DetailLabel>
                                            <DetailValue>{ userDetails.enabled ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Locked</DetailLabel>
                                            <DetailValue>{ userDetails.locked ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AccordionItem value={ "roles" }>
                            <AccordionTrigger>Roles</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>External</DetailLabel>
                                            <DetailValue>{ userDetails.roles.includes( UserRolesItem.EXTERNAL ) ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>User</DetailLabel>
                                            <DetailValue>{ userDetails.roles.includes( UserRolesItem.USER ) ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Manager</DetailLabel>
                                            <DetailValue>{ userDetails.roles.includes( UserRolesItem.MANAGER ) ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Admin</DetailLabel>
                                            <DetailValue>{ userDetails.roles.includes( UserRolesItem.ADMIN ) ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Superuser</DetailLabel>
                                            <DetailValue>{ userDetails.roles.includes( UserRolesItem.SUPERUSER ) ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AccordionItem value={ "system" }>
                            <AccordionTrigger>Audit</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Id</DetailLabel>
                                            <DetailValue>{ userDetails.id }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Created Date</DetailLabel>
                                            <DetailValue>{ format( userDetails.createdDate, "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Created By</DetailLabel>
                                            <DetailValue>
                                                { userDetails.createdBy.id != null ?
                                                    <Link
                                                        href={ `/app/admin/users/${ userDetails.createdBy.id }` }
                                                        className={ "hover:underline text-blue-800" }
                                                    >
                                                        { userDetails.createdBy.username }
                                                    </Link>
                                                    :
                                                    <span className={ "text-blue-800" }>
                                                        { userDetails.createdBy.username }
                                                    </span>
                                                }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Last Modified Date</DetailLabel>
                                            <DetailValue>{ format( userDetails.lastModifiedDate, "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Last Modified By</DetailLabel>
                                            <DetailValue>
                                                { userDetails.lastModifiedBy.id != null ?
                                                    <Link
                                                        href={ `/app/admin/users/${ userDetails.lastModifiedBy.id }` }
                                                        className={ "hover:underline text-blue-800" }
                                                    >
                                                        { userDetails.lastModifiedBy.username }
                                                    </Link>
                                                    :
                                                    <span className={ "text-blue-800" }>
                                                        { userDetails.lastModifiedBy.username }
                                                    </span>
                                                }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                    </Accordion>
                </TabsContent>
                <TabsContent value="session">
                    <SessionDetail username={ userDetails.username }/>
                </TabsContent>
            </Tabs>
        </CardContent>
    </Card>
}
