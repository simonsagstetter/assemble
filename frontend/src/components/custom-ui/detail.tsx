/*
 * assemble
 * detail.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { ReactNode } from "react";

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
    return <span className={ "border-b-1 block pt-2 pb1 text-zinc-900 text-sm" }>{ children }</span>
}

