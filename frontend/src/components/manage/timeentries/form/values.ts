/*
 * assemble
 * values.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { parseISO } from "date-fns";
import { isoDurationToMs, msToHHmm } from "@/utils/duration";

export function getTimeEntryDefaultValues(
    timeentry?: TimeEntryDTO,
    employeeId?: string,
    dateParam?: string
) {
    const isNew = timeentry === undefined;

    return {
        projectId: timeentry?.project.id ?? "",
        employeeId: isNew ? ( employeeId ?? "" ) : ( timeentry?.employee.id ?? "" ),
        date: isNew
            ? ( dateParam ? parseISO( dateParam ) : new Date() )
            : new Date( timeentry!.date ),
        startTime: "",
        endTime: "",
        totalTime: isNew ? "00:00:00" : msToHHmm( isoDurationToMs( timeentry!.totalTime ) ),
        pauseTime: isNew ? "00:00:00" : msToHHmm( isoDurationToMs( timeentry!.pauseTime ) ),
        description: isNew ? "" : timeentry!.description
    };
}