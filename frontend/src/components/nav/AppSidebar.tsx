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
import useUserContext from "@/hooks/use-user";
import { TimetrackingMenu } from "@/components/nav/menus/TimetrackingMenu";
import { AdministrationMenu } from "@/components/nav/menus/AdministrationMenu";
import AppSidebarHeader from "@/components/nav/AppSidebarHeader";

type AppSidebarProps = { userDetails: User }

export default function AppSidebar( { userDetails }: AppSidebarProps ) {
    const { setState, isAdmin, isSuperUser } = useUserContext();
    useEffect( () => setState( userDetails ), [ userDetails, setState ] );

    return (
        <Sidebar collapsible="offcanvas" variant="inset">
            <SidebarHeader>
                <AppSidebarHeader/>
            </SidebarHeader>
            <SidebarContent>
                <TimetrackingMenu/>
                { isAdmin || isSuperUser ? <AdministrationMenu/> : null }
            </SidebarContent>
            <SidebarFooter>
                <AppSidebarFooter userDetails={ userDetails }/>
            </SidebarFooter>
        </Sidebar>
    )
}