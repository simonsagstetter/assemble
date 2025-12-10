"use client"

import * as React from "react"
import { ChevronsUpDown, ClockIcon, Settings, TablePropertiesIcon } from "lucide-react"

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    useSidebar,
} from "@/components/ui/sidebar"
import { UserRolesItem } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";

const TEAMS = [
    {
        name: "Timetracking",
        logo: ClockIcon,
        plan: "Company GmbH",
        roles: [ "EXTERNAL", "USER", "MANAGER", "ADMIN", "SUPERUSER" ]
    },
    {
        name: "Management",
        logo: TablePropertiesIcon,
        plan: "Company GmbH",
        roles: [ "MANAGER", "ADMIN", "SUPERUSER" ]
    },
    {
        name: "Administration",
        logo: Settings,
        plan: "Company GmbH",
        roles: [ "ADMIN", "SUPERUSER" ]
    },
];

type AppSwitcherProps = {
    userRoles: Readonly<UserRolesItem[]>;
}

export function AppSwitcher( { userRoles }: AppSwitcherProps ) {
    const { isMobile } = useSidebar()
    const [ activeTeam, setActiveTeam ] = React.useState( TEAMS[ 0 ] );

    if ( !activeTeam ) {
        return null
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
                            <div
                                className="bg-sidebar-primary text-sidebar-primary-foreground flex aspect-square size-8 items-center justify-center rounded-lg">
                                <activeTeam.logo className="size-4"/>
                            </div>
                            <div className="grid flex-1 text-left text-sm leading-tight">
                                <span className="truncate font-medium">{ activeTeam.name }</span>
                                <span className="truncate text-xs">{ activeTeam.plan }</span>
                            </div>
                            <ChevronsUpDown className="ml-auto"/>
                        </SidebarMenuButton>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                        className="w-(--radix-dropdown-menu-trigger-width) min-w-56 rounded-lg"
                        align="start"
                        side={ isMobile ? "bottom" : "right" }
                        sideOffset={ 4 }
                    >
                        <DropdownMenuLabel className="text-muted-foreground text-xs">
                            App
                        </DropdownMenuLabel>
                        { TEAMS.filter( team => userRoles.some( role => team.roles.includes( role ) ) )
                            .map( ( team ) => (
                                <DropdownMenuItem
                                    key={ team.name }
                                    onClick={ () => setActiveTeam( team ) }
                                    className="gap-2 p-2"
                                >
                                    <div className="flex size-6 items-center justify-center rounded-md border">
                                        <team.logo className="size-3.5 shrink-0"/>
                                    </div>
                                    { team.name }
                                </DropdownMenuItem>
                            ) ) }
                    </DropdownMenuContent>
                </DropdownMenu>
            </SidebarMenuItem>
        </SidebarMenu>
    )
}
