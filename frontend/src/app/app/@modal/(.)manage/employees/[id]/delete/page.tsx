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
import ModalHeader from "@/components/custom-ui/ModalHeader";
import dynamic from "next/dynamic";
import EmployeeDeleteForm from "@/components/manage/employees/EmployeeDeleteForm";
import { useParams } from "next/navigation";
import { useGetEmployeeSuspense } from "@/api/rest/generated/query/employees/employees";

function EmployeeDeleteModal() {
    const { id } = useParams<{ id: string }>();
    const { data: employee } = useGetEmployeeSuspense( id );
    return <ModalHeader title={ "Delete" } description={ "Confirm the action below to delete this employee." }
                        entity={ "Employee" }>
        <EmployeeDeleteForm employee={ employee }/>
    </ModalHeader>;
}


export default dynamic( () => Promise.resolve( EmployeeDeleteModal ), {
    ssr: false,
} );
