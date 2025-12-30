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
import EmployeeEditForm from "@/components/manage/employees/EmployeeEditForm";

function EmployeeEditModal() {
    const { id } = useParams<{ id: string }>();
    const { data: employee } = useGetEmployeeSuspense( id );
    return <ModalHeader title={ "Edit" } description={ "Update the fields and click save to update the employee." }
                        entity={ "Employee" }>
        <EmployeeEditForm employee={ employee }/>
    </ModalHeader>;
}


export default dynamic( () => Promise.resolve( EmployeeEditModal ), {
    ssr: false,
} );
