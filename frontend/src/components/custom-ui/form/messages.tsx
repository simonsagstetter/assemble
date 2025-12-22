/*
 * assemble
 * messages.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { FieldGroup } from "@/components/ui/field";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { useFormContext } from "react-hook-form";
import useFormActionContext from "@/hooks/useFormActionContext";

function SuccessMessage( { message }: { message: string } ) {
    const { isSuccess } = useFormActionContext();
    return isSuccess ? (
        <FieldGroup>
            <Alert variant="default">
                <CheckCircle2Icon/>
                <AlertTitle>
                    { message }
                </AlertTitle>
            </Alert>
        </FieldGroup>
    ) : null;
}

function ErrorMessage() {
    const { formState: { errors } } = useFormContext();
    const { isError } = useFormActionContext();
    return isError && errors.root ? <FieldGroup>
        <Alert variant="destructive">
            <AlertCircleIcon/>
            <AlertTitle>Oops! We encountered an error.</AlertTitle>
            <AlertDescription>
                { errors.root.message }
            </AlertDescription>
        </Alert>
    </FieldGroup> : null;
}

export { SuccessMessage, ErrorMessage };