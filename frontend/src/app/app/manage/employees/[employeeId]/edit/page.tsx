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
import EmployeeEditForm from "@/components/manage/employees/EmployeeEditForm";

function EmployeeEditPage() {
    const { employeeId } = useParams<{ employeeId: string }>();
    const { data: employee } = useGetEmployeeSuspense( employeeId );
    return <FormPageHeader title={ "Edit" } description={ "Update the fields and click save to update the employee." }
                           entity={ "Employee" }>
        <EmployeeEditForm employee={ employee }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( EmployeeEditPage ), {
    ssr: false,
} );