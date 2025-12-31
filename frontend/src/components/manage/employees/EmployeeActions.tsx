/*
 * assemble
 * EmployeeActions.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { ComponentProps, ForwardRefExoticComponent, RefAttributes } from "react";
import { DropdownMenuContentProps } from "@radix-ui/react-dropdown-menu";
import { useRouter } from "@bprogress/next/app";
import {
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuSeparator
} from "@/components/ui/dropdown-menu";
import {
    IdCardIcon, LayersIcon, PencilIcon,
    TrashIcon,
    UserIcon,
} from "lucide-react";

export default function EmployeeActions(
    { id, hasUser, tableActions = false, ...props }
    :
        { id: string, tableActions?: boolean, hasUser: boolean }
        & ComponentProps<ForwardRefExoticComponent<DropdownMenuContentProps & RefAttributes<HTMLDivElement>>>
) {
    const router = useRouter();
    return <DropdownMenuContent { ...props }>
        <DropdownMenuGroup>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/manage/employees/${ id }/assign` ) }>
                <LayersIcon/> Assign
            </DropdownMenuItem>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/manage/employees/${ id }/user` ) }>
                <UserIcon/> { hasUser ? "Update User" : "Connect User" }
            </DropdownMenuItem>
        </DropdownMenuGroup>
        <DropdownMenuSeparator/>
        { tableActions ?
            <>
                <DropdownMenuGroup>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/manage/employees/${ id }` ) }>
                        <IdCardIcon/> Details
                    </DropdownMenuItem>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/manage/employees/${ id }/edit` ) }>
                        <PencilIcon/> Edit
                    </DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
            </>
            : null
        }
        <DropdownMenuGroup>
            <DropdownMenuItem variant="destructive"
                              onSelect={ () => router.push( `/app/manage/employees/${ id }/delete` ) }>
                <TrashIcon/>
                Delete
            </DropdownMenuItem>
        </DropdownMenuGroup>
    </DropdownMenuContent>
}