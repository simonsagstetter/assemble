/*
 * assemble
 * UserDataTable.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { UserAdmin, UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import Link from "next/link";
import { DataTable } from "@/components/custom-ui/DataTable";
import {
    CheckIcon,
    MoreHorizontal,
    XIcon
} from "lucide-react";
import {
    DropdownMenu,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import UserActions from "@/components/admin/users/UserActions";

type UserDataTableProps = {
    users: UserAdmin[];
}

export default function UserDataTable( { users }: UserDataTableProps ) {
    const columns: ColumnDef<UserAdmin>[] = useMemo( () => [
        {
            accessorKey: "username",
            header: "Username",
            cell( { row } ) {
                const user = row.original;
                return <Link
                    href={ `/app/admin/users/${ user.id }` }
                    className={ "hover:underline" }
                >
                    { user.username }
                </Link>
            }
        },
        { accessorKey: "email", header: "E-Mail" },
        {
            header: "Employee",
            cell( { row } ) {
                const user = row.original;

                if ( user.employee ) {
                    return <Link href={ `/app/manage/employees/${ user.employee.id }` }
                                 className={ "hover:underline" }
                    >
                        { user.employee.fullname }
                    </Link>
                } else {
                    return <span>-</span>
                }
            }
        },
        {
            header: "Manager",
            cell( { row } ) {
                const user = row.original;
                if ( user.roles.includes( UserRolesItem.MANAGER ) ) {
                    return <CheckIcon className="size-4 text-green-600"/>
                } else {
                    return <XIcon className={ "size-4 text-red-600" }/>
                }
            }
        },
        {
            header: "Admin",
            cell( { row } ) {
                const user = row.original;
                if ( user.roles.includes( UserRolesItem.ADMIN ) || user.roles.includes( UserRolesItem.SUPERUSER ) ) {
                    return <CheckIcon className="size-4 text-green-600"/>
                } else {
                    return <XIcon className={ "size-4 text-red-600" }/>
                }
            }
        },
        {
            header: "Enabled",
            cell( { row } ) {
                const user = row.original;
                if ( user.enabled ) {
                    return <CheckIcon className="size-4 text-green-600"/>
                } else {
                    return <XIcon className={ "size-4 text-red-600" }/>
                }
            }
        },
        {
            header: "Locked",
            cell( { row } ) {
                const user = row.original;
                if ( user.locked ) {
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
                const user = row.original;

                return (
                    <DropdownMenu dir="ltr">
                        <DropdownMenuTrigger asChild>
                            <Button variant="ghost" className="h-8 w-8 p-0">
                                <span className="sr-only">Open menu</span>
                                <MoreHorizontal className="h-4 w-4"/>
                            </Button>
                        </DropdownMenuTrigger>
                        <UserActions id={ user.id }
                                     hasEmployee={ user.employee != null }
                                     align={ "center" }
                                     tableActions
                        />
                    </DropdownMenu>
                )
            }
        },
    ], [] );

    return <DataTable columns={ columns } data={ users }/>
}
