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
import { useGetProjectByIdSuspense } from "@/api/rest/generated/query/projects/projects";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import ProjectAssignmentCreateForm from "@/components/manage/projects/ProjectAssignmentCreateForm";
import {
    useGetAllProjectAssignmentsByProjectIdSuspense
} from "@/api/rest/generated/query/project-assignments/project-assignments";

function AssignEmployeesPage() {
    const { id } = useParams<{ id: string }>();
    const { data: project } = useGetProjectByIdSuspense( id );
    const { data: assignments } = useGetAllProjectAssignmentsByProjectIdSuspense( id );

    return <FormPageHeader title={ "Assign Employees" }
                           description={ "Assign an employee to the current project team." }
                           entity={ "Project" }>
        <ProjectAssignmentCreateForm project={ project } assignments={ assignments }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( AssignEmployeesPage ), {
    ssr: false,
} )