/*
 * assemble
 * invalidations.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { ProjectDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { getGetAllProjectsQueryKey, getGetProjectByIdQueryKey } from "@/api/rest/generated/query/projects/projects";
import { InvalidateQueryFilters } from "@tanstack/react-query";
import {
    getGetAllProjectAssignmentsByEmployeeIdQueryKey,
    getGetAllProjectAssignmentsByProjectIdQueryKey, getGetOwnProjectAssignmentsQueryKey
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { ProjectAssignmentDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";

const getCreateProjectInvalidations = (): Array<InvalidateQueryFilters> => {
    return [
        {
            queryKey: getGetAllProjectsQueryKey(),
            refetchType: "all"
        }
    ]
}

const getUpdateProjectInvalidations = ( data: ProjectDTO ): Array<InvalidateQueryFilters> => {
    return [
        {
            queryKey: getGetProjectByIdQueryKey( data.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetAllProjectsQueryKey(),
            refetchType: "all"
        }
    ]
}

const getDeleteProjectInvalidations = ( data: ProjectDTO ): Array<InvalidateQueryFilters> => {
    return [
        {
            queryKey: getGetAllProjectsQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetProjectByIdQueryKey( data.id ),
            refetchType: "none"
        },
        {
            queryKey: getGetAllProjectAssignmentsByProjectIdQueryKey( data.id ),
            refetchType: "all"
        }
    ]
}

const getDeleteProjectAssignmentInvalidations = ( data: ProjectAssignmentDTO ): Array<InvalidateQueryFilters> => {
    return [
        {
            queryKey: getGetAllProjectAssignmentsByProjectIdQueryKey( data.project.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetAllProjectAssignmentsByEmployeeIdQueryKey( data.employee.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetOwnProjectAssignmentsQueryKey(),
            refetchType: "all"
        }
    ]
}

const getCreateProjectAssignmentInvalidations = ( data: ProjectAssignmentDTO ): Array<InvalidateQueryFilters> => {
    return [
        {
            queryKey: getGetAllProjectAssignmentsByProjectIdQueryKey( data.project.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetAllProjectAssignmentsByEmployeeIdQueryKey( data.employee.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetOwnProjectAssignmentsQueryKey(),
            refetchType: "all"
        }
    ]
}

export {
    getCreateProjectInvalidations,
    getUpdateProjectInvalidations,
    getDeleteProjectInvalidations,
    getDeleteProjectAssignmentInvalidations,
    getCreateProjectAssignmentInvalidations
}