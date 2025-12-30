/*
 * assemble
 * project.types.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { z } from "zod";
import { optionalString } from "@/utils/zod";
import { ProjectDTOStage, ProjectDTOType } from "@/api/rest/generated/query/openAPIDefinition.schemas";

const CreateProjectSchema = z.object( {
    name: z.string().trim().min( 1, "Name is required." ),
    active: z.boolean().optional(),
    type: optionalString( z.enum( ProjectDTOType ) ),
    category: z.string().trim().optional(),
    description: z.string().trim().optional(),
    stage: optionalString( z.enum( ProjectDTOStage ) )
} );

type CreateProjectFormData = z.infer<typeof CreateProjectSchema>;


export {
    type CreateProjectFormData,
    CreateProjectSchema
}