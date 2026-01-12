/*
 * assemble
 * layout.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { ReactNode } from "react";
import Modal from "@/components/custom-ui/Modal";

export default function SettingsModalLayout( { children }: { children: Readonly<ReactNode> } ) {
    return <Modal>{ children }</Modal>;
}