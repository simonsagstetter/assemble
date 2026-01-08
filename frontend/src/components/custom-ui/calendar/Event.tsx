/*
 * assemble
 * Event.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { msToHHmm } from "@/utils/duration";
import { colorClasses, colorMobileClasses } from "@/config/calendar/calendar.config";
import { EventData } from "@/types/calendar/calendar.types";
import { useRouter } from "@bprogress/next/app";
import {
    ContextMenu,
    ContextMenuTrigger,
    ContextMenuContent,
    ContextMenuItem,
    ContextMenuSeparator
} from "@/components/ui/context-menu";
import { PencilIcon, TrashIcon } from "lucide-react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { PopoverArrow } from "@radix-ui/react-popover";

type EventProps = {
    event: EventData;
    isReadOnly?: boolean;
}

export default function Event( { event, isReadOnly = false }: EventProps ) {
    const router = useRouter();

    const handleUpdate = () => {
        router.push( event.updateLink );
    }

    const handleDelete = () => {
        router.push( event.deleteLink );
    }

    return (
        <ContextMenu>
            <Popover>
                <PopoverTrigger asChild>
                    <ContextMenuTrigger asChild>
                        <div
                            className={ `py-0.5 ${ event.isMultiDay ? 'z-10' : '' } ${ isReadOnly ? "opacity-60" : "" }` }
                            style={ event.isMultiDay ? { width: 'calc(300% + 32px)' } : {} }>
                            <div
                                className={ "inline-flex size-2 items-center justify-center md:hidden" }>
                        <span className={
                            `size-1.5 rounded-full
                            ${ isReadOnly ? "bg-gray-500" : colorMobileClasses[ event.color ] }`
                        }></span>
                            </div>
                            <div
                                className={ `flex w-full cursor-pointer items-center gap-1 rounded-md px-2 py-1 ring-1 ring-inset max-md:hidden ${ isReadOnly ? "bg-gray-50 ring-gray-200 hover:bg-gray-100" : colorClasses[ event.color ] }` }>
                                <div className="flex w-full items-center justify-between gap-0.5">
                            <span
                                className={ `flex-1 truncate text-xs font-semibold ${ isReadOnly ? "text-gray-700" : "" }` }>
                                { event.title }
                            </span>
                                    { event.time !== 0 ?
                                        <time
                                            className={ `text-xs ${ isReadOnly ? "text-gray-600" : "" }` }>
                                            { msToHHmm( event.time ) }
                                        </time>
                                        : null
                                    }
                                </div>
                            </div>
                        </div>
                    </ContextMenuTrigger>
                </PopoverTrigger>
                { !isReadOnly ?
                    <PopoverContent className={ "py-2 max-md:hidden max-w-[200px] text-center" } arrowPadding={ 10 }>
                        <PopoverArrow className={ "fill-primary" }></PopoverArrow>
                        { event.tooltip }
                    </PopoverContent>
                    : null }
            </Popover>
            <ContextMenuContent>
                <ContextMenuItem onClick={ handleUpdate }>
                    <PencilIcon/>
                    Edit
                </ContextMenuItem>
                <ContextMenuSeparator/>
                <ContextMenuItem variant={ "destructive" } onClick={ handleDelete }>
                    <TrashIcon/>
                    Delete
                </ContextMenuItem>
            </ContextMenuContent>
        </ContextMenu>
    )
}