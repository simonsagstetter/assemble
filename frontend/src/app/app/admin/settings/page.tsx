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

import { useGetAppSettingsSuspense } from "@/api/rest/generated/query/app-settings/app-settings";
import dynamic from "next/dynamic";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import SettingsInteractiveForm from "@/components/admin/settings/SettingsInteractiveForm";

function SettingsPage() {
    const { data: settings } = useGetAppSettingsSuspense();
    return <FormPageHeader title={ "Settings" } description={ "Manage the application settings." }
                           entity={ "Settings" }>
        <SettingsInteractiveForm settings={ settings }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( SettingsPage ), { ssr: false } );
