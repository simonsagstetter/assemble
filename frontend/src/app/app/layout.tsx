/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { ReactNode, Suspense } from "react";
import { getUserDetails } from "@/services/rest/auth/auth";
import Loading from "@/components/custom-ui/Loading";
import { SidebarInset, SidebarProvider } from "@/components/ui/sidebar";
import AppSidebar from "@/components/nav/AppSidebar";
import { SiteHeader } from "@/components/nav/SiteHeader";

type AppLayoutProps = {
    children: Readonly<ReactNode>
}

export default async function AppLayout( { children }: AppLayoutProps ) {
    const userDetails = await getUserDetails();

    return <Suspense fallback={ <Loading/> }>
        <SidebarProvider>
            <AppSidebar userDetails={ userDetails }/>
            <SidebarInset>
                <SiteHeader/>
                { children }
            </SidebarInset>
        </SidebarProvider>
    </Suspense>
}