/*
 * assemble
 * user.types.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { z } from "zod";
import { UserRolesItem } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";

const UserCreateSchema = z.object( {
    username: z.string()
        .trim()
        .min( 4, "Username must be at least 4 characters long." )
        .max( 20, "Username cannot be longer than 20 characters." ),
    password: z.string()
        .trim().optional(),
    firstname: z.string().trim().min( 1, "Firstname is required." ),
    lastname: z.string().trim().min( 1, "Lastname is required." ),
    email: z.email(),
    roles: z.array( z.enum( UserRolesItem ) ),
    enabled: z.boolean(),
    employeeId: z.string().optional(),
} )

type UserCreateFormData = z.infer<typeof UserCreateSchema>;

const UserUpdateSchema = z.object( {
    username: z.string()
        .trim()
        .min( 4, "Username must be at least 4 characters long." )
        .max( 20, "Username cannot be longer than 20 characters." ),
    firstname: z.string().trim().min( 1, "Firstname is required." ),
    lastname: z.string().trim().min( 1, "Lastname is required." ),
    email: z.email()
} )

type UserUpdateFormData = z.infer<typeof UserUpdateSchema>;

const UserStatusSchema = z.object( {
    enabled: z.boolean(),
    locked: z.boolean()
} )

type UserStatusFormData = z.infer<typeof UserStatusSchema>;

const UserResetPasswordSchema = z.object( {
    newPassword: z.string().trim().min( 1, "Password is required." ),
    invalidateAllSessions: z.boolean().optional()
} )

type UserResetPasswordFormData = z.infer<typeof UserResetPasswordSchema>;

const UserUpdateRolesSchema = z.object( {
    roles: z.array( z.enum( UserRolesItem ) )
} )

type UserUpdateRolesFormData = z.infer<typeof UserUpdateRolesSchema>;

const UserUpdateEmpployeeSchema = z.object( {
    employeeId: z.string().optional()
} )

type UserUpdateEmpployeeFormData = z.infer<typeof UserUpdateEmpployeeSchema>;

export {
    type UserCreateFormData,
    type UserUpdateFormData,
    type UserStatusFormData,
    type UserResetPasswordFormData,
    type UserUpdateRolesFormData,
    type UserUpdateEmpployeeFormData,
    UserCreateSchema,
    UserUpdateSchema,
    UserStatusSchema,
    UserResetPasswordSchema,
    UserUpdateRolesSchema,
    UserUpdateEmpployeeSchema
}