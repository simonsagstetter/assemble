/*
 * assemble
 * invalidations.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { InvalidateQueryFilters } from "@tanstack/react-query";
import {
    getGetAllTimeEntriesByEmployeeIdQueryKey,
    getGetAllTimeEntriesByProjectIdQueryKey,
    getGetAllTimeEntriesQueryKey,
    getGetOwnTimeEntriesQueryKey,
    getGetOwnTimeEntryByIdQueryKey,
    getGetTimeEntryByIdQueryKey
} from "@/api/rest/generated/query/timeentries/timeentries";
import { getGetProjectByIdQueryKey } from "@/api/rest/generated/query/projects/projects";
import { getGetEmployeeQueryKey } from "@/api/rest/generated/query/employees/employees";

const getCreateTimeEntryInvalidations = ( data: TimeEntryDTO ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetOwnTimeEntriesQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetOwnTimeEntriesQueryKey( { aroundDate: data.date } ),
            refetchType: "all"
        },
        {
            queryKey: getGetOwnTimeEntryByIdQueryKey( data.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetTimeEntryByIdQueryKey( data.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetProjectByIdQueryKey( data.project.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetEmployeeQueryKey( data.employee.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetAllTimeEntriesByProjectIdQueryKey( data.project.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetAllTimeEntriesByEmployeeIdQueryKey( data.employee.id ),
            refetchType: "all"
        },
        {
            queryKey: getGetAllTimeEntriesQueryKey(),
            refetchType: "all"
        }
    ];

const getUpdateTimeEntryInvalidations = getCreateTimeEntryInvalidations;

const getDeleteTimeEntryInvalidations = ( data: TimeEntryDTO ): Array<InvalidateQueryFilters> => [
    {
        queryKey: getGetOwnTimeEntriesQueryKey(),
        refetchType: "all"
    },
    {
        queryKey: getGetOwnTimeEntriesQueryKey( { aroundDate: data.date } ),
        refetchType: "all"
    },
    {
        queryKey: getGetProjectByIdQueryKey( data.project.id ),
        refetchType: "all"
    },
    {
        queryKey: getGetEmployeeQueryKey( data.employee.id ),
        refetchType: "all"
    },
    {
        queryKey: getGetAllTimeEntriesByProjectIdQueryKey( data.project.id ),
        refetchType: "all"
    },
    {
        queryKey: getGetAllTimeEntriesByEmployeeIdQueryKey( data.employee.id ),
        refetchType: "all"
    },
    {
        queryKey: getGetAllTimeEntriesQueryKey(),
        refetchType: "all"
    },
    {
        queryKey: getGetTimeEntryByIdQueryKey( data.id ),
        refetchType: "none"
    }
]

export {
    getCreateTimeEntryInvalidations,
    getUpdateTimeEntryInvalidations,
    getDeleteTimeEntryInvalidations
}