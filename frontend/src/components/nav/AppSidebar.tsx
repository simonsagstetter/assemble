/*
 * assemble
 * AppSidebar.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { User } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { Sidebar, SidebarFooter, SidebarHeader, SidebarContent } from "@/components/ui/sidebar";
import AppSidebarFooter from "@/components/nav/AppSidebarFooter";
import { AppSidebarMenu } from "@/components/nav/AppSidebarMenu";
import { AppSwitcher } from "@/components/nav/AppSwitcher";

type AppSidebarProps = { userDetails: User }

export default function AppSidebar( { userDetails }: AppSidebarProps ) {
    return (
        <Sidebar collapsible="offcanvas" variant="inset">
            <SidebarHeader>
                <AppSwitcher userRoles={ userDetails.roles }/>
            </SidebarHeader>
            <SidebarContent>
                <AppSidebarMenu/>
            </SidebarContent>
            <SidebarFooter>
                <AppSidebarFooter userDetails={ userDetails }/>
            </SidebarFooter>
        </Sidebar>
    )
}