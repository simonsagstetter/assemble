/*
 * assemble
 * holiday.types.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { z } from "zod";

const HolidaySchema = z.object( {
    year: z.string(),
} )

const HolidayChooseYearSchema = z.object( { year: z.string() } );

type HolidayChooseYearFormData = z.infer<typeof HolidayChooseYearSchema>;

type HolidayFormData = z.infer<typeof HolidaySchema>

export {
    type HolidayFormData,
    type HolidayChooseYearFormData,
    HolidaySchema,
    HolidayChooseYearSchema
}