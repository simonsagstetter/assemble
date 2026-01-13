/*
 * assemble
 * ProjectCompact.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { EntityCompact } from "@/components/custom-ui/compact";
import { LayersIcon } from "lucide-react";
import Link from "next/link";
import { colorMobileClasses } from "@/config/calendar/calendar.config";
import { ProjectDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";

type ProjectCompactProps = {
    project: ProjectDTO;
}

export default function ProjectCompact( { project }: ProjectCompactProps ) {
    const details = [
        {
            label: "Stage",
            value: project.stage,
        },
        {
            label: "Category",
            value: project.category,
        },
        {
            label: "Status",
            value: project.active ? "Active" : "Inactive",
        },
        {
            label: "Description",
            value: project.description ?? "-",
        }
    ]
    return <EntityCompact Icon={ LayersIcon } title={ project.name } details={ details }>
        <Link
            href={ `/app/manage/projects/${ project.id }` }
            className={ "hover:underline flex flex-row items-center gap-1.5" }
        >
                        <span
                            className={ `size-2 rounded-full ${ colorMobileClasses[ project.color.toLowerCase() as keyof typeof colorMobileClasses ] }` }></span>
            { project.name }
        </Link>
    </EntityCompact>
}