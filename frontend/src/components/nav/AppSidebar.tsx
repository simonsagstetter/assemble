/*
 * assemble
 * AppSidebar.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { Sidebar, SidebarContent, SidebarFooter, SidebarHeader } from "@/components/ui/sidebar";
import AppSidebarFooter from "@/components/nav/AppSidebarFooter";
import useUserContext from "@/hooks/use-user";
import { TimetrackingMenu } from "@/components/nav/menus/TimetrackingMenu";
import { AdministrationMenu } from "@/components/nav/menus/AdministrationMenu";
import AppSidebarHeader from "@/components/nav/AppSidebarHeader";
import ManagementMenu from "@/components/nav/menus/ManagementMenu";


export default function AppSidebar() {
    const { isManager, isAdmin, isSuperUser, user } = useUserContext();

    return (
        <Sidebar collapsible="offcanvas" variant="inset">
            <SidebarHeader>
                <AppSidebarHeader/>
            </SidebarHeader>
            <SidebarContent>
                <TimetrackingMenu/>
                { isManager || isAdmin || isSuperUser ? <ManagementMenu/> : null }
                { isAdmin || isSuperUser ? <AdministrationMenu/> : null }
            </SidebarContent>
            <SidebarFooter>
                <AppSidebarFooter userDetails={ user }/>
            </SidebarFooter>
        </Sidebar>
    )
}