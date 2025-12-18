/*
 * assemble
 * SessionDetail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import Loading from "@/components/custom-ui/Loading";
import { useGetUserSessionDetails } from "@/api/rest/generated/query/session-management/session-management";
import SessionTable from "@/components/admin/session/SessionTable";

export default function SessionDetail( { username }: { username: string } ) {
    const { data: sessionData, isError, isLoading } = useGetUserSessionDetails( username, { query: { staleTime: 0 } } );

    if ( isLoading || !sessionData ) return <Loading/>

    if ( isError ) return <p>Failed to load session data</p>

    return <SessionTable sessions={ sessionData } username={ username }/>
}