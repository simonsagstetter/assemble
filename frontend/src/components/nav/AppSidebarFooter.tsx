/*
 * assemble
 * AppSidebarFooter.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client"

import {
    Avatar,
    AvatarFallback,
} from "@/components/ui/avatar"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    useSidebar,
} from "@/components/ui/sidebar"
import { CircleUserRoundIcon, EllipsisVerticalIcon, LogOutIcon } from "lucide-react";
import { User } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { submitLogout } from "@/services/rest/auth/auth";
import { toast } from "sonner";
import { LOGIN_PATH } from "@/config/auth/auth.config";
import { useRouter } from "@bprogress/next/app";

type AppSidebarFooterProps = {
    userDetails: User
}

export default function AppSidebarFooter( { userDetails }: AppSidebarFooterProps ) {
    const router = useRouter();
    const { firstname, lastname, username, email, fullname } = userDetails;
    const { isMobile } = useSidebar(),
        initials = firstname.substring( 0, 1 ) + lastname.substring( 0, 1 );

    const handleNavigate = ( url: string ) => {
        router.push( url );
    }

    const handleSubmitLogout = async () => {
        try {
            await submitLogout();
            toast.success( "Logout successful" );
            router.push( LOGIN_PATH );
        } catch {
            toast.error( "Logout failed" );
        }
    }

    return (
        <SidebarMenu>
            <SidebarMenuItem>
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <SidebarMenuButton
                            size="lg"
                            className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
                        >
                            <Avatar className="h-8 w-8 rounded-lg grayscale">
                                <AvatarFallback className="rounded-lg">{ initials }</AvatarFallback>
                            </Avatar>
                            <div className="grid flex-1 text-left text-sm leading-tight">
                                <span
                                    className="truncate font-medium">{ fullname }</span>
                                <span className="text-muted-foreground truncate text-xs">
                  { email }
                </span>
                            </div>
                            <EllipsisVerticalIcon className="ml-auto size-4"/>
                        </SidebarMenuButton>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                        className="w-(--radix-dropdown-menu-trigger-width) min-w-56 rounded-lg"
                        side={ isMobile ? "bottom" : "right" }
                        align="end"
                        sideOffset={ 4 }
                    >
                        <DropdownMenuLabel className="p-0 font-normal">
                            <div className="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                                <Avatar className="h-8 w-8 rounded-lg">
                                    <AvatarFallback className="rounded-lg">{ initials }</AvatarFallback>
                                </Avatar>
                                <div className="grid flex-1 text-left text-sm leading-tight">
                                    <span className="truncate font-medium">{ username }</span>
                                    <span className="text-muted-foreground truncate text-xs">
                                        { email }
                                    </span>
                                </div>
                            </div>
                        </DropdownMenuLabel>
                        <DropdownMenuSeparator/>
                        <DropdownMenuGroup>
                            <DropdownMenuItem onSelect={ () => handleNavigate( "/app/account" ) }>
                                <CircleUserRoundIcon/>
                                Account
                            </DropdownMenuItem>
                        </DropdownMenuGroup>
                        <DropdownMenuSeparator/>
                        <DropdownMenuItem onSelect={ handleSubmitLogout }>
                            <LogOutIcon/> Logout
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </SidebarMenuItem>
        </SidebarMenu>
    )
}