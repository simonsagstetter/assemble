/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { useParams } from "next/navigation";
import dynamic from "next/dynamic";
import { useGetEmployeeSuspense } from "@/api/rest/generated/query/employees/employees";
import EmployeeDetail from "@/components/manage/employees/EmployeeDetail";

function EmployeeDetailPage() {
    const { id } = useParams<{ id: string }>();
    const { data: employee } = useGetEmployeeSuspense( id );
    return <EmployeeDetail employee={ employee }/>
}

export default dynamic( () => Promise.resolve( EmployeeDetailPage ), {
    ssr: false,
} );
