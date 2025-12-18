/*
 * assemble
 * Loading.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */


import { Empty, EmptyDescription, EmptyHeader, EmptyMedia, EmptyTitle } from "@/components/ui/empty";
import { Spinner } from "@/components/ui/spinner";

type LoadingProps = {
    title?: string;
    message?: string;
    customClassName?: string;
    hideMessage?: boolean;
}

export default function Loading( { title, message, customClassName, hideMessage = false }: LoadingProps ) {
    return <Empty className={ customClassName || "w-full h-full" }>
        <EmptyHeader>
            <EmptyMedia variant="default">
                <Spinner className="size-8"/>
            </EmptyMedia>
            <EmptyTitle>{ title || "App is loading" }</EmptyTitle>
            { !hideMessage ?
                <EmptyDescription>
                    { message || "Getting everything ready for you. Do not refresh the page." }
                </EmptyDescription>
                : null }
        </EmptyHeader>
    </Empty>
}