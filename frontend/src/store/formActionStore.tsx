/*
 * assemble
 * formActionStore.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { createContext } from "react";

interface FormActionContext {
    isPending: boolean,
    isSuccess: boolean,
    isError: boolean,
    handleCancel: () => void,
    disableOnSuccess: boolean
}

const FormActionContext = createContext<FormActionContext | null>( null );

export {
    FormActionContext
}