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
import { useGetEmployeeSuspense } from "@/api/rest/generated/query/employees/employees";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import dynamic from "next/dynamic";
import EmployeeUpdateUserForm from "@/components/manage/employees/EmployeeUpdateUserForm";

function EmployeeUpdateUserPage() {
    const { id } = useParams<{ id: string }>();
    const { data: employee } = useGetEmployeeSuspense( id );
    return <FormPageHeader title={ "Edit" } description={ "Update the connected user of this employee." }
                           entity={ "Employee" }>
        <EmployeeUpdateUserForm employee={ employee }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( EmployeeUpdateUserPage ), {
    ssr: false,
} );