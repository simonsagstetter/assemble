/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import EmployeeCreateForm from "@/components/manage/employees/EmployeeCreateForm";

export default function CreateEmployeePage() {
    return <FormPageHeader title={ "New" } description={ "Fill out the fields and click new to create a new employee." }
                           entity={ "Employee" }>
        <EmployeeCreateForm/>
    </FormPageHeader>
}