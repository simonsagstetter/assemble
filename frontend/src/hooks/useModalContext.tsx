/*
 * assemble
 * useModalContext.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { use } from "react";
import { ModalContext } from "@/components/custom-ui/Modal";

export default function useModalContext() {
    return use( ModalContext );
}
