/*
 * assemble
 * TimeEntryDataTable.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import Link from "next/link";
import { colorMobileClasses } from "@/config/calendar/calendar.config";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { CalendarIcon, MoreHorizontal, PlusIcon } from "lucide-react";
import { DataTable, DataTableHeader } from "@/components/custom-ui/DataTable";
import { isoDurationToMs, msToHHmm } from "@/utils/duration";
import TimeEntryActions from "@/components/manage/timeentries/TimeEntryActions";

type TimeEntryDataTableProps = {
    timeentries: TimeEntryDTO[],
    isRelatedTable?: boolean
}

export default function TimeEntryDataTable( { timeentries, isRelatedTable = false }: TimeEntryDataTableProps ) {
    const columns: ColumnDef<TimeEntryDTO>[] = useMemo( () => [
            {
                header: "No",
                cell( { row } ) {
                    const timeentry = row.original;
                    return <Link
                        href={ `/app/manage/timeentries/${ timeentry.id }` }
                        className={ "hover:underline" }
                    >
                        { timeentry.no }
                    </Link>
                },
            },
            {
                header: "Project",
                cell( { row } ) {
                    const project = row.original.project;
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
                header: "Employee",
                cell( { row } ) {
                    const employee = row.original.employee;
                    return <Link
                        href={ `/app/manage/employees/${ employee.id }` }
                        className={ "hover:underline flex flex-row items-center gap-1.5" }
                    >
                        { employee.fullname }
                    </Link>
                }
            },
            {
                id: "duration",
                header: () => <div className={ "text-right" }>Duration</div>,
                cell( { row } ) {
                    return <div className={ "text-right" }>
                        { msToHHmm( isoDurationToMs( row.original.totalTime ) ) }
                    </div>
                }
            },
            {
                id: "pauseTime",
                header: () => <div className={ "text-right" }>Pause</div>,
                cell( { row } ) {
                    return <div className={ "text-right" }>
                        { msToHHmm( isoDurationToMs( row.original.pauseTime ) ) }
                    </div>
                }
            },
            {
                id: "todalDuration",
                header: () => <div className={ "text-right" }>Total Duration</div>,
                cell( { row } ) {
                    const timeentry = row.original;

                    return <div className={ "text-right" }>
                        { msToHHmm( isoDurationToMs( timeentry.totalTime ) - isoDurationToMs( timeentry.pauseTime ) ) }
                    </div>
                }
            },
            {
                id: "hourlyRate",
                header: () => <div className={ "text-right" }>Hourly Rate</div>,
                cell( { row } ) {
                    return <div className={ "text-right" }>
                        { new Intl.NumberFormat( "de-DE", { style: "currency", currency: "EUR" } ).format(
                            row.original.rate ?? 0.0
                        ) }
                    </div>
                }
            },
            {
                id: "totalExternal",
                header: () => <div className={ "text-right" }>Total (External)</div>,
                cell( { row } ) {
                    return <div className={ "text-right" }>
                        { new Intl.NumberFormat( "de-DE", { style: "currency", currency: "EUR" } ).format(
                            row.original.total ?? 0.0
                        ) }
                    </div>
                }
            },
            {
                id: "totalInternal",
                header: () => <div className={ "text-right" }>Total (Internal)</div>,
                cell( { row } ) {
                    return <div className={ "text-right" }>
                        { new Intl.NumberFormat( "de-DE", { style: "currency", currency: "EUR" } ).format(
                            row.original.totalInternal ?? 0.0
                        ) }
                    </div>
                }
            },
            {
                id: "actions",
                header: "Actions",
                cell: ( { row } ) => {
                    const timeEntry = row.original;
                    return (
                        <DropdownMenu dir="ltr">
                            <DropdownMenuTrigger asChild>
                                <Button variant="ghost" className="h-8 w-8 p-0">
                                    <span className="sr-only">Open menu</span>
                                    <MoreHorizontal className="h-4 w-4"/>
                                </Button>
                            </DropdownMenuTrigger>
                            <TimeEntryActions id={ timeEntry.id } tableActions/>
                        </DropdownMenu>
                    )
                }
            },
        ]
        , [] );

    const data = useMemo( () => timeentries
            .sort( ( a, b ) => a.no.localeCompare( b.no ) ),
        [ timeentries ]
    );

    if ( isRelatedTable ) {
        return <DataTable columns={ columns.slice( 0, -1 ) } data={ data }/>
    }

    return <DataTableHeader
        EntityIcon={ CalendarIcon }
        entity={ "Time Entry" }
        currentView={ "All Time Entries" }
        createActionLink={ "/app/manage/timeentries/create" }
        createActionLabel={ "New" }
        createActionIcon={ PlusIcon }
    >
        <DataTable columns={ columns } data={ data }/>
    </DataTableHeader>
}
