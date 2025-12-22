/*
 * assemble
 * useFormActionContext.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { FormActionContext } from "@/store/formActionStore";
import { use } from "react";

export default function useFormActionContext() {
    const ctx = use( FormActionContext )

    if ( !ctx ) {
        throw new Error( "useFormActionContext must be used inside FormActionProvider" )
    }

    return ctx
}
