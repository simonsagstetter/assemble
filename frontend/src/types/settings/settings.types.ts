/*
 * assemble
 * settings.types.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { AppSettingsDTOHolidaySubdivisionCode } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { z } from "zod";
import { AddressSchema } from "@/types/employees/employee.types";

const SettingsSchema = z.object( {
    companyName: z.string().trim().min( 1, "Company name is required" ),
    holidaySubdivisionCode: z.enum( AppSettingsDTOHolidaySubdivisionCode ),
    companyAddress: AddressSchema
} )

type SettingsFormData = z.infer<typeof SettingsSchema>

const SubdivisionLabels = {
    BB: "Brandenburg",
    BE: "Berlin",
    BW: "Baden-Württemberg",
    BY: "Bayern",
    HB: "Bremen",
    HE: "Hessen",
    HH: "Hamburg",
    MV: "Mecklenburg-Vorpommern",
    NI: "Niedersachsen",
    NW: "Nordrhein-Westfalen",
    RP: "Rheinland-Pfalz",
    SH: "Schleswig-Holstein",
    SL: "Saarland",
    SN: "Sachsen",
    ST: "Sachsen-Anhalt",
    TH: "Thüringen",
} as const;

export {
    type SettingsFormData,
    SubdivisionLabels,
    SettingsSchema
}