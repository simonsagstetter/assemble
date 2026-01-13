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

import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import Link from "next/link";
import { DataTable, DataTableHeader } from "@/components/custom-ui/DataTable";
import {
    MoreHorizontal, PlusIcon, UserIcon,
} from "lucide-react";
import {
    DropdownMenu,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import UserActions from "@/components/admin/users/UserActions";
import Status from "@/components/custom-ui/status";
import { Badge } from "@/components/ui/badge";

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
            header: "Roles",
            cell( { row } ) {
                const user = row.original;
                return user.roles.map( role => <Badge variant={ "secondary" } className={ "mx-[0.1rem] select-none" }
                                                      key={ role }>{ role }</Badge> )
            }
        },
        {
            header: "Status",
            cell( { row } ) {
                const user = row.original;
                const fragments = [];
                if ( user.enabled ) fragments.push( <Status key={ "Enabled" } label={ "Enabled" }/> );
                else fragments.push( <Status key={ "Disabled" } label={ "Disabled" } variant={ "red" }/> )
                if ( user.locked ) fragments.push( <Status key={ "Locked" } label={ "Locked" } variant={ "red" }
                                                           className={ "mx-[0.1rem]" }/> )

                return fragments;
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
                                     isSuperuser={ user.roles.includes( "SUPERUSER" ) }
                        />
                    </DropdownMenu>
                )
            }
        },
    ], [] );

    return <DataTableHeader EntityIcon={ UserIcon }
                            entity={ "Users" }
                            currentView={ "All Users" }
                            createActionLink={ "/app/admin/users/create" }
                            createActionLabel={ "New" }
                            createActionIcon={ PlusIcon }>
        <DataTable columns={ columns } data={ users }/>
    </DataTableHeader>
}
