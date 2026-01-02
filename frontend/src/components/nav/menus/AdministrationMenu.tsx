/*
 * assemble
 * AdministrationMenu.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client"

import {
    SidebarGroup,
    SidebarGroupLabel,
    SidebarMenu,
} from "@/components/ui/sidebar"

import MenuItem from "@/components/nav/menus/MenuItem";
import { ListIcon, PlusIcon, UserIcon } from "lucide-react";

const userActions = [
    {
        label: "View All",
        href: "/app/admin/users",
        Icon: ListIcon
    },
    {
        label: "New",
        href: "/app/admin/users/create",
        Icon: PlusIcon
    }
]

export function AdministrationMenu() {
    return (
        <SidebarGroup className="group-data-[collapsible=icon]:hidden">
            <SidebarGroupLabel>Administration</SidebarGroupLabel>
            <SidebarMenu>
                <MenuItem
                    label={ "Users" }
                    href={ "/app/admin/users" }
                    Icon={ UserIcon }
                    actions={ userActions }
                ></MenuItem>
            </SidebarMenu>
        </SidebarGroup>

    )
}