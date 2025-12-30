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
    return <ModalHeader title={ "Update Employee" }
                        description={ "Update the connected employee of this user." }
                        entity={ "user" }>
        <Loading title={ "Loading User Details" }/>
    </ModalHeader>
}
