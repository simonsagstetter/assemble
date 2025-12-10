/*
 * assemble
 * LogoutButton.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { submitLogout } from "@/services/rest/auth/auth";
import { LOGIN_PATH } from "@/config/auth/auth.config";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { LogOutIcon } from "lucide-react";

export default function LogoutButton() {
    const router = useRouter();
    const form = useForm();

    const handleSubmitLogout = async () => {
        try {
            await submitLogout();
            toast.success( "Logout successful" );
            router.push( LOGIN_PATH );
        } catch {
            toast.error( "Logout failed" );
        }
    }

    return <form onSubmit={ form.handleSubmit( handleSubmitLogout ) }>
        <Button type={ "submit" } variant={ "outline" }><LogOutIcon/> Logout</Button>
    </form>
}
