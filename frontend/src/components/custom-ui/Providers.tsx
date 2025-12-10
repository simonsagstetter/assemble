/*
 * assemble
 * Providers.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { ReactNode } from "react";
import { AppProgressProvider } from "@bprogress/next";

type ProvidersProps = {
    children: Readonly<ReactNode>
}

export default function Providers( { children }: ProvidersProps ) {
    return <AppProgressProvider
        height={ "4px" }
        color={ "oklch(14.1% 0.005 285.823)" }
        options={ {
            showSpinner: false,
        } }
        shallowRouting
        disableSameURL
        startOnLoad
    >
        { children }
    </AppProgressProvider>
}