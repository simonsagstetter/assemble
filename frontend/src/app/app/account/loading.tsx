/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Skeleton } from "@/components/ui/skeleton";
import { Card, CardContent, CardHeader } from "@/components/ui/card";

export default function Loading() {
    return <div className={ "p-8" }>
        <Card className={ "p-8 space-y-4" }>
            <CardHeader>
                <Skeleton className={ "h-6 w-36" }/>
                <Skeleton className={ "h-5 w-56" }/>
            </CardHeader>
            <CardContent className={ "space-y-6" }>
                <div className={ "space-y-2" }>
                    <Skeleton className={ "h-5 w-36" }/>
                    <Skeleton className={ "h-4 w-56" }/>
                </div>
                <div className={ "flex flex-row gap-10" }>
                    <Skeleton className={ "h-8 w-full" }/>
                    <Skeleton className={ "h-8 w-full" }/>
                </div>
                <div className={ "flex flex-row gap-10" }>
                    <Skeleton className={ "h-8 w-full" }/>
                    <Skeleton className={ "h-8 w-full" }/>
                </div>
                <div className={ "h-6 w-full" }></div>
                <div className={ "space-y-2" }>
                    <Skeleton className={ "h-6 w-36" }/>
                    <Skeleton className={ "h-4 w-56" }/>
                </div>
                <div className={ "space-y-4" }>
                    <Skeleton className={ "h-8 w-full" }/>
                    <Skeleton className={ "h-8 w-full" }/>
                    <Skeleton className={ "h-8 w-full" }/>
                </div>
            </CardContent>
        </Card>
    </div>
}
