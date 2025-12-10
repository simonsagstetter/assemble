/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { ReactNode } from "react";
import { Metadata } from "next";

export const metadata: Metadata = {
    title: "Account | Assemble"
}

export default function AccountLayout( { children }: { children: Readonly<ReactNode> } ) {
    return children;
}
