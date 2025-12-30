/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import dynamic from "next/dynamic";
import UserCreateForm from "@/components/admin/users/UserCreateForm";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function CreateUserModal() {
    return <ModalHeader title={ "New" } description={ "Fill out the fields and click new to create a new user." }
                        entity={ "user" }>
        <UserCreateForm/>
    </ModalHeader>;
}


export default dynamic( () => Promise.resolve( CreateUserModal ), {
    ssr: false,
} );
