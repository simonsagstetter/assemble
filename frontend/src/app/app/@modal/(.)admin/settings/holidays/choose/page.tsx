/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { useGetImportedYearsSuspense } from "@/api/rest/generated/query/holiday-import/holiday-import";
import HolidayChooseYearForm from "@/components/admin/settings/holidays/HolidayChooseYearForm";
import ModalHeader from "@/components/custom-ui/ModalHeader";
import dynamic from "next/dynamic";

function ChooseHolidayYearModal() {
    const { data: years } = useGetImportedYearsSuspense();
    return <ModalHeader title={ "Choose a Year" } description={ "Select a year to view holidays" }
                        entity={ "Holiday" }>
        <HolidayChooseYearForm years={ years }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( ChooseHolidayYearModal ), { ssr: false } );