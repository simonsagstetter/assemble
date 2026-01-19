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
import { ProjectDTOColor, ProjectDTOStage, ProjectDTOType } from "@/api/rest/generated/query/openAPIDefinition.schemas";

const ProjectCreateSchema = z.object( {
    name: z.string().trim().min( 1, "Name is required." ),
    active: z.boolean().optional(),
    type: optionalString( z.enum( ProjectDTOType ) ),
    category: z.string().trim().optional(),
    description: z.string().trim().optional(),
    stage: optionalString( z.enum( ProjectDTOStage ) ),
    color: z.enum( ProjectDTOColor )
} );

type ProjectCreateFormData = z.infer<typeof ProjectCreateSchema>;

const ProjectAssignmentCreateSchema = z.object( {
    employeeId: z.string().trim().min( 1, "Employee ID is required." ),
    projectId: z.string().trim().min( 1, "Project ID is required." ),
    active: z.boolean(),
    hourlyRate: z.coerce.number<number>().optional()
} );

type ProjectAssignmentCreateFormData = z.infer<typeof ProjectAssignmentCreateSchema>;

const ProjectUpdateSchema = z.object( {
    name: z.string().trim().min( 1, "Name is required." ).optional(),
    active: z.boolean().optional(),
    type: optionalString( z.enum( ProjectDTOType ) ),
    category: z.string().trim().min( 1, "Category is required." ).optional(),
    description: z.string().trim().min( 1, "Description is required." ).optional(),
    stage: optionalString( z.enum( ProjectDTOStage ) ),
    color: optionalString( z.enum( ProjectDTOColor ) )
} )

type ProjectUpdateFormData = z.infer<typeof ProjectUpdateSchema>;

export {
    type ProjectCreateFormData,
    type ProjectAssignmentCreateFormData,
    type ProjectUpdateFormData,
    ProjectCreateSchema,
    ProjectUpdateSchema,
    ProjectAssignmentCreateSchema
}