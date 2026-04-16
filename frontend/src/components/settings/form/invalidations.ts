/*
 * assemble
 * invalidations.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import {
    AppSettingsDTO,
    AppSettingsDTOHolidaySubdivisionCode
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { InvalidateQueryFilters } from "@tanstack/react-query";
import { getGetAppSettingsQueryKey } from "@/api/rest/generated/query/app-settings/app-settings";
import {
    getGetCompanySubdivisionCodeQueryKey,
    getGetHolidaysByYearAndSubdivisionCodeQueryKey
} from "@/api/rest/generated/query/holidays/holidays";
import { format } from "date-fns";
import { getGetImportedYearsQueryKey } from "@/api/rest/generated/query/holiday-import/holiday-import";

const getUpdateAppSettingsInvalidations = ( data: AppSettingsDTO ): Array<InvalidateQueryFilters> =>
    [
        {
            queryKey: getGetAppSettingsQueryKey(),
            refetchType: "all"
        },
        {
            queryKey: getGetHolidaysByYearAndSubdivisionCodeQueryKey( {
                subdivisionCode: "DE-" + data.holidaySubdivisionCode,
                year: format( new Date(), "yyyy" )
            } ),
            refetchType: "all"
        },
        {
            queryKey: getGetCompanySubdivisionCodeQueryKey(),
            refetchType: "all"
        }
    ];

const getImportHolidaysInvalidations = ( year: string ): Array<InvalidateQueryFilters> => {
    return [
        ...Object.keys( AppSettingsDTOHolidaySubdivisionCode )
            .map( code => (
                {
                    queryKey: getGetHolidaysByYearAndSubdivisionCodeQueryKey( {
                        subdivisionCode: "DE-" + code,
                        year
                    } ),
                    refetchType: "all"
                } satisfies InvalidateQueryFilters
            ) ),
        {
            queryKey: getGetImportedYearsQueryKey(),
            refetchType: "all"
        }
    ]
}

export {
    getUpdateAppSettingsInvalidations,
    getImportHolidaysInvalidations
}