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
import { useGetProjectByIdSuspense } from "@/api/rest/generated/query/projects/projects";
import { useParams } from "next/navigation";
import ProjectDetail from "@/components/manage/projects/ProjectDetail";

function ProjectDetailsPage() {
    const { id } = useParams<{ id: string }>();
    const { data: project } = useGetProjectByIdSuspense( id );

    return <ProjectDetail project={ project }/>
}

export default dynamic( () => Promise.resolve( ProjectDetailsPage ), {
    ssr: false,
} );