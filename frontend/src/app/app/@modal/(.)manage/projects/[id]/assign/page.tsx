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
import { useGetProjectByIdSuspense } from "@/api/rest/generated/query/projects/projects";
import ModalHeader from "@/components/custom-ui/ModalHeader";
import dynamic from "next/dynamic";
import {
    useGetAllProjectAssignmentsByProjectIdSuspense
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import ProjectAssignmentCreateForm from "@/components/manage/projects/ProjectAssignmentCreateForm";

function AssignEmployeesModal() {
    const { id } = useParams<{ id: string }>();
    const { data: project } = useGetProjectByIdSuspense( id );
    const { data: assignments } = useGetAllProjectAssignmentsByProjectIdSuspense( id );
    return <ModalHeader title={ "Assign Employees" }
                        description={ "Assign an employee to the current project team." }
                        entity={ "Project" }>
        <ProjectAssignmentCreateForm project={ project } assignments={ assignments }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( AssignEmployeesModal ), {
    ssr: false,
} );