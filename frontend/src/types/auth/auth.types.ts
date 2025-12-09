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


export {
    type LoginForm,
    LoginFormSchema,
}