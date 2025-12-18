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
    HatGlassesIcon,
    IdCardIcon,
    MoreHorizontal,
    PencilIcon,
    RectangleEllipsisIcon,
    TrashIcon,
    UserCogIcon,
    UserIcon,
    UserLockIcon,
    XIcon
} from "lucide-react";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { useRouter } from "@bprogress/next/app";

type UserDataTableProps = {
    users: UserAdmin[];
}

export default function UserDataTable( { users }: UserDataTableProps ) {
    const router = useRouter();
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
                        <DropdownMenuContent align="start">
                            <DropdownMenuItem
                                onClick={ () => router.push( `/app/admin/users/${ user.id }/employee` ) }
                            >
                                <IdCardIcon/> { user.employee == null ? "Connect Employee" : "Update Employee" }
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={ () => router.push( `/app/admin/users/${ user.id }/status` ) }
                            >
                                <UserLockIcon/> Update Status
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={ () => router.push( `/app/admin/users/${ user.id }/roles` ) }
                            >
                                <UserCogIcon/> Update Roles
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={ () => router.push( `/app/admin/users/${ user.id }/reset-password` ) }
                            >
                                <RectangleEllipsisIcon/> Reset Password
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem
                                onClick={ () => router.push( `/app/admin/users/${ user.id }` ) }
                            >
                                <UserIcon/> Details
                            </DropdownMenuItem>
                            <DropdownMenuItem
                                onClick={ () => router.push( `/app/admin/users/${ user.id }/edit` ) }>
                                <PencilIcon className={ "size-3" }/> Edit
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem className={ "text-yellow-600" }
                                              onClick={ () => {
                                              } }>
                                <HatGlassesIcon className={ "text-shadow-yellow-600" }></HatGlassesIcon> Impersonate
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem className={ "text-red-600" }
                                              onClick={ () => router.push( `/app/admin/users/${ user.id }/delete` ) }>
                                <TrashIcon className={ "text-red-600" }/> Delete
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                )
            }
        },
    ], [ router ] );

    return <DataTable columns={ columns } data={ users }/>
}
