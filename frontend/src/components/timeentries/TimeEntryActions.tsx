/*
 * assemble
 * TimeEntryActions.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
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
import { CalendarIcon, PencilIcon, TrashIcon } from "lucide-react";

export default function TimeEntryActions(
    { id, tableActions = false, ...props }:
        {
            id: string,
            tableActions?: boolean
        } & ComponentProps<ForwardRefExoticComponent<DropdownMenuContentProps & RefAttributes<HTMLDivElement>>>
) {
    const router = useRouter();

    return <DropdownMenuContent { ...props }>
        { tableActions ?
            <>
                <DropdownMenuGroup>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/manage/timeentries/${ id }` ) }>
                        <CalendarIcon/> Details
                    </DropdownMenuItem>
                    <DropdownMenuItem
                        onSelect={ () => router.push( `/app/manage/timeentries/${ id }/edit` ) }>
                        <PencilIcon/> Edit
                    </DropdownMenuItem>
                </DropdownMenuGroup>
                <DropdownMenuSeparator/>
            </>
            : null
        }
        <DropdownMenuGroup>
            <DropdownMenuItem variant="destructive"
                              onSelect={ () => router.push( `/app/manage/timeentries/${ id }/delete` ) }>
                <TrashIcon/>
                Delete
            </DropdownMenuItem>
        </DropdownMenuGroup>
    </DropdownMenuContent>
}