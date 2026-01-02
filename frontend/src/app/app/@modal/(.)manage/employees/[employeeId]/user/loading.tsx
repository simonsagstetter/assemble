/*
 * assemble
 * loading.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import ModalHeader from "@/components/custom-ui/ModalHeader";
import Loading from "@/components/custom-ui/Loading";

export default function LoadingPage() {
    return <ModalHeader title={ "Edit" } description={ "Update the connected user of this employee." }
                        entity={ "Employee" }>
        <Loading title={ "Loading Employee Details" }/>
    </ModalHeader>
}