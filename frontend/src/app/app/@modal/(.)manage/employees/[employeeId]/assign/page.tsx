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
import {
    useGetAllProjectAssignmentsByEmployeeIdSuspense,
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import ModalHeader from "@/components/custom-ui/ModalHeader";
import ProjectAssignmentCreateForm from "@/components/manage/projects/ProjectAssignmentCreateForm";
import dynamic from "next/dynamic";
import { useGetEmployeeSuspense } from "@/api/rest/generated/query/employees/employees";

function AssignProjectModal() {
    const { employeeId } = useParams<{ employeeId: string }>();
    const { data: employee } = useGetEmployeeSuspense( employeeId );
    const { data: assignments } = useGetAllProjectAssignmentsByEmployeeIdSuspense( employeeId );
    return <ModalHeader title={ "Assign Projects" }
                        description={ "Assign a project to the current employee." }
                        entity={ "Employee" }>
        <ProjectAssignmentCreateForm assignments={ assignments } employee={ employee }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( AssignProjectModal ), {
    ssr: false,
} );
