/*
 * assemble
 * zod.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { z } from "zod";

const optionalString = <T extends z.ZodTypeAny>( schema: T ) =>
    z.preprocess(
        ( val: string | undefined ) => val === "" ? undefined : val,
        schema.optional()
    );

export { optionalString }