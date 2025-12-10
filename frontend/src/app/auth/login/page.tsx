/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import Login from "@/components/auth/Login";

export default function LoginPage() {
    return (
        <section key={ "login-form" } className="flex flex-col justify-center items-center">
            <Login/>
        </section>
    );
}