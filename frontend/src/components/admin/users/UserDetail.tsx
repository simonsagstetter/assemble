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
import {
    BadgeDetailValue,
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue
} from "@/components/custom-ui/record-detail/detail";
import { format } from "date-fns";
import { ButtonGroup } from "@/components/ui/button-group";
import {
    DropdownMenu,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import {
    ChevronDownIcon,
    PencilIcon, UserIcon,
} from "lucide-react";
import { Separator } from "@/components/ui/separator";
import UserActions from "@/components/admin/users/UserActions";
import Status from "@/components/custom-ui/status";

type UserDetailProps = {
    userDetails: UserAdmin
}

export default function UserDetail( { userDetails }: UserDetailProps ) {

    const renderUserRole = ( role: UserRolesItem ) => {
        const hasRole = userDetails.roles.includes( role );
        if ( hasRole ) return <BadgeDetailValue variant={ "default" }>Yes</BadgeDetailValue>
        else return <BadgeDetailValue variant={ "secondary" }>No</BadgeDetailValue>
    }

    return <Card className="w-full border-0 shadow-none rounded-none">
        <CardHeader className={ "px-8 py-4" }>
            <div className={ "flex flex-row gap-2 items-center" }>
                <UserIcon className={ "size-10 bg-primary text-primary-foreground rounded-lg p-2 stroke-1" }/>
                <div className={ "flex flex-col" }>
                    <small className="text-xs uppercase">user</small>
                    <CardTitle className="text-2xl leading-6">{ userDetails.username }</CardTitle>
                </div>
            </div>
            <CardDescription className={ "leading-6" }>
                <div
                    className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm **:text-stone-800">
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
                        <span>Status</span>
                        <p className={ "**:text-primary-foreground!" }>
                            { userDetails.enabled ? <Status key={ "Enabled" } label={ "Enabled" }/> :
                                <Status key={ "Disabled" } label={ "Disabled" } variant={ "red" }/> }
                            { userDetails.locked ? <Status key={ "Locked" } label={ "Locked" } variant={ "red" }
                                                           className={ "mx-[0.1rem]" }/> : null }
                        </p>
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
                                     className="[--radius:1rem]"
                                     isSuperuser={ userDetails.roles.includes( "SUPERUSER" ) }
                        />
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
                                            { userDetails.enabled ?
                                                <BadgeDetailValue>Active</BadgeDetailValue>
                                                :
                                                <BadgeDetailValue variant={ "secondary" }>Inactive</BadgeDetailValue>
                                            }

                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Locked</DetailLabel>
                                            { userDetails.locked ?
                                                <BadgeDetailValue>Locked</BadgeDetailValue>
                                                :
                                                <BadgeDetailValue variant={ "secondary" }>Unlocked</BadgeDetailValue>
                                            }
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
                                            { renderUserRole( UserRolesItem.EXTERNAL ) }
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>User</DetailLabel>
                                            { renderUserRole( UserRolesItem.USER ) }
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Manager</DetailLabel>
                                            { renderUserRole( UserRolesItem.MANAGER ) }
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Admin</DetailLabel>
                                            { renderUserRole( UserRolesItem.ADMIN ) }
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Superuser</DetailLabel>
                                            { renderUserRole( UserRolesItem.SUPERUSER ) }
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
