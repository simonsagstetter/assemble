/*
 * assemble
 * UserActions.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import {
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem, DropdownMenuSeparator,
} from "@/components/ui/dropdown-menu";
import {
    HatGlassesIcon,
    IdCardIcon, PencilIcon,
    RectangleEllipsisIcon, TrashIcon,
    UserCogIcon, UserIcon,
    UserLockIcon
} from "lucide-react";
import { useRouter } from "@bprogress/next/app";
import { ComponentProps, ForwardRefExoticComponent, RefAttributes } from "react";
import { DropdownMenuContentProps } from "@radix-ui/react-dropdown-menu";

export default function UserActions(
    { id, hasEmployee, tableActions = false, ...props }
    :
        { id: string, tableActions?: boolean, hasEmployee: boolean }
        & ComponentProps<ForwardRefExoticComponent<DropdownMenuContentProps & RefAttributes<HTMLDivElement>>>
) {
    const router = useRouter();
    return <DropdownMenuContent { ...props }>
        <DropdownMenuGroup>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/admin/users/${ id }/employee` ) }>
                <IdCardIcon/> { hasEmployee ? "Connect Employee" : "Update Employee" }
            </DropdownMenuItem>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/admin/users/${ id }/status` ) }>
                <UserLockIcon/> Update Status
            </DropdownMenuItem>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/admin/users/${ id }/roles` ) }>
                <UserCogIcon/> Update Roles
            </DropdownMenuItem>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/admin/users/${ id }/reset-password` ) }>
                <RectangleEllipsisIcon/> Reset Password
            </DropdownMenuItem>
        </DropdownMenuGroup>
        <DropdownMenuSeparator/>
        { tableActions ?
            <>
                <DropdownMenuGroup>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/admin/users/${ id }` ) }>
                        <UserIcon/> Details
                    </DropdownMenuItem>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/admin/users/${ id }/edit` ) }>
                        <PencilIcon/> Edit
                    </DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
            </>
            : null
        }

        <DropdownMenuGroup>
            <DropdownMenuItem className={ "text-yellow-500 focus:bg-yellow-500/10 focus:text-yellow-500" }>
                <HatGlassesIcon className={ "text-shadow-yellow-500" }></HatGlassesIcon> Impersonate
            </DropdownMenuItem>
        </DropdownMenuGroup>
        <DropdownMenuSeparator/>
        <DropdownMenuGroup>
            <DropdownMenuItem variant="destructive"
                              onSelect={ () => router.push( `/app/admin/users/${ id }/delete` ) }>
                <TrashIcon/>
                Delete
            </DropdownMenuItem>
        </DropdownMenuGroup>
    </DropdownMenuContent>
}
