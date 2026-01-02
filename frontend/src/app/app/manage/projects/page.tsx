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

import { useGetAllProjectsSuspense } from "@/api/rest/generated/query/projects/projects";
import dynamic from "next/dynamic";
import ProjectDataTable from "@/components/manage/projects/ProjectDataTable";

function ProjectsPage() {
    const { data: projects } = useGetAllProjectsSuspense();
    return <ProjectDataTable projects={ projects }/>
}

export default dynamic( () => Promise.resolve( ProjectsPage ), {
    ssr: false,
} );