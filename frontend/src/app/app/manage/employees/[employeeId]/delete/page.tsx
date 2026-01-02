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
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import EmployeeDeleteForm from "@/components/manage/employees/EmployeeDeleteForm";

function EmployeeDeletePage() {
    const { employeeId } = useParams<{ employeeId: string }>();
    const { data: employee } = useGetEmployeeSuspense( employeeId );
    return <FormPageHeader title={ "Delete" } description={ "Confirm the action below to delete this employee." }
                           entity={ "Employee" }>
        <EmployeeDeleteForm employee={ employee }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( EmployeeDeletePage ), {
    ssr: false,
} );