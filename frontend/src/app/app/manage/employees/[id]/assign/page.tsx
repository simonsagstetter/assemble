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
import {
    useGetAllProjectAssignmentsByEmployeeIdSuspense,
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import ProjectAssignmentCreateForm from "@/components/manage/projects/ProjectAssignmentCreateForm";

function AssignProjectsPage() {
    const { id } = useParams<{ id: string }>();
    const { data: employee } = useGetEmployeeSuspense( id );
    const { data: assignments } = useGetAllProjectAssignmentsByEmployeeIdSuspense( id );
    return <FormPageHeader title={ "Assign Projects" }
                           description={ "Assign a project to the current employee." }
                           entity={ "Employee" }>
        <ProjectAssignmentCreateForm assignments={ assignments } employee={ employee }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( AssignProjectsPage ), {
    ssr: false,
} );