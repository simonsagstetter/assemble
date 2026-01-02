/*
 * assemble
 * ManagementMenu.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client"

import {
    PlusIcon, IdCardIcon, LayersIcon,
} from "lucide-react";

import {
    SidebarGroup,
    SidebarGroupLabel,
    SidebarMenu,
} from "@/components/ui/sidebar"
import MenuItem from "@/components/nav/menus/MenuItem";

const employeeActions = [
    {
        label: "View All",
        href: "/app/manage/employees",
        Icon: IdCardIcon
    },
    {
        label: "New",
        href: "/app/manage/employees/create",
        Icon: PlusIcon
    }
]

const projectActions = [
    {
        label: "View All",
        href: "/app/manage/projects",
        Icon: LayersIcon
    },
    {
        label: "New",
        href: "/app/manage/projects/create",
        Icon: PlusIcon
    }
]

export default function ManagementMenu() {

    return (
        <SidebarGroup className="group-data-[collapsible=icon]:hidden">
            <SidebarGroupLabel>Management</SidebarGroupLabel>
            <SidebarMenu>
                <MenuItem href={ "/app/manage/projects" }
                          label={ "Projects" }
                          Icon={ LayersIcon }
                          actions={ projectActions }/>
                <MenuItem href={ "/app/manage/employees" }
                          label={ "Employees" }
                          Icon={ IdCardIcon }
                          actions={ employeeActions }/>
            </SidebarMenu>
        </SidebarGroup>
    )
}
