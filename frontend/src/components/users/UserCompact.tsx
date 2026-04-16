/*
 * assemble
 * UserCompact.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { EntityCompact } from "@/components/custom-ui/compact";
import { UserIcon } from "lucide-react";
import Link from "next/link";
import { User } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Badge } from "@/components/ui/badge";

type UserCompactProps = {
    user: User;
}

export default function UserCompact( { user }: UserCompactProps ) {
    const details = [
        {
            label: "Name",
            value: user.fullname,
        },
        {
            label: "Email",
            value: user.email,
        },
        {
            label: "Roles",
            node: <div className={ "block" }>{ user.roles.map( role => (
                <Badge key={ role } variant={ "secondary" }>{ role }</Badge>
            ) ) }</div>
        }
    ]
    return <EntityCompact Icon={ UserIcon } title={ user.username } details={ details }>
        <Link
            href={ "/app/admin/users/" + user.id }
            className={ "hover:underline" }
        >
            { user.username }
        </Link>
    </EntityCompact>
}