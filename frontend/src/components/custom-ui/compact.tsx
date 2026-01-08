/*
 * assemble
 * compact.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */


import { LucideProps } from "lucide-react";
import { Separator } from "@/components/ui/separator";
import { ForwardRefExoticComponent, Fragment, RefAttributes } from "react";

type CompactProps = {
    Icon: ForwardRefExoticComponent<Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>>;
    title: string;
    details: { label: string, value: string }[];
}

function Compact( { Icon, title, details }: CompactProps ) {

    return <div className={ "**:tracking-tight" }>
        <div className={ "flex flex-row items-center gap-3 justify-start bg-accent/50 p-2" }>
            <Icon className={ "size-7 bg-primary text-primary-foreground rounded-lg stroke-1 p-1" }/>
            <div className="text-lg text-accent-foreground">
                { title }
            </div>
        </div>
        <Separator/>
        <div className={ "bg-background p-2 text-left" }>
            { details.map( ( { label, value } ) => (
                <Fragment key={ label }>
                    <small className={ "text-xs text-accent-foreground" }>{ label }</small>
                    <p className={ "text-sm" }>{ value }</p>
                </Fragment>
            ) ) }
        </div>
    </div>
}

export {
    type CompactProps,
    Compact
}