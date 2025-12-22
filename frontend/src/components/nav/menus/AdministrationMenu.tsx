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

import { ListIcon, MoreHorizontal, PlusIcon, UserIcon } from "lucide-react"

import {
    SidebarGroup,
    SidebarGroupLabel,
    SidebarMenu, SidebarMenuAction,
    SidebarMenuButton,
    SidebarMenuItem,
    useSidebar,
} from "@/components/ui/sidebar"
import Link from "next/link";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { useRouter } from "@bprogress/next/app";

export function AdministrationMenu() {
    const router = useRouter();
    const { isMobile } = useSidebar();
    return (
        <SidebarGroup className="group-data-[collapsible=icon]:hidden">
            <SidebarGroupLabel>Administration</SidebarGroupLabel>
            <SidebarMenu>
                <SidebarMenuItem>
                    <SidebarMenuButton asChild>
                        <Link href="/app/admin/users">
                            <UserIcon></UserIcon>
                            <span>Users</span>
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
                            <DropdownMenuItem onSelect={ () => router.push( "/app/admin/users" ) }>
                                <ListIcon className="text-muted-foreground"/>
                                <span>View All</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem onSelect={ () => router.push( "/app/admin/users/create" ) }>
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