/*
 * assemble
 * CalendarLoading.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { Spinner } from "@/components/ui/spinner";

export default function CalendarLoading() {
    return <div
        className={ "absolute bottom-16 left-0 w-full flex flex-row justify-center" }>
        <div
            className={ "bg-primary text-primary-foreground tracking-tight py-2 px-4 shadow-xl rounded-lg flex flex-row items-center gap-2" }>
            <Spinner className={ "inline-block size-4" }/>
            <span>Loading</span>
        </div>
    </div>
}
