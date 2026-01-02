/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { useParams } from "next/navigation";
import {
    useGetProjectAssignmentByIdSuspense
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import ProjectAssignmentDeleteForm from "@/components/manage/projects/ProjectAssignmentDeleteForm";
import dynamic from "next/dynamic";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function DeleteAssignmentModal() {
    const { assignmentId } = useParams<{ assignmentId: string }>();
    const { data: assignment } = useGetProjectAssignmentByIdSuspense( assignmentId );

    return <ModalHeader title={ "Delete" } description={ "Confirm the action below to delete this assignment." }
                        entity={ "Project Assignment" }>
        <ProjectAssignmentDeleteForm assignment={ assignment }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( DeleteAssignmentModal ), {
    ssr: false,
} );