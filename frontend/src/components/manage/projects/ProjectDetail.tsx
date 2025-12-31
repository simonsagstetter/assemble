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
import { ChevronDownIcon, PencilIcon, PlusIcon } from "lucide-react";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Separator } from "@/components/ui/separator";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import {
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue,
} from "@/components/custom-ui/detail";
import { format } from "date-fns";
import { ProjectDTO, ProjectDTOStage } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import ProjectActions from "@/components/manage/projects/ProjectActions";
import StageProgress from "@/components/custom-ui/StageProgress";
import {
    useGetAllProjectAssignmentsByProjectId
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import Loading from "@/components/custom-ui/Loading";
import ProjectAssignmentDataTable from "@/components/manage/projects/ProjectAssignmentDataTable";
import { useSearchParams } from "next/navigation";

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
    const params = useSearchParams();
    const activeTab = params.get( "tab" ) ?? "details";

    return <Card className="w-full border-0 shadow-none rounded-none">
        <CardHeader className={ "px-8 py-4" }>
            <small className="text-xs uppercase leading-0 pt-1">project</small>
            <CardTitle className="text-2xl leading-6">{ project.name }</CardTitle>
            <CardDescription className={ "leading-6" }>
                <div className="flex flex-row gap-10 **:[&_span]:text-xs **:[&_p]:font-semibold **:[&_p]:text-sm">
                    <div>
                        <span>No.</span>
                        <p>{ project.no }</p>
                    </div>
                    <div>
                        <span>Active</span>
                        <p>{ project.active ? "Yes" : "No" }</p>
                    </div>
                    <div>
                        <span>Category</span>
                        <p>
                            { project.category }
                        </p>
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
            <Tabs defaultValue={ activeTab }>
                <TabsList>
                    <TabsTrigger value="details">Details</TabsTrigger>
                    <TabsTrigger value="team">Team</TabsTrigger>
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
                                            <DetailValue>{ project.stage }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Active</DetailLabel>
                                            <DetailValue>{ project.active ? "Yes" : "No" }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Category</DetailLabel>
                                            <DetailValue>{ project.category }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Type</DetailLabel>
                                            <DetailValue>{ project.type }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Description</DetailLabel>
                                            <DetailValue>
                                                { project.description }
                                            </DetailValue>
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
                                            <DetailValue>{ project.id }</DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Created Date</DetailLabel>
                                            <DetailValue>{ format( project.createdDate, "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Created By</DetailLabel>
                                            <DetailValue>
                                                { project.createdBy.id != null ?
                                                    <Link
                                                        href={ `/app/admin/users/${ project.createdBy.id }` }
                                                        className={ "hover:underline text-blue-800" }
                                                    >
                                                        { project.createdBy.username }
                                                    </Link>
                                                    :
                                                    <span className={ "text-blue-800" }>
                                                        { project.createdBy.username }
                                                    </span>
                                                }
                                            </DetailValue>
                                        </Detail>
                                    </DetailRow>
                                    <DetailRow>
                                        <Detail>
                                            <DetailLabel>Last Modified Date</DetailLabel>
                                            <DetailValue>{ format( project.lastModifiedDate, "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                                        </Detail>
                                        <Detail>
                                            <DetailLabel>Last Modified By</DetailLabel>
                                            <DetailValue>
                                                { project.lastModifiedBy.id != null ?
                                                    <Link
                                                        href={ `/app/admin/users/${ project.lastModifiedBy.id }` }
                                                        className={ "hover:underline text-blue-800" }
                                                    >
                                                        { project.lastModifiedBy.username }
                                                    </Link>
                                                    :
                                                    <span className={ "text-blue-800" }>
                                                        { project.lastModifiedBy.username }
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
                <TabsContent value="team">
                    { isPending || !projectAssignments ? <Loading/> : null }
                    { isError ? error?.message ?? "Error loading assignments" : null }
                    { !isPending && projectAssignments ?
                        <div className={ "relative" }>
                            <Button type={ "button" } className={ "absolute right-0 -top-11" }>
                                <Link href={ `/app/manage/projects/${ project.id }/assign` }
                                      className={ "px-4 py-3 flex flex-row items-center gap-1" }><PlusIcon
                                    className={ "size-4" }/>Assign</Link>
                            </Button>
                            <ProjectAssignmentDataTable projectAssignments={ projectAssignments }/>
                        </div>
                        : null }
                </TabsContent>
            </Tabs>
        </CardContent>
    </Card>
}