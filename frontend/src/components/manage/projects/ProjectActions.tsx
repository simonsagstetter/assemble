/*
 * assemble
 * ProjectActions.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { useRouter } from "@bprogress/next/app";
import {
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuSeparator
} from "@/components/ui/dropdown-menu";
import {
    IdCardIcon,
    PencilIcon,
    TrashIcon,
    LayersIcon,
} from "lucide-react";
import { ComponentProps, ForwardRefExoticComponent, RefAttributes } from "react";
import { DropdownMenuContentProps } from "@radix-ui/react-dropdown-menu";

export default function ProjectActions(
    { id, tableActions = false, ...props }:
        {
            id: string,
            tableActions?: boolean
        } & ComponentProps<ForwardRefExoticComponent<DropdownMenuContentProps & RefAttributes<HTMLDivElement>>>
) {
    const router = useRouter();

    return <DropdownMenuContent { ...props }>
        <DropdownMenuGroup>
            <DropdownMenuItem
                onSelect={ () => router.push( `/app/manage/projects/${ id }/assign-employees` ) }>
                <IdCardIcon/> Assign Employees
            </DropdownMenuItem>
        </DropdownMenuGroup>
        <DropdownMenuSeparator/>
        { tableActions ?
            <>
                <DropdownMenuGroup>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/manage/projects/${ id }` ) }>
                        <LayersIcon/> Details
                    </DropdownMenuItem>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/manage/projects/${ id }/edit` ) }>
                        <PencilIcon/> Edit
                    </DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
            </>
            : null
        }
        <DropdownMenuGroup>
            <DropdownMenuItem variant="destructive"
                              onSelect={ () => router.push( `/app/manage/projects/${ id }/delete` ) }>
                <TrashIcon/>
                Delete
            </DropdownMenuItem>
        </DropdownMenuGroup>
    </DropdownMenuContent>
}