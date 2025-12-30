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
import ProjectCreateForm from "@/components/manage/projects/ProjectCreateForm";

export default function CreateProjectPage() {
    return <FormPageHeader title={ "New" } description={ "Fill out the fields and click new to create a new project." }
                           entity={ "Project" }>
        <ProjectCreateForm/>
    </FormPageHeader>
}
