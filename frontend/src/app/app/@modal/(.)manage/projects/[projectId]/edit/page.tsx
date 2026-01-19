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
import { useGetProjectByIdSuspense } from "@/api/rest/generated/query/projects/projects";
import ProjectEditForm from "@/components/manage/projects/ProjectEditForm";
import dynamic from "next/dynamic";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function EditProjectPage() {
    const { projectId } = useParams<{ projectId: string }>();
    const { data: project } = useGetProjectByIdSuspense( projectId );
    return <ModalHeader title={ "Edit" } description={ "Update the fields and click save to update the project." }
                        entity={ "Project" }>
        <ProjectEditForm project={ project }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( EditProjectPage ), { ssr: false } );
