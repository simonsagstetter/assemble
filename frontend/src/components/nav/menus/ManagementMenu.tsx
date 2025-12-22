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
    MoreHorizontal,
    ListIcon, PlusIcon, IdCardIcon,
} from "lucide-react"

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
    SidebarGroup,
    SidebarGroupLabel,
    SidebarMenu,
    SidebarMenuAction,
    SidebarMenuButton,
    SidebarMenuItem,
    useSidebar,
} from "@/components/ui/sidebar"
import Link from "next/link";
import { useRouter } from "@bprogress/next/app";

export default function ManagementMenu() {
    const router = useRouter();
    const { isMobile } = useSidebar()

    return (
        <SidebarGroup className="group-data-[collapsible=icon]:hidden">
            <SidebarGroupLabel>Management</SidebarGroupLabel>
            <SidebarMenu>
                <SidebarMenuItem>
                    <SidebarMenuButton asChild>
                        <Link href="/app/manage/employees">
                            <IdCardIcon></IdCardIcon>
                            <span>Employees</span>
                        </Link>
                    </SidebarMenuButton>
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <SidebarMenuAction showOnHover>
                                <MoreHorizontal/>
                                <span className="sr-only">More</span>
                            </SidebarMenuAction>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent
                            className="w-48 rounded-lg"
                            side={ isMobile ? "bottom" : "right" }
                            align={ isMobile ? "end" : "start" }
                        >
                            <DropdownMenuItem onSelect={ () => router.push( "/app/manage/employees" ) }>
                                <ListIcon className="text-muted-foreground"/>
                                <span>View All</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem onSelect={ () => router.push( "/app/manage/employees/create" ) }>
                                <PlusIcon className="text-muted-foreground"/>
                                <span>New</span>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </SidebarMenuItem>
            </SidebarMenu>
        </SidebarGroup>
    )
}
