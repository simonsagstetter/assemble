/*
 * assemble
 * auth.types.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { z } from "zod";

const LoginFormSchema = z.object( {
    username: z.string().trim().min( 1, "Username is required." ),
    password: z.string().trim().min( 1, "Password is required." ),
} );

type LoginForm = z.infer<typeof LoginFormSchema>;

const ChangePasswordSchema = z.object( {
    oldPassword: z.string().trim().min( 1, "Old password is required." ),
    newPassword: z.string().trim().min( 1, "New password is required." ),
    confirmPassword: z.string().trim().min( 1, "Confirm password is required." ),
} )

type ChangePasswordFormData = z.infer<typeof ChangePasswordSchema>;

export {
    type LoginForm,
    LoginFormSchema,
    type ChangePasswordFormData,
    ChangePasswordSchema
}