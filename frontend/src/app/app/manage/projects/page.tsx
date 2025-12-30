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
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { PlusIcon } from "lucide-react";
import dynamic from "next/dynamic";
import ProjectDataTable from "@/components/manage/projects/ProjectDataTable";

function ProjectsPage() {
    const { data: projects } = useGetAllProjectsSuspense();

    return <div className={ "p-8 flex flex-col gap-4" }>
        <Button type={ "button" } className={ "self-end p-0" }>
            <Link href={ "/app/manage/projects/create" }
                  className={ "px-4 py-3 flex flex-row items-center gap-1" }><PlusIcon
                className={ "size-4" }/>New</Link>
        </Button>
        <ProjectDataTable projects={ projects }/>
    </div>
}

export default dynamic( () => Promise.resolve( ProjectsPage ), {
    ssr: false,
} );