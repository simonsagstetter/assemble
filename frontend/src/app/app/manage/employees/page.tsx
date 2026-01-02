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

import { useGetAllEmployeesSuspense } from "@/api/rest/generated/query/employees/employees";
import dynamic from "next/dynamic";
import EmployeeDataTable from "@/components/manage/employees/EmployeeDataTable";

function EmployeesPage() {
    const { data: employees } = useGetAllEmployeesSuspense();

    return <EmployeeDataTable employees={ employees }/>
}

export default dynamic( () => Promise.resolve( EmployeesPage ), {
    ssr: false,
} );