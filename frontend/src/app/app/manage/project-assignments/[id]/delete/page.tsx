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
import {
    useGetProjectAssignmentByIdSuspense
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import ProjectAssignmentDeleteForm from "@/components/manage/projects/ProjectAssignmentDeleteForm";

function DeleteProjectAssignmentPage() {
    const { id } = useParams<{ id: string }>();
    const { data: assignment } = useGetProjectAssignmentByIdSuspense( id );

    return <FormPageHeader title={ "Delete" } description={ "Confirm the action below to delete this assignment." }
                           entity={ "Project Assignment" }>
        <ProjectAssignmentDeleteForm assignment={ assignment }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( DeleteProjectAssignmentPage ), {
    ssr: false,
} );