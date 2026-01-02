/*
 * assemble
 * loading.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import ModalHeader from "@/components/custom-ui/ModalHeader";
import Loading from "@/components/custom-ui/Loading";

export default function LoadingPage() {
    return <ModalHeader title={ "Delete" } description={ "Confirm the action below to delete this assignment." }
                        entity={ "Project Assignment" }>
        <Loading title={ "Loading Assignment Details" }/>
    </ModalHeader>
}