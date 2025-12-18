/*
 * assemble
 * error.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

'use client' // Error boundaries must be Client Components

import { Empty, EmptyDescription, EmptyHeader, EmptyMedia, EmptyTitle } from "@/components/ui/empty";
import { ServerCrashIcon } from "lucide-react";

export default function Error( {
                                   error,
                                   reset,
                               }: {
    error: Error & { digest?: string }
    reset: () => void
} ) {
    return (
        <Empty className="w-full h-screen">
            <EmptyHeader>
                <EmptyMedia variant="default">
                    <ServerCrashIcon/>
                </EmptyMedia>
                <EmptyTitle>{ error.message || "Ooops! An unknown error occurred" }</EmptyTitle>
                <EmptyDescription>
                    <button className={ "hover:underline cursor-pointer" }
                            onClick={ reset }>{ "Please try to refresh the page. If the error persists, contact the administrator." }</button>
                </EmptyDescription>
            </EmptyHeader>
        </Empty>
    )
}