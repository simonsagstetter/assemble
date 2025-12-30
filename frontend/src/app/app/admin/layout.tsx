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
import { Metadata } from "next";
import RequireRole from "@/components/auth/RequireRole";
import { UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";

export const metadata: Metadata = {
    title: "Admin | Assemble"
}

type AdminLayoutProps = {
    children: Readonly<ReactNode>
}

export default function AdminLayout( { children }: AdminLayoutProps ) {
    return <RequireRole roles={ [ UserRolesItem.ADMIN, UserRolesItem.SUPERUSER ] }>
        { children }
    </RequireRole>;
}