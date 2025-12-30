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
import ModalHeader from "@/components/custom-ui/ModalHeader";
import dynamic from "next/dynamic";
import EmployeeUpdateUserForm from "@/components/manage/employees/EmployeeUpdateUserForm";

function EmployeeUpdateUserModal() {
    const { id } = useParams<{ id: string }>();
    const { data: employee } = useGetEmployeeSuspense( id );
    return <ModalHeader title={ "Edit" } description={ "Update the connected user of this employee." }
                        entity={ "Employee" }>
        <EmployeeUpdateUserForm employee={ employee }/>
    </ModalHeader>;
}


export default dynamic( () => Promise.resolve( EmployeeUpdateUserModal ), {
    ssr: false,
} );
