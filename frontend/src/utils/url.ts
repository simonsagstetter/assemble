/*
 * assemble
 * url.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

const isValidRoutePath = ( path: string ) => /^\/(?!.*\/\/)[a-zA-Z0-9_-]*(\/[a-zA-Z0-9_-]*)*$/.test( path );

export { isValidRoutePath }