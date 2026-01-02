/*
 * assemble
 * MenuItem.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { SidebarMenuButton, SidebarMenuItem } from "@/components/ui/sidebar";
import Link from "next/link";
import MenuItemActions from "@/components/nav/menus/MenuItemActions";
import { MenuItem as MenuItemProps } from "@/types/nav/nav.types";
import { usePathname } from "next/navigation";


export default function MenuItem(
    { href, label, Icon, actions }: MenuItemProps
) {
    const pathname = usePathname();
    const isActive = pathname.startsWith( href );
    return <SidebarMenuItem>
        <SidebarMenuButton asChild isActive={ isActive }>
            <Link href={ href }>
                <Icon></Icon>
                <span>{ label }</span>
            </Link>
        </SidebarMenuButton>
        { actions ?
            <MenuItemActions actions={ actions } isActive={ isActive }/>
            : null }
    </SidebarMenuItem>
}
