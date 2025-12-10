/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { ReactNode } from "react";
import { BlocksIcon } from "lucide-react";

type AuthLayoutProps = {
    children: Readonly<ReactNode>
}

export default function AuthLayout( { children }: AuthLayoutProps ) {

    return <div className="flex min-h-svh flex-col items-center justify-center gap-6 p-6 md:p-10">
        <div className="flex w-full max-w-sm flex-col gap-6">
            <div className="flex items-center gap-2 self-center font-medium text-xl select-none">
                <div className="bg-primary text-primary-foreground flex size-6 items-center justify-center rounded-md">
                    <BlocksIcon className="size-4"/>
                </div>
                Assemble
            </div>
            { children }
        </div>
    </div>
}
