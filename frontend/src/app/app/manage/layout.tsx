/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Metadata } from "next";
import { ReactNode } from "react";
import RequireRole from "@/components/auth/RequireRole";
import { UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";

export const metadata: Metadata = {
    title: "Management | Assemble"
}

type ManageLayoutProps = {
    children: Readonly<ReactNode>,
    modal: Readonly<ReactNode>
}

export default function ManageLayout( { children, modal }: ManageLayoutProps ) {
    return <RequireRole roles={ [ UserRolesItem.MANAGER, UserRolesItem.ADMIN, UserRolesItem.SUPERUSER ] }>
        { children }
        { modal }
    </RequireRole>;
}