/*
 * assemble
 * SessionTable.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { SessionDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ColumnDef } from "@tanstack/react-table";
import { useMemo } from "react";
import { DataTable } from "@/components/custom-ui/DataTable";
import { format } from "date-fns";
import { Button } from "@/components/ui/button";
import { useInvalidateUserSessions } from "@/api/rest/generated/query/session-management/session-management";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { getMeQueryKey } from "@/api/rest/generated/query/users/users";
import { useRouter } from "@bprogress/next/app";

type SessionTableProps = {
    username: string;
    sessions: SessionDTO[]
}

export default function SessionTable( { username, sessions }: SessionTableProps ) {
    const invalidateSessions = useInvalidateUserSessions();
    const queryClient = useQueryClient();
    const router = useRouter();
    const columns: ColumnDef<SessionDTO>[] = useMemo( () => [
        {
            accessorKey: "sessionId",
            header: "Session ID"
        },
        {
            header: "Created",
            cell( { row } ) {
                const session = row.original;
                return format( session.createdDate, "dd.MM.yyyy - HH:mm:ss" )
            }
        },
        {
            header: "Last Access",
            cell( { row } ) {
                const session = row.original;
                return format( session.lastAccessedDate, "dd.MM.yyyy - HH:mm:ss" )
            }
        },
    ], [] )

    const handleInvalidateSessions = async () => {
        invalidateSessions.mutate( {
                principalName: username,
            },
            {
                onSuccess: () => {
                    toast.success( "Sessions invalidated" );
                    queryClient.invalidateQueries( {
                        queryKey: getMeQueryKey()
                    } );
                    router.refresh();
                },
                onError: () => {
                    toast.error( "Failed to invalidate sessions" );
                }
            } )
    }

    return <div className={ "w-full flex flex-col justify-start items-start gap-4" }>
        <Button className="self-end" type={ "button" } variant={ "outline" }
                onClick={ () => handleInvalidateSessions() }>Invalidate All
            Sessions</Button>
        <div className={ "self-stretch w-full" }>
            <DataTable columns={ columns } data={ sessions.filter( session => !session.isExpired ) }/>
        </div>
    </div>
}