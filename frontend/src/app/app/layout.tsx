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

type AppLayoutProps = {
    children: Readonly<ReactNode>
}

export default async function AppLayout( { children }: AppLayoutProps ) {
    const userDetail = await getUserDetails();

    return <div>
        <h1>{ userDetail.fullname }</h1>
        { children }
    </div>
}