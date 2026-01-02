/*
 * assemble
 * ProjectAssignmentDataTable.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { ColumnDef } from "@tanstack/react-table";
import { ProjectAssignmentDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useMemo } from "react";
import { DataTable } from "@/components/custom-ui/DataTable";
import { MoreHorizontal, PencilIcon, TrashIcon } from "lucide-react";
import {
    DropdownMenu, DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { useRouter } from "@bprogress/next/app";
import Link from "next/link";
import Status from "@/components/custom-ui/status";

type ProjectAssignmentDataTableProps = {
    projectAssignments: ProjectAssignmentDTO[];
    origin?: "project" | "employee";
}

export default function ProjectAssignmentDataTable(
    {
        projectAssignments,
        origin = "project"
    }: ProjectAssignmentDataTableProps
) {
    const router = useRouter();

    const columns: ColumnDef<ProjectAssignmentDTO>[] = useMemo( () => [
        {
            header: "No",
            cell( { row } ) {
                const assignment = row.original;
                if ( origin === "project" ) {
                    return <Link
                        href={ `/app/manage/employees/${ assignment.employee.id }` }
                        className={ "hover:underline" }
                    >
                        { assignment.employee.no }
                    </Link>
                } else {
                    return <Link
                        href={ `/app/manage/projects/${ assignment.project.id }` }
                        className={ "hover:underline" }
                    >
                        { assignment.project.no }
                    </Link>
                }
            }
        },
        {
            header: "Name",
            cell( { row } ) {
                const assignment = row.original;
                if ( origin === "project" ) {
                    return <Link
                        href={ `/app/manage/employees/${ assignment.employee.id }` }
                        className={ "hover:underline" }
                    >
                        { assignment.employee.fullname }
                    </Link>
                } else {
                    return <Link
                        href={ `/app/manage/projects/${ assignment.project.id }` }
                        className={ "hover:underline" }
                    >
                        { assignment.project.name }
                    </Link>
                }
            }
        },
        {
            header: "Status",
            cell( { row } ) {
                const assignment = row.original;
                if ( assignment.active ) {
                    return <Status label={ "Active" }/>
                } else {
                    return <Status label={ "Inactive" } variant={ "red" }/>
                }
            }
        },
        {
            header: "Rate",
            cell( { row } ) {
                const assignment = row.original;

                return new Intl.NumberFormat( "de-DE", { style: "currency", currency: "EUR" } ).format(
                    assignment?.hourlyRate ?? 0.0
                )
            }
        },
        {
            id: "actions",
            header: "Actions",
            cell: ( { row } ) => {
                const assignment = row.original;
                return (
                    <DropdownMenu dir="ltr">
                        <DropdownMenuTrigger asChild>
                            <Button variant="ghost" className="h-8 w-8 p-0">
                                <span className="sr-only">Open menu</span>
                                <MoreHorizontal className="h-4 w-4"/>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align={ "start" }>
                            <DropdownMenuGroup>
                                <DropdownMenuItem
                                    onSelect={ () => router.push(
                                        `/app/manage/assignments/${ assignment.id }/edit`
                                    ) }>
                                    <PencilIcon/> Edit
                                </DropdownMenuItem>
                            </DropdownMenuGroup>
                            <DropdownMenuSeparator/>
                            <DropdownMenuGroup>
                                <DropdownMenuItem
                                    variant="destructive"
                                    onSelect={
                                        () => router.push(
                                            `/app/manage/assignments/${ assignment.id }/delete?origin=${ origin }`
                                        )
                                    }>
                                    <TrashIcon/>
                                    Delete
                                </DropdownMenuItem>
                            </DropdownMenuGroup>
                        </DropdownMenuContent>
                    </DropdownMenu>
                )
            }
        },
    ], [ origin, router ] );

    return <DataTable columns={ columns } data={ projectAssignments }/>
}