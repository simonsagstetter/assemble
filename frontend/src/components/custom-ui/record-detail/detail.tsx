/*
 * assemble
 * detail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { ComponentProps, ReactNode, useState } from "react";
import { EyeClosedIcon, EyeIcon } from "lucide-react";
import { Badge } from "@/components/ui/badge";

export function DetailSection( { children }: { children: Readonly<ReactNode> } ) {
    return <div className={ "flex flex-col gap-4" }>{ children }</div>
}

export function DetailRow( { children }: { children: Readonly<ReactNode> } ) {
    return <div className={ `grid grid-cols-2 gap-16` }>{ children }</div>
}

export function Detail( { children }: { children: Readonly<ReactNode> } ) {
    return <div>
        { children }
    </div>
}

export function DetailLabel( { children }: { children: string } ) {
    return <label className={ "block text-xs text-zinc-600 tracking-tight select-none" }>{ children }</label>
}

export function DetailValue( { children }: { children: Readonly<ReactNode> } ) {
    return <span
        className={ "border-b-1 block pt-2 pb-1 text-zinc-900 text-sm" }>{ children ? children : "-" }</span>
}

export function BadgeDetailValue( { children, ...props }: {
    children: Readonly<ReactNode>
} & ComponentProps<typeof Badge> ) {
    return <span
        className={ "border-b-1 block pt-2 pb-1 text-zinc-900 text-sm" }>
            <Badge { ...props }>{ children }</Badge>
        </span>
}

export function SensitiveDetailValue( { children }: { children: Readonly<ReactNode> } ) {
    const [ isHidden, setIsHidden ] = useState<boolean>( true );

    return <div className={ "border-b-1 block pt-2 pb-1 text-zinc-900 text-sm" }>
        { isHidden ?
            <div className={ "relative" }>
                <span>{ "*****************" }</span>
                <button type={ "button" }
                        onClick={ () => setIsHidden( false ) }
                        className={ "cursor-pointer absolute right-2" }
                        aria-label={ "Show sensitive data" }>
                    <EyeClosedIcon className={ "text-zinc-500 size-4" }/>
                </button>
            </div>
            : <div className={ "relative" }>
                <span>{ children ? children : "-" }</span>
                <button type={ "button" }
                        onClick={ () => setIsHidden( true ) }
                        className={ "cursor-pointer absolute right-2" }
                        aria-label={ "Hide sensitive data" }>
                    <EyeIcon className={ "text-zinc-500 size-4" }/>
                </button>
            </div> }
    </div>
}