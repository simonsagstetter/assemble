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
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey
} from "@/api/rest/generated/query/user-management/user-management";
import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { getGetUserSessionDetailsQueryKey } from "@/api/rest/generated/query/session-management/session-management";
import { getMeQueryKey } from "@/api/rest/generated/query/users/users";

const getCreateUserInvalidations = (): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllUsersQueryKey(),
            refetchType: "all"
        }
    ];

const getUpdateUserInvalidations = ( data: UserAdmin ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllUsersQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetUserByIdQueryKey( data.id ),
            refetchType: "all"
        }
    ];

const getResetUserPasswordInvalidations = ( data: UserAdmin ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetUserSessionDetailsQueryKey( data.id ),
            refetchType: "all"
        }
    ];

const getUpdateUserEmployeeInvalidations = ( data: UserAdmin ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllUsersQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetUserByIdQueryKey( data.id ),
            refetchType: "all"
        },
        {
            queryKey: getMeQueryKey(),
            refetchType: "all"
        }
    ];

const getUpdateUserRoleInvalidations = ( data: UserAdmin ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllUsersQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetUserByIdQueryKey( data.id ),
            refetchType: "all"
        }
    ];

const getUpdateUserStatusInvalidations = ( data: UserAdmin ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllUsersQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetUserByIdQueryKey( data.id ),
            refetchType: "all"
        }
    ];

const getDeleteUserInvalidations = ( data: UserAdmin ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAllUsersQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetUserByIdQueryKey( data.id ),
            refetchType: "none"
        }
    ];

export {
    getCreateUserInvalidations,
    getUpdateUserInvalidations,
    getResetUserPasswordInvalidations,
    getUpdateUserEmployeeInvalidations,
    getUpdateUserRoleInvalidations,
    getUpdateUserStatusInvalidations,
    getDeleteUserInvalidations
}