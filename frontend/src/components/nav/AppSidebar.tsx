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
import { User } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { Sidebar, SidebarContent, SidebarFooter, SidebarHeader } from "@/components/ui/sidebar";
import AppSidebarFooter from "@/components/nav/AppSidebarFooter";
import { useEffect } from "react";
import useUserContext from "@/hooks/useUserContext";
import { TimetrackingMenu } from "@/components/nav/menus/TimetrackingMenu";
import { AdministrationMenu } from "@/components/nav/menus/AdministrationMenu";
import AppSidebarHeader from "@/components/nav/AppSidebarHeader";
import ManagementMenu from "@/components/nav/menus/ManagementMenu";

type AppSidebarProps = { userDetails: User }

export default function AppSidebar( { userDetails }: AppSidebarProps ) {
    const { setState, isManager, isAdmin, isSuperUser } = useUserContext();
    useEffect( () => setState( userDetails ), [ userDetails, setState ] );

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
                <AppSidebarFooter userDetails={ userDetails }/>
            </SidebarFooter>
        </Sidebar>
    )
}