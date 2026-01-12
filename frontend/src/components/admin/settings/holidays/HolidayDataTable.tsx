/*
 * assemble
 * HolidayDataTable.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { HolidayDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import { DataTable, DataTableHeader } from "@/components/custom-ui/DataTable";
import { CalendarIcon, ChevronDownIcon, PlusIcon, TrashIcon, TreePalm } from "lucide-react";
import { format, isAfter } from "date-fns";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import {
    getGetImportedYearsQueryKey,
    useDeleteHolidaysFromYear
} from "@/api/rest/generated/query/holiday-import/holiday-import";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup, DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { invalidateQueries } from "@/utils/query";
import { useQueryClient } from "@tanstack/react-query";
import { getGetHolidaysByYearAndSubdivisionCodeQueryKey } from "@/api/rest/generated/query/holidays/holidays";
import { SubdivisionLabels } from "@/types/settings/settings.types";
import { toast } from "sonner";
import { useRouter } from "@bprogress/next/app";

type HolidayDataTableProps = {
    holidays: HolidayDTO[],
    year: string;
}

export default function HolidayDataTable( { holidays, year }: HolidayDataTableProps ) {
    const queryClient = useQueryClient();
    const router = useRouter();
    const { mutate, isPending } = useDeleteHolidaysFromYear();
    const columns: ColumnDef<HolidayDTO>[] = useMemo( () => [
        {
            accessorKey: "name",
            header: "Name"
        },
        {
            header: "Date",
            cell( { row } ) {
                const holiday = row.original;
                return format( new Date( holiday.startDate! ), "dd.MM.yyyy" )
            }
        },
        {
            header: "Temporal Scope",
            cell( { row } ) {
                const holiday = row.original;

                return <Badge variant={ "secondary" }>{ holiday.temporalScope }</Badge>
            }
        },
        {
            header: "Division Scope",
            cell( { row } ) {
                const holiday = row.original;
                if ( holiday.nationWide ) {
                    return <Badge variant={ "secondary" }>Nation Wide</Badge>
                } else {
                    return holiday.subdivisions.map( sub => (
                        <Badge className={ "mr-1" } variant={ "default" } key={ sub.code }>{ sub.name }</Badge>
                    ) )
                }
            }
        }
    ], [] );

    const sortedHolidays = useMemo( () => holidays
            .sort( ( a, b ) => isAfter( a.startDate!, b.startDate! ) ? 1 : -1 )
        , [ holidays ]
    );


    const handleDeleteHolidays = async ( year: string ) => {
        mutate(
            {
                year
            },
            {
                onSuccess: async () => {
                    await invalidateQueries( queryClient, [
                        ...Object.keys( SubdivisionLabels ).map( subdivisionCode =>
                            getGetHolidaysByYearAndSubdivisionCodeQueryKey( {
                                subdivisionCode: "DE-" + subdivisionCode,
                                year
                            } ) as readonly string[]
                        ),
                        getGetImportedYearsQueryKey()
                    ] );

                    toast.success( "Success",
                        {
                            description: "Holidays were deleted",
                        }
                    );

                    router.refresh();
                },
                onError: () => {
                    toast.error( "Error", {
                        description: "Could not delete holidays.",
                    } )
                }
            }
        );
    }

    return <DataTableHeader
        EntityIcon={ TreePalm }
        entity={ "Holiday" }
        currentView={ "All Holidays for " + year }
        createActionLink={ "/app/admin/settings/holidays/import" }
        createActionLabel={ "Import" }
        createActionIcon={ PlusIcon }
        additonalAction={
            <Button variant={ "default" } asChild>
                <Link href={ "/app/admin/settings/holidays/choose" }>
                    <CalendarIcon/> Choose Year
                </Link>
            </Button>
        }
        dropdownAction={
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <Button variant="default" className="!pl-2">
                        <ChevronDownIcon/>
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end"
                                     className="[--radius:1rem]">
                    <DropdownMenuGroup>
                        <DropdownMenuItem variant={ "destructive" } onSelect={ () => handleDeleteHolidays( year ) }
                                          disabled={ isPending }>
                            <TrashIcon/> Delete Selection

                        </DropdownMenuItem>
                    </DropdownMenuGroup>
                </DropdownMenuContent>
            </DropdownMenu>
        }
    >
        <DataTable columns={ columns } data={ sortedHolidays }/>
    </DataTableHeader>
}