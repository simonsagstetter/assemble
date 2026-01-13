/*
 * assemble
 * EmployeeCompact.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { EmployeeDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { EntityCompact } from "@/components/custom-ui/compact";
import { IdCardIcon } from "lucide-react";
import Link from "next/link";

type EmployeeCompactProps = {
    employee: EmployeeDTO;
}

export default function EmployeeCompact( { employee }: EmployeeCompactProps ) {
    const details = [
        {
            label: "User",
            value: employee.user.username,
        },
        {
            label: "Email",
            value: employee.email,
        }
    ]
    return <EntityCompact Icon={ IdCardIcon } title={ employee.fullname } details={ details }>
        <Link
            href={ `/app/manage/employees/${ employee.id }` }
            className={ "hover:underline flex flex-row items-center gap-1.5" }
        >
            { employee.fullname }
        </Link>
    </EntityCompact>
}