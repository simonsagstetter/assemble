/*
 * assemble
 * TimeEntryDetail.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Card, CardAction, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { CalendarIcon, ChevronDownIcon, PencilIcon } from "lucide-react";
import { ButtonGroup } from "@/components/ui/button-group";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Separator } from "@/components/ui/separator";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import {
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue
} from "@/components/custom-ui/record-detail/detail";
import { format } from "date-fns";
import { isoDurationToMs, msToHHmm } from "@/utils/duration";
import TimeEntryActions from "@/components/manage/timeentries/TimeEntryActions";
import { AuditDetail } from "@/components/custom-ui/record-detail/audit";
import ProjectCompact from "@/components/manage/projects/ProjectCompact";
import EmployeeCompact from "@/components/manage/employees/EmployeeCompact";

type TimeEntryDetailProps = {
    timeEntry: TimeEntryDTO
}

export default function TimeEntryDetail( { timeEntry }: TimeEntryDetailProps ) {
    const date = format( timeEntry.date, "dd.MM.yyyy" ),
        duration = isoDurationToMs( timeEntry.totalTime ),
        pause = isoDurationToMs( timeEntry.pauseTime ),
        totalDuration = msToHHmm( duration - pause ),
        total = new Intl.NumberFormat( 'de-DE', { style: "currency", currency: "EUR" } )
            .format( timeEntry.total );

    const projectLink = <ProjectCompact project={ timeEntry.project }/>

    const employeeLink = <EmployeeCompact employee={ timeEntry.employee }/>

    return <Card className="w-full border-0 shadow-none rounded-none">
        <CardHeader className={ "px-8 py-4" }>
            <div className={ "flex flex-row gap-2 items-center" }>
                <CalendarIcon className={ "size-10 bg-primary text-primary-foreground rounded-lg p-2 stroke-1" }/>
                <div className={ "flex flex-col" }>
                    <small className="text-xs uppercase">time entry</small>
                    <CardTitle className="text-2xl leading-6 flex flex-row items-center gap-2">
                        { timeEntry.no }
                    </CardTitle>
                </div>
            </div>
            <CardDescription className={ "leading-6" }>
                <div
                    className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm **:text-stone-800">
                    <div>
                        <span>Project</span>
                        <p>
                            { projectLink }
                        </p>
                    </div>
                    <div>
                        <span>Employee</span>
                        <p>
                            { employeeLink }
                        </p>
                    </div>
                    <div>
                        <span>Date</span>
                        <div>{ date }</div>
                    </div>
                    <div>
                        <span>Total Duration</span>
                        <div>{ totalDuration }</div>
                    </div>
                    <div>
                        <span>Total Amount</span>
                        <div>
                            { total }
                        </div>
                    </div>
                </div>
            </CardDescription>
            <CardAction className="space-x-2">
                <ButtonGroup>
                    <Button variant="outline" className={ "p-0" }>
                        <Link href={ `/app/manage/timeentries/${ timeEntry.id }/edit` }
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
                        <TimeEntryActions id={ timeEntry.id } tableActions={ false }
                                          align="end"
                                          className="[--radius:1rem]"/>
                    </DropdownMenu>
                </ButtonGroup>
            </CardAction>
        </CardHeader>
        <Separator/>
        <CardContent className={ "px-8" }>
            <Tabs defaultValue={ "details" }>
                <TabsList>
                    <TabsTrigger value="details">Details</TabsTrigger>
                </TabsList>
                <TabsContent value="details" className={ "py-2" }>
                    <Accordion type={ "single" } defaultValue={ "details" }>
                        <AccordionItem value={ "details" }>
                            <AccordionTrigger>Details</AccordionTrigger>
                            <AccordionContent className={ "pb-10" }>
                                <DetailSection>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>No</DetailLabel>
                                            <DetailValue>{ timeEntry.no }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Date</DetailLabel>
                                            <DetailValue>{ date }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Project</DetailLabel>
                                            <DetailValue>
                                                { projectLink }
                                            </DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Employee</DetailLabel>
                                            <DetailValue>
                                                { employeeLink }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Duration</DetailLabel>
                                            <DetailValue>{ msToHHmm( duration ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Pause</DetailLabel>
                                            <DetailValue>{ msToHHmm( pause ) }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Total Duration</DetailLabel>
                                            <DetailValue>{ totalDuration }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Hourly Rate</DetailLabel>
                                            <DetailValue>{ new Intl.NumberFormat( "de-DE", {
                                                style: "currency",
                                                currency: "EUR"
                                            } ).format( timeEntry.rate ) }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Total (External)</DetailLabel>
                                            <DetailValue>{ new Intl.NumberFormat( "de-DE", {
                                                style: "currency",
                                                currency: "EUR"
                                            } ).format( timeEntry.total ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Total (Internal)</DetailLabel>
                                            <DetailValue>{ new Intl.NumberFormat( "de-DE", {
                                                style: "currency",
                                                currency: "EUR"
                                            } ).format( timeEntry.totalInternal ) }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Description</DetailLabel>
                                            <DetailValue>{ timeEntry.description }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AuditDetail id={ timeEntry.id }
                                     createdDate={ timeEntry.createdDate }
                                     createdBy={ timeEntry.createdBy }
                                     lastModifiedDate={ timeEntry.lastModifiedDate }
                                     lastModifiedBy={ timeEntry.lastModifiedBy }/>
                    </Accordion>
                </TabsContent>
            </Tabs>
        </CardContent>
    </Card>
}