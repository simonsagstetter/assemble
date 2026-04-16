/*
 * assemble
 * utils.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { UseFormReturn, useWatch } from "react-hook-form";
import { type TimeEntryFormInput, type TimeEntryFormOutput } from "@/types/timeentries/timeentry.types";
import { useEffect, useState } from "react";
import { HHmmToMs, msToHHmm } from "@/utils/duration";
import { useGetOwnTimeEntries } from "@/api/rest/generated/query/timeentries/timeentries";
import { format } from "date-fns";

function useTimeEntryCalculations( form: UseFormReturn<TimeEntryFormInput, unknown, TimeEntryFormOutput> ) {
    const [ total, setTotal ] = useState<string>( "00:00" );
    const [ startTime, endTime ] = useWatch( {
        name: [ "startTime", "endTime" ],
        control: form.control
    } );
    const [ duration, pauseTime ] = useWatch( {
        name: [ "totalTime", "pauseTime" ],
        control: form.control
    } );

    useEffect( () => {
        if ( startTime && endTime ) {
            const startTimeMs = HHmmToMs( startTime );
            const endTimeMs = HHmmToMs( endTime );

            if ( startTimeMs > endTimeMs ) {
                form.setError( "endTime", {
                    message: "End time must be after start time",
                    type: "manual"
                } );
            } else {
                form.clearErrors( "endTime" );
                const calculatedDuration = msToHHmm( endTimeMs - startTimeMs ) + ":00";
                form.setValue( "totalTime", calculatedDuration );
            }
        }
    }, [ startTime, endTime, form ] );

    useEffect( () => {

        const stateCallback = ( total: string ) => setTotal( total );

        if ( duration && pauseTime ) {
            const totalMs = HHmmToMs( duration );
            const pauseMs = HHmmToMs( pauseTime );

            if ( totalMs < pauseMs ) {
                form.setError( "pauseTime", {
                    message: "Pause time must be less than total time",
                    type: "manual"
                } );
            } else {
                form.clearErrors( "pauseTime" );
                stateCallback( msToHHmm( totalMs - pauseMs ) );
            }
        }
    }, [ duration, pauseTime, form ] );

    return { total };
}

function useRelatedTimeEntries( form: UseFormReturn<TimeEntryFormInput, unknown, TimeEntryFormOutput> ) {
    const [ date ] = useWatch( {
        name: [ "date" ],
        control: form.control
    } );

    return useGetOwnTimeEntries( {
        exactDate: format( date, "yyyy-MM-dd" )
    } )
}

export {
    useTimeEntryCalculations,
    useRelatedTimeEntries,
}