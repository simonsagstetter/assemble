/*
 * assemble
 * MonthFooter.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import useCalendar from "@/hooks/useCalendar";
import { format } from "date-fns";
import { mapTimeEntriesToDayEvents } from "@/utils/calendar/timeentries";
import { msToHHmm } from "@/utils/duration";
import { colorClasses } from "@/config/calendar/calendar.config";
import { useRouter } from "@bprogress/next/app";
import {
    ContextMenu,
    ContextMenuContent,
    ContextMenuItem,
    ContextMenuSeparator,
    ContextMenuTrigger
} from "@/components/ui/context-menu";
import { PencilIcon, TrashIcon } from "lucide-react";
import { Fragment } from "react";

export default function MonthFooter() {
    const router = useRouter();
    const { selectedDate, events } = useCalendar();
    const key = format( selectedDate, "yyyy-MM-dd" );
    const selectedDayEvents = mapTimeEntriesToDayEvents( events[ key as keyof typeof events ] || [] );

    return <div className="border-t border-gray-200 px-4 py-5 md:hidden"><h3
        className="text-sm font-semibold text-gray-950">{ format( selectedDate, "PPPP" ) }</h3>
        <div className="mt-4 flex flex-col gap-1.5">
            { selectedDayEvents && selectedDayEvents.length > 0 ? selectedDayEvents.map( ( event, index ) => (
                <Fragment key={ event.id }>
                    <ContextMenu>
                        <ContextMenuTrigger asChild>
                            <div key={ index }
                                 onClick={ () => router.push( event.updateLink ) }
                                 className={ `flex w-full cursor-pointer items-center gap-1 rounded-md px-2 py-1 ring-1 ring-inset ${ colorClasses[ event.color ] }` }>
                                <div className="flex w-full items-center justify-between gap-0.5">
                        <span
                            className="flex-1 truncate text-xs font-semibold text-utility-green-700">{ event.title }</span>
                                    <time className="text-xs text-utility-green-600">{ msToHHmm( event.time ) }</time>
                                </div>
                            </div>
                        </ContextMenuTrigger>
                        <ContextMenuContent>
                            <ContextMenuItem>
                                <PencilIcon/> Edit
                            </ContextMenuItem>
                            <ContextMenuSeparator/>
                            <ContextMenuItem variant={ "destructive" }>
                                <TrashIcon/> Delete
                            </ContextMenuItem>
                        </ContextMenuContent>
                    </ContextMenu>
                </Fragment>

            ) ) : <p>No timeentries on this date.</p> }
        </div>
    </div>
}