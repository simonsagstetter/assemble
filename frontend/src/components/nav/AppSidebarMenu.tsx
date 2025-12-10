/*
 * assemble
 * AppSidebarMenu.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client"

import { CalendarIcon, ChevronRight } from "lucide-react"

import {
    Collapsible,
    CollapsibleContent,
    CollapsibleTrigger,
} from "@/components/ui/collapsible"
import {
    SidebarGroup,
    SidebarGroupLabel,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarMenuSub,
    SidebarMenuSubButton,
    SidebarMenuSubItem,
} from "@/components/ui/sidebar"
import Link from "next/link";

export function AppSidebarMenu() {
    const items = [
        {
            title: "Working Times",
            url: "#",
            icon: CalendarIcon,
            isActive: true,
            items: [
                {
                    title: "Calendar",
                    url: "/app/timetracking/calendar",
                },
                {
                    title: "List View",
                    url: "/app/timetracking/list",
                },
            ],
        }
    ];
    return (
        <SidebarGroup>
            <SidebarGroupLabel>Timetracking</SidebarGroupLabel>
            <SidebarMenu>
                { items.map( ( item ) => (
                    <Collapsible
                        key={ item.title }
                        asChild
                        defaultOpen={ item.isActive }
                        className="group/collapsible"
                    >
                        <SidebarMenuItem>
                            <CollapsibleTrigger asChild>
                                <SidebarMenuButton tooltip={ item.title }>
                                    { item.icon && <item.icon/> }
                                    <span>{ item.title }</span>
                                    <ChevronRight
                                        className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90"/>
                                </SidebarMenuButton>
                            </CollapsibleTrigger>
                            <CollapsibleContent>
                                <SidebarMenuSub>
                                    { item.items?.map( ( subItem ) => (
                                        <SidebarMenuSubItem key={ subItem.title }>
                                            <SidebarMenuSubButton asChild>
                                                <Link href={ subItem.url }>
                                                    <span>{ subItem.title }</span>
                                                </Link>
                                            </SidebarMenuSubButton>
                                        </SidebarMenuSubItem>
                                    ) ) }
                                </SidebarMenuSub>
                            </CollapsibleContent>
                        </SidebarMenuItem>
                    </Collapsible>
                ) ) }
            </SidebarMenu>
        </SidebarGroup>
    )
}