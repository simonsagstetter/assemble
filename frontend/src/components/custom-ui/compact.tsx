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
import React, {
    ForwardRefExoticComponent,
    Fragment,
    ReactNode,
    RefAttributes,
    useCallback,
    useState,
    MouseEvent, useRef
} from "react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { useDebounceCallback } from "usehooks-ts";
import { Timeout } from "@radix-ui/primitive";

type CompactProps = {
    Icon: ForwardRefExoticComponent<Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>>;
    title: string;
    details: { label: string, value?: string, node?: ReactNode }[];
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
            { details.map( ( { label, value, node } ) => (
                <Fragment key={ label }>
                    <small className={ "text-xs text-accent-foreground" }>{ label }</small>
                    { !node ? <p className={ "text-sm" }>{ value }</p> : node }
                </Fragment>
            ) ) }
        </div>
    </div>
}

type EntityCompactProps = CompactProps & {
    delay?: number;
    children: ReactNode;
}

function EntityCompact( { delay = 200, children, ...props }: EntityCompactProps ) {
    const [ open, setOpen ] = useState( false );
    const timeout = useRef<Timeout | null>( null )

    const handleMouseEnter = useCallback( ( e: MouseEvent<HTMLButtonElement> | MouseEvent<HTMLDivElement> ) => {
        e.preventDefault();
        if ( timeout.current ) clearTimeout( timeout.current );
        timeout.current = setTimeout( () => {
            setOpen( true )
        }, delay );
    }, [ delay ] );

    const debouncedMouseEnterHandler = useDebounceCallback( handleMouseEnter, delay );

    const handleMouseLeave = useCallback( ( e: MouseEvent<HTMLButtonElement> | MouseEvent<HTMLDivElement> ) => {
        e.preventDefault();
        if ( timeout.current ) clearTimeout( timeout.current );
        setOpen( false );
    }, [] );
    const debouncedMouseLeaveHandler = useDebounceCallback( handleMouseLeave, delay );


    return <Popover open={ open } onOpenChange={ setOpen }>
        <PopoverTrigger
            asChild
            onMouseEnter={ debouncedMouseEnterHandler }
            onMouseLeave={ debouncedMouseLeaveHandler }
            onClick={ ( e ) => e.preventDefault() }
        >
            { children }
        </PopoverTrigger>
        <PopoverContent
            className={ "p-0" }
            onMouseEnter={ debouncedMouseEnterHandler }
            onMouseLeave={ debouncedMouseLeaveHandler }
        >
            <Compact { ...props }/>
        </PopoverContent>
    </Popover>
}

export {
    type CompactProps,
    Compact,
    EntityCompact
}