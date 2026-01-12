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
    SidebarMenu, SidebarMenuButton, SidebarMenuItem, SidebarMenuSub, SidebarMenuSubButton, SidebarMenuSubItem,
} from "@/components/ui/sidebar"

import MenuItem from "@/components/nav/menus/MenuItem";
import { ListIcon, PlusIcon, UserIcon, SettingsIcon, ChevronRight } from "lucide-react";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import Link from "next/link";
import { usePathname } from "next/navigation";

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

const collapsibleMenuItems = [
    {
        title: "Settings",
        url: "/app/admin/settings",
        icon: SettingsIcon,
        isActive: true,
        items: [
            {
                title: "General",
                url: "/app/admin/settings"
            },
            {
                title: "Holidays",
                url: "/app/admin/settings/holidays"
            }
        ]
    }
]

export function AdministrationMenu() {
    const pathname = usePathname();
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
                { collapsibleMenuItems.map( ( item ) => (
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