/*
 * assemble
 * global-provider.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
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

export default function GlobalProvider( { children }: Readonly<ProvidersProps> ) {
    return <AppProgressProvider
        height={ "4px" }
        color={ "oklch(0.5854 0.2041 277.1173)" }
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