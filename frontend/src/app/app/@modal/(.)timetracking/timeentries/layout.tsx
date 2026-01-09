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
import Modal from "@/components/custom-ui/Modal";

export default function TimeEntryModal( { children }: { children: Readonly<ReactNode> } ) {
    return <Modal>{ children }</Modal>;
}