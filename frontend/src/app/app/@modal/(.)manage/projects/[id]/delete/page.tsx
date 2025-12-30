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
import ProjectDeleteForm from "@/components/manage/projects/ProjectDeleteForm";
import dynamic from "next/dynamic";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function DeleteProjectModal() {
    const { id } = useParams<{ id: string }>();
    const { data: project } = useGetProjectByIdSuspense( id );
    return <ModalHeader title={ "Delete" } description={ "Confirm the action below to delete this project." }
                        entity={ "Project" }>
        <ProjectDeleteForm project={ project }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( DeleteProjectModal ), {
    ssr: false,
} );