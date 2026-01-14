/*
 * assemble
 * auth.config.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

const LOGIN_PATH = "/auth/login";

const LOGIN_REDIRECT_PATH = "/app/timetracking/calendar";

const LOGOUT_PATH = "/auth/logout";

const ALLOWED_PATHS = [
    "/auth/login"
]

export {
    LOGIN_PATH,
    LOGIN_REDIRECT_PATH,
    LOGOUT_PATH,
    ALLOWED_PATHS
}