/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client"
import { useGetAllUsersSuspense } from "@/api/rest/generated/query/user-management/user-management";
import dynamic from "next/dynamic";
import UserDataTable from "@/components/admin/users/UserDataTable";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { PlusIcon } from "lucide-react";

function UsersPage() {
    const { data: users } = useGetAllUsersSuspense();

    return <div className={ "p-8 flex flex-col gap-4" }>
        <Button type={ "button" } className={ "self-end p-0" }>
            <Link href={ "/app/admin/users/create" }
                  className={ "px-4 py-3 flex flex-row items-center gap-1" }><PlusIcon
                className={ "size-4" }/>New</Link>
        </Button>
        <UserDataTable users={ users }/>
    </div>
}

export default dynamic( () => Promise.resolve( UsersPage ), {
    ssr: false,
} );
