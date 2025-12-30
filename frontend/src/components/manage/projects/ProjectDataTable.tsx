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
import { DataTable } from "@/components/custom-ui/DataTable";
import { CheckIcon, MoreHorizontal, XIcon } from "lucide-react";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import EmployeeActions from "@/components/manage/employees/EmployeeActions";
import ProjectActions from "@/components/manage/projects/ProjectActions";

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
                        className={ "hover:underline" }
                    >
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
                accessorKey: "stage"
            },
            {
                header: "Type",
                accessorKey: "type"
            },
            {
                header: "Active",
                cell( { row } ) {
                    const project = row.original;
                    if ( project.active ) {
                        return <CheckIcon className="size-4 text-green-600"/>
                    } else {
                        return <XIcon className={ "size-4 text-red-600" }/>
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
    return <DataTable columns={ columns } data={ projects }/>
}