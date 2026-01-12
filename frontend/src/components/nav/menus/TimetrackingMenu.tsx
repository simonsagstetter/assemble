/*
 * assemble
 * TimetrackingMenu.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client"

import { CalendarIcon, ChevronRight, PlaneTakeoff } from "lucide-react"

import { Collapsible, CollapsibleContent, CollapsibleTrigger, } from "@/components/ui/collapsible"
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
import { usePathname } from "next/navigation";

const items = [
    {
        title: "Time Entries",
        url: "/app/timetracking/calendar",
        icon: CalendarIcon,
        isActive: true,
        items: [
            {
                title: "Calendar",
                url: "/app/timetracking/calendar",
            }
        ],
    },
    {
        title: "Absence",
        url: "/app/timetracking/absence",
        icon: PlaneTakeoff,
        isActive: false,
        items: [
            {
                title: "Vacation",
                url: "/app",
            },
            {
                title: "Sickness",
                url: "/app",
            },
            {
                title: "Other",
                url: "/app",
            },
        ],
    },

];

export function TimetrackingMenu() {
    const pathname = usePathname();
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
                                <SidebarMenuButton tooltip={ item.title }
                                                   isActive={ pathname.startsWith( item.url ) }>
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