/*
 * assemble
 * RoleSearch.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { MultiCombobox } from "@/components/custom-ui/MultiCombobox";
import { ControllerRenderProps, FieldValues } from "react-hook-form";
import { UserCogIcon } from "lucide-react";

const options: UserRolesItem[] = [
    UserRolesItem.EXTERNAL,
    UserRolesItem.USER,
    UserRolesItem.MANAGER,
    UserRolesItem.ADMIN,
    UserRolesItem.SUPERUSER
]

type UserRoleFieldProps = {
    field: ControllerRenderProps<FieldValues, string>,
    disabled: boolean
}

export default function RoleSearch( { field, disabled }: UserRoleFieldProps ) {

    return <MultiCombobox
        disabled={ disabled }
        placeholder={ "Select roles..." }
        heading={ "User Roles" }
        field={ field }
        options={ options }
        Icon={ <UserCogIcon/> }
    />
}
