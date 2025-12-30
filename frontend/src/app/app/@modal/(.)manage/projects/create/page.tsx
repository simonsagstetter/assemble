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

import ModalHeader from "@/components/custom-ui/ModalHeader";
import dynamic from "next/dynamic";
import ProjectCreateForm from "@/components/manage/projects/ProjectCreateForm";

function CreateProjectModal() {
    return <ModalHeader title={ "New" } description={ "Fill out the fields and click new to create a new project." }
                        entity={ "Project" }>
        <ProjectCreateForm/>
    </ModalHeader>;
}


export default dynamic( () => Promise.resolve( CreateProjectModal ), {
    ssr: false,
} );