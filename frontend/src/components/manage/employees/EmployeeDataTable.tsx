/*
 * assemble
 * EmployeeDataTable.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { EmployeeDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import Link from "next/link";
import { DataTable } from "@/components/custom-ui/DataTable";
import { DropdownMenu, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { MoreHorizontal } from "lucide-react";
import EmployeeActions from "@/components/manage/employees/EmployeeActions";

type EmployeeDataTableProps = {
    employees: EmployeeDTO[]
}

export default function EmployeeDataTable( { employees }: EmployeeDataTableProps ) {
    const columns: ColumnDef<EmployeeDTO>[] = useMemo( () => [
        {
            header: "No",
            cell( { row } ) {
                const employee = row.original;
                return <Link
                    href={ `/app/manage/employees/${ employee.id }` }
                    className={ "hover:underline" }
                >
                    { employee.no }
                </Link>
            }
        },
        {
            header: "Fullname",
            cell( { row } ) {
                const employee = row.original;
                return <Link
                    href={ `/app/manage/employees/${ employee.id }` }
                    className={ "hover:underline" }
                >
                    { employee.fullname }
                </Link>
            }
        },
        {
            accessorKey: "email",
            header: "E-Mail"
        },
        {
            header: "User",
            cell( { row } ) {
                const employee = row.original;

                if ( employee.user ) {
                    return <Link href={ `/app/admin/users/${ employee.user.id }` }
                                 className={ "hover:underline" }
                    >
                        { employee.user.username }
                    </Link>
                } else {
                    return <span>-</span>
                }
            }
        },
        {
            header: "Phone",
            accessorKey: "phone"
        },
        {
            id: "actions",
            header: "Actions",
            cell: ( { row } ) => {
                const employee = row.original;
                return (
                    <DropdownMenu dir="ltr">
                        <DropdownMenuTrigger asChild>
                            <Button variant="ghost" className="h-8 w-8 p-0">
                                <span className="sr-only">Open menu</span>
                                <MoreHorizontal className="h-4 w-4"/>
                            </Button>
                        </DropdownMenuTrigger>
                        <EmployeeActions id={ employee.id } hasUser={ employee.user != null } align={ "center" }
                                         tableActions/>
                    </DropdownMenu>
                )
            }
        },
    ], [] )

    return <DataTable columns={ columns } data={ employees }/>
}