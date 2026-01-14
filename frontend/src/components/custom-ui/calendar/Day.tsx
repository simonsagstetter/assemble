/*
 * assemble
 * Day.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { PlusIcon } from "lucide-react";
import Event from "@/components/custom-ui/calendar/Event";
import { format } from "date-fns";
import { msToHHmm } from "@/utils/duration";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import useCalendar from "@/hooks/useCalendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { EventData } from "@/types/calendar/calendar.types";
import { ContextMenu, ContextMenuContent, ContextMenuItem, ContextMenuTrigger } from "@/components/ui/context-menu";
import { useRouter } from "@bprogress/next/app";
import { Button } from "@/components/ui/button";

type DayProps = {
    day: number;
    date: Date;
    dayEvents: EventData[];
    newLink: string;
    weekTotal?: number;
    todayTotal?: number;
    isSunday?: boolean;
    isSelected?: boolean;
    isReadOnly?: boolean;
    isHoliday?: boolean;
}

export default function Day(
    {
        day,
        date,
        dayEvents,
        newLink,
        weekTotal,
        todayTotal = 0,
        isSunday = false,
        isSelected = false,
        isReadOnly = false,
        isHoliday = false,
    }: DayProps
) {
    const router = useRouter();
    const { setSelectedDate, settings } = useCalendar();

    const handleNew = () => {
        if ( !settings.disabled ) router.push( newLink + "?date=" + format( date, "yyyy-MM-dd" ) );
    }
    return (
        <ContextMenu>
            <Tooltip>
                <TooltipTrigger asChild>
                    <ContextMenuTrigger asChild>
                        <div
                            className={ `group relative flex flex-col gap-1.5 p-1.5 hover:bg-gray-50 max-md:max-h-202 md:gap-1 md:p-2 before:pointer-events-none before:absolute before:inset-0 before:border-b before:border-gray-200 ${ isSunday ? "before:border-r-0" : "before:border-r" } ${ isReadOnly || isHoliday ? "bg-gray-50 cursor-not-allowed" : "bg-white cursor-pointer " }` }
                            onClick={ () => !isReadOnly && !isHoliday ? setSelectedDate( date ) : null }>
                            { !settings.disabled && !isReadOnly && !isHoliday ?
                                <div className="absolute right-1.5 bottom-1.5 z-10 hidden group-hover:inline-flex">
                                    <Button variant={ "ghost" } onClick={ handleNew }
                                            className="group relative inline-flex cursor-pointer items-center justify-center whitespace-nowrap size-7 text-gray-500 rounded-lg bg-white shadow-sm ring-1 ring-gray-300 ring-inset hover:bg-gray-50">
                                        <PlusIcon size={ 20 }/>
                                    </Button>
                                </div>
                                : null
                            }
                            <span
                                className={
                                    `flex flex-col size-6 items-center justify-center rounded-full text-xs font-semibold ${ !isReadOnly && isSelected ? 'bg-primary text-white' : '' } ${ isReadOnly ? "text-gray-400" : "text-gray-900" }`
                                }>
                        { day }
                    </span>
                            <div className="flex gap-1 max-md:pl-1 md:flex-col">
                                { dayEvents.slice( 0, 3 ).map( ( event ) => (
                                    <Event
                                        key={ event.id }
                                        isReadOnly={ isReadOnly }
                                        event={ event }/>
                                ) ) }
                            </div>
                            { dayEvents.length > 3 ? <Popover>
                                <PopoverTrigger asChild className={ "max-md:hidden" }>
                                    <div className="truncate text-xs font-semibold text-gray-500 max-md:pl-1">
                                        { dayEvents.length - 3 } more...
                                    </div>
                                </PopoverTrigger>
                                <PopoverContent className={ "max-md:hidden" }>
                                    { dayEvents.map( ( event ) => (
                                        <Event
                                            key={ event.id }
                                            isReadOnly={ isReadOnly }
                                            event={ event }
                                        />
                                    ) ) }
                                </PopoverContent>
                            </Popover> : null }
                        </div>
                    </ContextMenuTrigger>
                </TooltipTrigger>
                { !isReadOnly && !isHoliday && ( todayTotal || weekTotal ) ?
                    <TooltipContent className={ "max-md:hidden" }>
                        { weekTotal ? <p>{ "Week: " + msToHHmm( weekTotal ) }</p> : null }
                        { todayTotal ? <p>{ "Today: " + msToHHmm( todayTotal ) }</p> : null }
                    </TooltipContent> : null }
            </Tooltip>
            { !settings.disabled && !isReadOnly && !isHoliday ?
                <ContextMenuContent className={ "max-md:hidden" }>
                    <ContextMenuItem onSelect={ handleNew }><PlusIcon/>New</ContextMenuItem>
                </ContextMenuContent>
                :
                null
            }
        </ContextMenu>
    )
}