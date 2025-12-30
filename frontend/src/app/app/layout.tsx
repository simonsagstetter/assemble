/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { ReactNode } from "react";
import { getUserDetails } from "@/services/rest/auth/auth";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar";
import AppSidebar from "@/components/nav/AppSidebar";
import { SiteHeader } from "@/components/nav/SiteHeader";

type AppLayoutProps = {
    children: Readonly<ReactNode>,
    modal: Readonly<ReactNode>
}

export default async function AppLayout( { children, modal }: AppLayoutProps ) {
    const userDetails = await getUserDetails();

    return <SidebarProvider>
        <AppSidebar userDetails={ userDetails }/>
        <SidebarInset>
            <SiteHeader/>
            { children }
            { modal }
        </SidebarInset>
    </SidebarProvider>
}