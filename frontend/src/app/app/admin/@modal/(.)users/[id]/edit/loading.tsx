/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import Loading from "@/components/custom-ui/Loading";
import ModalHeader from "@/components/custom-ui/ModalHeader";

export default function LoadingPage() {
    return <ModalHeader title={ "Edit" } description={ "Update the fields and click save to update the user." }>
        <Loading title={ "Loading User Details" }/>
    </ModalHeader>
}
