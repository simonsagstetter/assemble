/*
 * assemble
 * MenuItemActions.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { useRouter } from "@bprogress/next/app";
import { MenuItemAction } from "@/types/nav/nav.types";
import { SidebarMenuAction, useSidebar } from "@/components/ui/sidebar";
import { MoreHorizontal } from "lucide-react";

export default function MenuItemActions(
    { actions, isActive = false }:
    { actions: MenuItemAction[], isActive?: boolean }
) {
    const router = useRouter();
    const { isMobile } = useSidebar();
    return <DropdownMenu>
        <DropdownMenuTrigger asChild className={ isActive ? "hover:bg-primary border-none" : "border-none" }>
            <SidebarMenuAction showOnHover
                               className={ isActive ? "**:text-primary-foreground" : "" }>
                <MoreHorizontal/>
                <span className="sr-only">More</span>
            </SidebarMenuAction>
        </DropdownMenuTrigger>
        <DropdownMenuContent
            className="w-48 rounded-lg"
            side={ isMobile ? "bottom" : "right" }
            align={ isMobile ? "end" : "start" }
        >
            { actions.map( ( { label, href, Icon } ) => (
                <DropdownMenuItem key={ label } onSelect={ () => router.push( href ) }>
                    <Icon className={ "text-muted-foreground" }/>
                    <span>{ label }</span>
                </DropdownMenuItem>
            ) ) }
        </DropdownMenuContent>
    </DropdownMenu>

}