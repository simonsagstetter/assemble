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

export function useTimeEntryCalculations( form: UseFormReturn<TimeEntryFormInput, unknown, TimeEntryFormOutput> ) {
    const [ total, setTotal ] = useState<string>( "00:00" );
    const [ startTime, endTime ] = useWatch( {
        name: [ "startTime", "endTime" ],
        control: form.control
    } );
    const [ duration, pauseTime ] = useWatch( {
        name: [ "totalTime", "pauseTime" ],
        control: form.control
    } );

    // Start/End Time Validation
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

    // Duration/Pause Time Validation
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