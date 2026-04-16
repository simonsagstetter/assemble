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
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import dynamic from "next/dynamic";
import { useParams } from "next/navigation";
import { useGetProjectByIdSuspense } from "@/api/rest/generated/query/projects/projects";
import ProjectEditForm from "@/components/projects/ProjectEditForm";

function EditProjectPage() {
    const { projectId } = useParams<{ projectId: string }>();
    const { data: project } = useGetProjectByIdSuspense( projectId );
    return <FormPageHeader title={ "Edit" } description={ "Update the fields and click save to edit the project." }
                           entity={ "Project" }>
        <ProjectEditForm project={ project }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( EditProjectPage ), { ssr: false } );
