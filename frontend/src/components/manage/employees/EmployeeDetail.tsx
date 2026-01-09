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

import { EmployeeDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
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
} from "@/components/custom-ui/record-detail/detail";
import { ButtonGroup } from "@/components/ui/button-group";
import {
    DropdownMenu,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import {
    ChevronDownIcon, IdCardIcon,
    PencilIcon, PlusIcon,
} from "lucide-react";
import { Separator } from "@/components/ui/separator";
import EmployeeActions from "@/components/manage/employees/EmployeeActions";
import {
    useGetAllProjectAssignmentsByEmployeeId
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import Loading from "@/components/custom-ui/Loading";
import ProjectAssignmentDataTable from "@/components/manage/projects/ProjectAssignmentDataTable";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import {
    useGetAllTimeEntriesByEmployeeId,
} from "@/api/rest/generated/query/timeentries/timeentries";
import TimeEntryDataTable from "@/components/manage/timeentries/TimeEntryDataTable";
import { AuditDetail } from "@/components/custom-ui/record-detail/audit";

type EmployeeDetailProps = {
    employee: EmployeeDTO
}

export default function EmployeeDetail( { employee }: EmployeeDetailProps ) {
    const {
        data: projectAssignments,
        isPending,
        isError,
        error
    } = useGetAllProjectAssignmentsByEmployeeId( employee.id );
    const {
        data: timeentries,
        isPending: isTPending,
        isError: isTError,
        error: tError
    } = useGetAllTimeEntriesByEmployeeId( employee.id );

    const params = useSearchParams();
    const [ activeTab, setActiveTab ] = useState( params.get( "tab" ) ?? "details" );

    useEffect( () => {
        const update = () => {
            setActiveTab( params.get( "tab" ) ?? "details" );
        }
        update();
    }, [ params ] )

    return <Card className="w-full border-0 shadow-none rounded-none bg-transparent">
        <CardHeader className={ "px-8 py-4" }>
            <div className={ "flex flex-row gap-2 items-center" }>
                <IdCardIcon className={ "size-10 bg-primary text-primary-foreground rounded-lg p-2 stroke-1" }/>
                <div className={ "flex flex-col" }>
                    <small className="text-xs uppercase">employee</small>
                    <CardTitle className="text-2xl leading-6">{ employee.fullname }</CardTitle>
                </div>
            </div>
            <CardDescription className={ "leading-6" }>
                <div
                    className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm **:text-stone-800">
                    <div>
                        <span>No.</span>
                        <p>{ employee.no }</p>
                    </div>
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
            <Tabs defaultValue={ activeTab } value={ activeTab } onValueChange={ setActiveTab }>
                <TabsList>
                    <TabsTrigger value="details">Details</TabsTrigger>
                    <TabsTrigger value="projects">Projects</TabsTrigger>
                    <TabsTrigger value={ "timeentries" }>Time Entries</TabsTrigger>
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
                                        <Detail>
                                            <DetailLabel>No.</DetailLabel>
                                            <DetailValue>{ employee.no }</DetailValue>
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
                        <AuditDetail id={ employee.id }
                                     createdDate={ employee.createdDate }
                                     createdBy={ employee.createdBy }
                                     lastModifiedDate={ employee.lastModifiedDate }
                                     lastModifiedBy={ employee.lastModifiedBy }/>
                    </Accordion>
                </TabsContent>
                <TabsContent value="projects">
                    { isPending || !projectAssignments ? <Loading title={ "Loading Data" }/> : null }
                    { isError ? error?.message ?? "Error loading assignments" : null }
                    { !isPending && projectAssignments ?
                        <div className={ "relative" }>
                            <Button type={ "button" } className={ "absolute right-0 -top-11 p-0" }>
                                <Link href={ `/app/manage/employees/${ employee.id }/assign` }
                                      className={ "px-4 py-3 flex flex-row items-center gap-1" }><PlusIcon
                                    className={ "size-4" }/>Assign</Link>
                            </Button>
                            <ProjectAssignmentDataTable projectAssignments={ projectAssignments }
                                                        origin={ "employee" }/>
                        </div> : null }
                </TabsContent>
                <TabsContent value="timeentries">
                    { isTPending || !timeentries ? <Loading title={ "Loading data" }/> : null }
                    { isTError ? tError?.message ?? "Error loading timeentries" : null }
                    { !isTPending && timeentries ?
                        <TimeEntryDataTable timeentries={ timeentries } isRelatedTable/>
                        : null }
                </TabsContent>
            </Tabs>
        </CardContent>
    </Card>
}
