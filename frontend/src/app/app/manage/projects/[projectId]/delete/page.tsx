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

import dynamic from "next/dynamic";
import { useParams } from "next/navigation";
import { useGetProjectByIdSuspense } from "@/api/rest/generated/query/projects/projects";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import ProjectDeleteForm from "@/components/manage/projects/ProjectDeleteForm";

function DeleteProjectPage() {
    const { projectId } = useParams<{ projectId: string }>();
    const { data: project } = useGetProjectByIdSuspense( projectId );
    return <FormPageHeader title={ "Delete" } description={ "Confirm the action below to delete this project." }
                           entity={ "Project" }>
        <ProjectDeleteForm project={ project }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( DeleteProjectPage ), {
    ssr: false,
} );