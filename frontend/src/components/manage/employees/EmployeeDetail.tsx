/*
 * assemble
 * EmployeeDetail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { Employee } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Card, CardAction, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import {
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue, SensitiveDetailValue,
} from "@/components/custom-ui/detail";
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
import EmployeeActions from "@/components/manage/employees/EmployeeActions";

type EmployeeDetailProps = {
    employee: Employee
}

export default function EmployeeDetail( { employee }: EmployeeDetailProps ) {
    return <Card className="w-full border-0 shadow-none rounded-none">
        <CardHeader className={ "px-8 py-4" }>
            <small className="text-xs uppercase leading-0 pt-1">employee</small>
            <CardTitle className="text-2xl leading-6">{ employee.fullname }</CardTitle>
            <CardDescription className={ "leading-6" }>
                <div className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm">
                    <div>
                        <span>User</span>
                        <p>
                            { employee.user != null ? <Link
                                    href={ `/app/admin/users/${ employee.user.id }` }
                                    className={ "hover:underline" }
                                >{ employee.user.username }</Link>
                                : "-" }
                        </p>
                    </div>
                    <div>
                        <span>Email</span>
                        <p>{ employee.email }</p>
                    </div>
                </div>
            </CardDescription>
            <CardAction className="space-x-2">
                <ButtonGroup>
                    <Button variant="outline" className={ "p-0" }>
                        <Link href={ `/app/manage/employees/${ employee.id }/edit` }
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
                        <EmployeeActions id={ employee.id }
                                         hasUser={ employee.user != null }
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
                    <TabsTrigger value="related">Related</TabsTrigger>
                </TabsList>
                <TabsContent value="details" className={ "py-2" }>
                    <Accordion type={ "single" } defaultValue={ "identity" }>
                        <AccordionItem value={ "identity" }>
                            <AccordionTrigger>Identity</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Firstname</DetailLabel>
                                            <DetailValue>{ employee.firstname }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Lastname</DetailLabel>
                                            <DetailValue>{ employee.lastname }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Date of Birth</DetailLabel>
                                            <DetailValue>{ employee.dateOfBirth }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Place of Birth</DetailLabel>
                                            <SensitiveDetailValue>{ employee.placeOfBirth }</SensitiveDetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Marital Status</DetailLabel>
                                            <SensitiveDetailValue>{ employee.maritalStatus }</SensitiveDetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Citizenship</DetailLabel>
                                            <SensitiveDetailValue>{ employee.citizenship }</SensitiveDetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>User</DetailLabel>
                                            <DetailValue>{ employee.user != null ?
                                                <Link
                                                    href={ `/app/admin/users/${ employee.user.id }` }
                                                    className={ "hover:underline text-blue-800" }
                                                >
                                                    { employee.user.username }
                                                </Link>
                                                : "-" }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AccordionItem value={ "contact" }>
                            <AccordionTrigger>Contact</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Email</DetailLabel>
                                            <DetailValue>{ employee.email }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Phone</DetailLabel>
                                            <SensitiveDetailValue>{ employee.phone }</SensitiveDetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AccordionItem value={ "address" }>
                            <AccordionTrigger>Address</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Street</DetailLabel>
                                            <SensitiveDetailValue>{ employee.address?.street }</SensitiveDetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Number</DetailLabel>
                                            <SensitiveDetailValue>{ employee.address?.number }</SensitiveDetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Postal Code</DetailLabel>
                                            <DetailValue>{ employee.address?.postalCode }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>City</DetailLabel>
                                            <DetailValue>{ employee.address?.city }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>State</DetailLabel>
                                            <DetailValue>{ employee.address?.state }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Country</DetailLabel>
                                            <DetailValue>{ employee.address?.country }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AccordionItem value={ "finance" }>
                            <AccordionTrigger>Finance</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Holder</DetailLabel>
                                            <DetailValue>{ employee.bankAccount?.holderName }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Institution</DetailLabel>
                                            <DetailValue>{ employee.bankAccount?.institutionName }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>IBAN</DetailLabel>
                                            <SensitiveDetailValue>{ employee.bankAccount?.iban }</SensitiveDetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>BIC</DetailLabel>
                                            <SensitiveDetailValue>{ employee.bankAccount?.bic }</SensitiveDetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Tax ID</DetailLabel>
                                            <SensitiveDetailValue>{ employee.taxIdentificationNumber }</SensitiveDetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AccordionItem value={ "insurance" }>
                            <AccordionTrigger>Insurance</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Health Insurance</DetailLabel>
                                            <DetailValue>{ employee.healthInsurance }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Social Security Number</DetailLabel>
                                            <SensitiveDetailValue>{ employee.nationalInsuranceNumber }</SensitiveDetailValue>
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
                                            <DetailValue>{ employee.id }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Created Date</DetailLabel>
                                            <DetailValue>{ format( employee.createdDate, "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Created By</DetailLabel>
                                            <DetailValue>
                                                { employee.createdBy.id != null ?
                                                    <Link
                                                        href={ `/app/admin/users/${ employee.createdBy.id }` }
                                                        className={ "hover:underline text-blue-800" }
                                                    >
                                                        { employee.createdBy.username }
                                                    </Link>
                                                    :
                                                    <span className={ "text-blue-800" }>
                                                        { employee.createdBy.username }
                                                    </span>
                                                }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Last Modified Date</DetailLabel>
                                            <DetailValue>{ format( employee.lastModifiedDate, "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Last Modified By</DetailLabel>
                                            <DetailValue>
                                                { employee.lastModifiedBy.id != null ?
                                                    <Link
                                                        href={ `/app/admin/users/${ employee.lastModifiedBy.id }` }
                                                        className={ "hover:underline text-blue-800" }
                                                    >
                                                        { employee.lastModifiedBy.username }
                                                    </Link>
                                                    :
                                                    <span className={ "text-blue-800" }>
                                                        { employee.lastModifiedBy.username }
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
                <TabsContent value="related">
                    <>Not Implemented Yet</>
                </TabsContent>
            </Tabs>
        </CardContent>
    </Card>
}
