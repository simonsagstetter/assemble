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

import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { submitLogout } from "@/services/rest/auth/auth";
import { LOGIN_PATH } from "@/config/auth/auth.config";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { useProgress } from "@bprogress/next";

export default function LogoutButton() {
    const router = useRouter();
    const progress = useProgress();
    const form = useForm();

    const handleSubmitLogout = async () => {
        progress.start();
        try {
            await submitLogout();
            toast.success( "Logout successful" );
            router.push( LOGIN_PATH );
        } catch {
            toast.error( "Logout failed" );
        } finally {
            progress.stop();
        }
    }

    return <form onSubmit={ form.handleSubmit( handleSubmitLogout ) }>
        <Button type={ "submit" } variant={ "default" }>Logout</Button>
    </form>
}
