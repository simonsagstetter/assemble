/*
 * assemble
 * default.types.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

type ArrayElement<T> = T extends ( infer U )[] ? U : never;

export { type ArrayElement }