/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import UserCreateForm from "@/components/admin/users/UserCreateForm";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";

export default function CreateUserPage() {
    return <FormPageHeader title={ "New" } description={ "Fill out the fields and click new to create a new user." }
                           entity={ "USER" }>
        <UserCreateForm/>
    </FormPageHeader>
}
