/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import type { Metadata } from "next";
import "./globals.css";
import { ReactNode } from "react";
import { Toaster } from "@/components/ui/sonner";
import GlobalProvider from "@/providers/global-provider";

export const metadata: Metadata = {
    title: "Assemble",
    description: "Assemble is tailored resource management tool for small carftmanship enterprises",
};

export default function RootLayout(
    { children, }: Readonly<{ children: ReactNode }>
) {
    return (
        <html lang="en">
        <body className="antialiased">
        <main>
            <GlobalProvider>
                { children }
            </GlobalProvider>
        </main>
        <Toaster position="top-right"/>
        </body>
        </html>
    );
}
