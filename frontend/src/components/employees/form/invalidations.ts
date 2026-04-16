/*
 * assemble
 * invalidations.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { InvalidateQueryFilters } from "@tanstack/react-query";
import { getGetAllEmployeesQueryKey, getGetEmployeeQueryKey } from "@/api/rest/generated/query/employees/employees";
import { EmployeeDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import {
    getGetAllProjectAssignmentsByEmployeeIdQueryKey
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { getMeQueryKey } from "@/api/rest/generated/query/users/users";

const getCreateEmployeeInvalidations = (): Array<InvalidateQueryFilters> => (
    [
        {
            queryKey: getGetAllEmployeesQueryKey(),
            refetchType: "all"
        }
    ]
);

const getUpdateEmployeeInvalidations = ( data: EmployeeDTO ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllEmployeesQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetEmployeeQueryKey( data.id ),
            refetchType: "all"
        }
    ];

const getUpdateEmployeeUserInvalidations = ( data: EmployeeDTO ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllEmployeesQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetEmployeeQueryKey( data.id ),
            refetchType: "all"
        },
        {
            queryKey: getMeQueryKey(),
            refetchType: "all"
        }
    ]

const getDeleteEmployeeInvalidations = ( data: EmployeeDTO ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllEmployeesQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetEmployeeQueryKey( data.id ),
            refetchType: "none"
        },
        {
            queryKey: getGetAllProjectAssignmentsByEmployeeIdQueryKey( data.id ),
            refetchType: "all"
        }
    ];

export {
    getCreateEmployeeInvalidations,
    getUpdateEmployeeInvalidations,
    getUpdateEmployeeUserInvalidations,
    getDeleteEmployeeInvalidations
}