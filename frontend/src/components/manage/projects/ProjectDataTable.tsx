/*
 * assemble
 * ProjectDataTable.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";


import { ProjectDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import Link from "next/link";
import { DataTable, DataTableHeader } from "@/components/custom-ui/DataTable";
import { LayersIcon, MoreHorizontal, PlusIcon } from "lucide-react";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import ProjectActions from "@/components/manage/projects/ProjectActions";
import Status from "@/components/custom-ui/status";
import { Badge } from "@/components/ui/badge";
import { colorMobileClasses } from "@/config/calendar/calendar.config";

type ProjectDataTableProps = {
    projects: ProjectDTO[];
}

export default function ProjectDataTable( { projects }: ProjectDataTableProps ) {
    const columns: ColumnDef<ProjectDTO>[] = useMemo( () => [
            {
                header: "No",
                cell( { row } ) {
                    const project = row.original;
                    return <Link
                        href={ `/app/manage/projects/${ project.id }` }
                        className={ "hover:underline" }
                    >
                        { project.no }
                    </Link>
                }
            },
            {
                header: "Name",
                cell( { row } ) {
                    const project = row.original;
                    return <Link
                        href={ `/app/manage/projects/${ project.id }` }
                        className={ "hover:underline flex flex-row items-center gap-1.5" }
                    >
                        <span
                            className={ `size-2 rounded-full ${ colorMobileClasses[ project.color.toLowerCase() as keyof typeof colorMobileClasses ] }` }></span>
                        { project.name }
                    </Link>
                }
            },
            {
                header: "Category",
                accessorKey: "category"
            },
            {
                header: "Stage",
                cell( { row } ) {
                    return <Badge variant={ "secondary" }>{ row.original.stage }</Badge>
                }
            },
            {
                header: "Type",
                cell( { row } ) {
                    return <Badge variant={ "outline" }>{ row.original.type }</Badge>
                }
            },
            {
                header: "Status",
                cell( { row } ) {
                    const project = row.original;
                    if ( project.active ) {
                        return <Status label={ "Active" }/>
                    } else {
                        return <Status label={ "Inactive" } variant={ "red" }/>
                    }
                }
            },
            {
                id: "actions",
                header: "Actions",
                cell: ( { row } ) => {
                    const project = row.original;
                    return (
                        <DropdownMenu dir="ltr">
                            <DropdownMenuTrigger asChild>
                                <Button variant="ghost" className="h-8 w-8 p-0">
                                    <span className="sr-only">Open menu</span>
                                    <MoreHorizontal className="h-4 w-4"/>
                                </Button>
                            </DropdownMenuTrigger>
                            <ProjectActions id={ project.id } tableActions/>
                        </DropdownMenu>
                    )
                }
            },
        ]
        , [] )
    return <DataTableHeader
        EntityIcon={ LayersIcon }
        entity={ "Projects" }
        currentView={ "All Projects" }
        createActionLink={ "/app/manage/projects/create" }
        createActionLabel={ "New" }
        createActionIcon={ PlusIcon }
    >
        <DataTable columns={ columns } data={ projects }/>
    </DataTableHeader>
}