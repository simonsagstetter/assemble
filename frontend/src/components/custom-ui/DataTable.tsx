/*
 * assemble
 * DataTable.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client"

import { ColumnDef, flexRender, getCoreRowModel, useReactTable, } from "@tanstack/react-table"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow, } from "@/components/ui/table"
import { LucideProps, PlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { ForwardRefExoticComponent, ReactNode, RefAttributes } from "react";
import { ButtonGroup, ButtonGroupSeparator } from "@/components/ui/button-group";

interface DataTableProps<TData, TValue> {
    columns: ColumnDef<TData, TValue>[]
    data: TData[]
}

export function DataTable<TData, TValue>( {
                                              columns,
                                              data,
                                          }: DataTableProps<TData, TValue> ) {
    const table = useReactTable( {
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
    } )

    return (
        <div className="overflow-hidden rounded-md border">
            <Table>
                <TableHeader>
                    { table.getHeaderGroups().map( ( headerGroup ) => (
                        <TableRow key={ headerGroup.id }>
                            { headerGroup.headers.map( ( header ) => {
                                return (
                                    <TableHead key={ header.id }>
                                        { header.isPlaceholder
                                            ? null
                                            : flexRender(
                                                header.column.columnDef.header,
                                                header.getContext()
                                            ) }
                                    </TableHead>
                                )
                            } ) }
                        </TableRow>
                    ) ) }
                </TableHeader>
                <TableBody>
                    { table.getRowModel().rows?.length ? (
                        table.getRowModel().rows.map( ( row ) => (
                            <TableRow
                                key={ row.id }
                                data-state={ row.getIsSelected() && "selected" }
                            >
                                { row.getVisibleCells().map( ( cell ) => (
                                    <TableCell key={ cell.id }>
                                        { flexRender( cell.column.columnDef.cell, cell.getContext() ) }
                                    </TableCell>
                                ) ) }
                            </TableRow>
                        ) )
                    ) : (
                        <TableRow>
                            <TableCell colSpan={ columns.length } className="h-24 text-center">
                                No results.
                            </TableCell>
                        </TableRow>
                    ) }
                </TableBody>
            </Table>
        </div>
    )
}

function DataTableHeader(
    { entity, currentView, EntityIcon, createActionLink, createActionLabel, additonalAction, dropdownAction, children }
    :
    {
        EntityIcon: ForwardRefExoticComponent<Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>>,
        entity: string,
        currentView: string,
        createActionLink: string,
        createActionLabel: string,
        createActionIcon: ForwardRefExoticComponent<Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>>,
        additonalAction?: ReactNode,
        dropdownAction?: ReactNode,
        children: ReactNode
    }
) {

    const newAction = <Button type={ "button" } className={ "self-end p-0" }>
        <Link href={ createActionLink }
              className={ "px-4 py-3 flex flex-row items-center gap-1" }><PlusIcon
            className={ "size-4" }/>{ createActionLabel }</Link>
    </Button>

    return <div className={ "p-8 flex flex-col gap-4 items-stretch justify-center pt-[2.49rem]" }>
        <div className={ "flex flex-row justify-between items-center" }>
            <div className={ "flex flex-row items-center gap-2 justify-center" }>
                <EntityIcon className={ "size-10 bg-primary text-primary-foreground rounded-lg p-2 stroke-1" }/>
                <div className={ "flex flex-col gap-0" }>
                    <small className={ "text-xs uppercase" }>{ entity }</small>
                    <h3 className={ "text-2xl font-bold leading-6" }>{ currentView }</h3>
                </div>
            </div>
            { additonalAction || dropdownAction ?
                <ButtonGroup>
                    { additonalAction }
                    <ButtonGroupSeparator/>
                    { newAction }
                    { dropdownAction ?
                        <>
                            <ButtonGroupSeparator/>
                            { dropdownAction }
                        </>
                        : null }
                </ButtonGroup>
                :
                newAction
            }
        </div>
        { children }
    </div>
}

export { DataTableHeader }