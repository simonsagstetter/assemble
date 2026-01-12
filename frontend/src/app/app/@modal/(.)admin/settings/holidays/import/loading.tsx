/*
 * assemble
 * loading.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import ModalHeader from "@/components/custom-ui/ModalHeader";
import Loading from "@/components/custom-ui/Loading";

export default function LoadingPage() {
    return <ModalHeader title={ "Holiday Import" } description={ "Select a year to import holidays" }
                        entity={ "Holiday" }>
        <Loading title={ "Loading Holiday Import" }/>
    </ModalHeader>
}