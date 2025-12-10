/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import LogoutButton from "@/components/auth/LogoutButton";
import Link from "next/link";

export default function AppPage() {


    return <div>
        <Link href={ "/app/account" }>Account</Link>
        <LogoutButton/>
    </div>
}
