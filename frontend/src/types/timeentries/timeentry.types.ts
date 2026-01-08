/*
 * assemble
 * timeentry.types.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { z } from "zod";
import { toInstant } from "@/utils/datetime";
import { optionalString } from "@/utils/zod";
import { toDuration } from "@/utils/duration";
import { format } from "date-fns";

const TimeEntrySchema = z.object( {
    projectId: z.string().trim().min( 1, "Project is required" ),
    employeeId: z.string().trim().min( 1, "Employee is required" ),
    date: z.date(),
    startTime: optionalString( z.iso.time() ),
    endTime: optionalString( z.iso.time() ),
    totalTime: z.iso.time( { message: "This field is required" } ),
    pauseTime: z.iso.time( { message: "This field is required" } ),
    description: z.string().min( 10, "Description must be at least 10 characters" )
        .max( 1000, "Description must be at most 1000 characters" )
} ).transform( data => {
    return {
        ...data,

        date: format( data.date, "yyyy-MM-dd" ),

        startTime: toInstant( data.date, data.startTime ),
        endTime: toInstant( data.date, data.endTime ),

        totalTime: toDuration( data.totalTime ),
        pauseTime: toDuration( data.pauseTime ),
    };
} )

type TimeEntryFormInput = z.input<typeof TimeEntrySchema>;
type TimeEntryFormOutput = z.output<typeof TimeEntrySchema>;

export {
    type TimeEntryFormInput,
    type TimeEntryFormOutput,
    TimeEntrySchema,
}