/*
 * assemble
 * ProjectDetail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { Card, CardAction, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import Link from "next/link";
import { ButtonGroup } from "@/components/ui/button-group";
import { Button } from "@/components/ui/button";
import { ChevronDownIcon, LayersIcon, PencilIcon, PlusIcon } from "lucide-react";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Separator } from "@/components/ui/separator";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import {
    BadgeDetailValue,
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue,
} from "@/components/custom-ui/record-detail/detail";
import { ProjectDTO, ProjectDTOStage } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import ProjectActions from "@/components/manage/projects/ProjectActions";
import {
    useGetAllProjectAssignmentsByProjectId
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import Loading from "@/components/custom-ui/Loading";
import ProjectAssignmentDataTable from "@/components/manage/projects/ProjectAssignmentDataTable";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { colorMobileClasses } from "@/config/calendar/calendar.config";
import Status from "@/components/custom-ui/status";
import { useGetAllTimeEntriesByProjectId } from "@/api/rest/generated/query/timeentries/timeentries";
import TimeEntryDataTable from "@/components/manage/timeentries/TimeEntryDataTable";
import { AuditDetail } from "@/components/custom-ui/record-detail/audit";
import StageProgress from "@/components/custom-ui/progress";

type ProjectStage =
    typeof ProjectDTOStage[keyof typeof ProjectDTOStage];

const PROJECT_STAGE_LIST = Object.values( ProjectDTOStage ) as ProjectStage[];

type ProjectDetailProps = {
    project: ProjectDTO
}

export default function ProjectDetail( { project }: ProjectDetailProps ) {
    const {
        data: projectAssignments,
        isPending,
        isError,
        error
    } = useGetAllProjectAssignmentsByProjectId( project.id );

    const {
        data: timeentries,
        isPending: isTPending,
        isError: isTError,
        error: tError
    } = useGetAllTimeEntriesByProjectId( project.id );

    const timeEntryTotal = timeentries && Array.isArray( timeentries ) ? timeentries
            .reduce( ( total, timeentryy ) => total += timeentryy.total, 0.0 )
        : 0.0;

    const params = useSearchParams();
    const [ activeTab, setActiveTab ] = useState( params.get( "tab" ) ?? "details" );
    const colorKey = project.color.toLowerCase(),
        colorClass = colorMobileClasses[ colorKey as keyof typeof colorMobileClasses ];

    useEffect( () => {
        const update = () => {
            setActiveTab( params.get( "tab" ) ?? "details" );
        }
        update();
    }, [ params ] )

    return <Card className="w-full border-0 shadow-none rounded-none">
        <CardHeader className={ "px-8 py-4" }>
            <div className={ "flex flex-row gap-2 items-center" }>
                <LayersIcon className={ "size-10 bg-primary text-primary-foreground rounded-lg p-2 stroke-1" }/>
                <div className={ "flex flex-col" }>
                    <small className="text-xs uppercase">project</small>
                    <CardTitle className="text-2xl leading-6 flex flex-row items-center gap-2">
                        { project.name }
                        <span className={ `size-3 rounded-full mr-1 ${ colorClass }` }></span>
                    </CardTitle>
                </div>
            </div>
            <CardDescription className={ "leading-6" }>
                <div
                    className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm **:text-stone-800">
                    <div>
                        <span>No.</span>
                        <p>{ project.no }</p>
                    </div>
                    <div>
                        <span>Category</span>
                        <p>
                            { project.category }
                        </p>
                    </div>
                    <div>
                        <span>Status</span>
                        <div className={ "**:text-primary-foreground!" }>
                            { project.active ?
                                <Status label={ "Active" }/>
                                :
                                <Status label={ "Inactive" } variant={ "red" }/>
                            }
                        </div>
                    </div>
                    <div>
                        <span>Time Entry Total</span>
                        <div>
                            { new Intl.NumberFormat( 'de-DE', {
                                style: 'currency',
                                currency: 'EUR'
                            } ).format( timeEntryTotal ) }
                        </div>
                    </div>
                </div>
            </CardDescription>
            <CardAction className="space-x-2">
                <ButtonGroup>
                    <Button variant="outline" className={ "p-0" }>
                        <Link href={ `/app/manage/projects/${ project.id }/edit` }
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
                        <ProjectActions id={ project.id }
                                        align="end"
                                        className="[--radius:1rem]"/>
                    </DropdownMenu>
                </ButtonGroup>
            </CardAction>
        </CardHeader>
        <Separator/>
        <StageProgress<ProjectStage> label={ "project stage" } values={ PROJECT_STAGE_LIST }
                                     value={ project.stage }/>
        <Separator/>
        <CardContent className={ "px-8" }>
            <Tabs defaultValue={ activeTab } onValueChange={ setActiveTab } value={ activeTab }>
                <TabsList>
                    <TabsTrigger value="details">Details</TabsTrigger>
                    <TabsTrigger value="team">Team</TabsTrigger>
                    <TabsTrigger value="timeentries">Time Entries</TabsTrigger>
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
                                            <DetailValue>{ project.no }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Name</DetailLabel>
                                            <DetailValue>{ project.name }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Stage</DetailLabel>
                                            <BadgeDetailValue
                                                variant={ "secondary" }>{ project.stage }</BadgeDetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Status</DetailLabel>
                                            <DetailValue>
                                                { project.active ?
                                                    <Status label={ "Active" }/>
                                                    :
                                                    <Status label={ "Inactive" } variant={ "red" }/>
                                                }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Category</DetailLabel>
                                            <DetailValue>{ project.category }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Type</DetailLabel>
                                            <BadgeDetailValue
                                                variant={ "outline" }>{ project.type }</BadgeDetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Description</DetailLabel>
                                            <DetailValue>
                                                { project.description }
                                            </DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Color</DetailLabel>
                                            <BadgeDetailValue variant={ "outline" }>
                                                <span
                                                    className={ `size-2 rounded-full inline-block mr-1 ${ colorClass }` }></span>
                                                { project.color.toLowerCase() }
                                            </BadgeDetailValue>
                                        </Detail>
                                    </DetailRow>
                                </DetailSection>
                            </AccordionContent>
                        </AccordionItem>
                        <AuditDetail id={ project.id }
                                     createdDate={ project.createdDate }
                                     createdBy={ project.createdBy }
                                     lastModifiedDate={ project.lastModifiedDate }
                                     lastModifiedBy={ project.lastModifiedBy }/>
                    </Accordion>
                </TabsContent>
                <TabsContent value="team">
                    { isPending || !projectAssignments ? <Loading title={ "Loading data" }/> : null }
                    { isError ? error?.message ?? "Error loading assignments" : null }
                    { !isPending && projectAssignments ?
                        <div className={ "relative" }>
                            <Button type={ "button" } className={ "absolute right-0 -top-11 p-0" }>
                                <Link href={ `/app/manage/projects/${ project.id }/assign` }
                                      className={ "px-4 py-3 flex flex-row items-center gap-1" }><PlusIcon
                                    className={ "size-4" }/>Assign</Link>
                            </Button>
                            <ProjectAssignmentDataTable projectAssignments={ projectAssignments }/>
                        </div>
                        : null }
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