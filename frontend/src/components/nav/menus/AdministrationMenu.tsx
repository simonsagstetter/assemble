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

import { ChevronRight, UserIcon } from "lucide-react"

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

export function AdministrationMenu() {
    const items = [
        {
            title: "Users",
            url: "/app/admin/users",
            icon: UserIcon,
            isActive: true,
            items: [
                {
                    title: "List View",
                    url: "/app/admin/users",
                },
            ],
        }
    ];
    return (
        <SidebarGroup>
            <SidebarGroupLabel>Administration</SidebarGroupLabel>
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