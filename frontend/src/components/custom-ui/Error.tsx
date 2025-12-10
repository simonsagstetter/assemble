/*
 * assemble
 * Error.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use server";
import { Empty, EmptyHeader, EmptyMedia, EmptyDescription, EmptyTitle } from "@/components/ui/empty";
import { ServerCrashIcon } from "lucide-react";

type ErrorProps = {
    title?: string;
    message?: string;
    customClassName?: string;
    error: Error;
    reset?: ( () => void ) | undefined;
}

export default async function Error( { title, message, customClassName, error, reset }: ErrorProps ) {
    return <Empty className={ customClassName || "w-full h-full" }>
        <EmptyHeader>
            <EmptyMedia variant="default">
                <ServerCrashIcon/>
            </EmptyMedia>
            <EmptyTitle>{ title || "Ooops! An error ocurred" }</EmptyTitle>
            <EmptyDescription>
                { message || "Please try to refresh the page. If the error persists, contact the administrator." }
            </EmptyDescription>
        </EmptyHeader>
    </Empty>
}