/*
 * assemble
 * actions.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { Field } from "@/components/ui/field";
import { useFormContext } from "react-hook-form";
import { ComponentProps } from "react";
import useFormActionContext from "@/hooks/useFormActionContext";

type Variants = ComponentProps<typeof Button>["variant"];

function FormActions(
    { formId, variant, label }
    :
    {
        formId: string,
        variant?: Variants,
        label: string
    }
) {
    const { formState: { isSubmitting } } = useFormContext();
    const { isPending, isSuccess, handleCancel, disableOnSuccess } = useFormActionContext();
    return <Field orientation={ "horizontal" } className={ "p-8" }>
        <Button type="button"
                variant="secondary"
                className={ `${ variant === "destructive" ? "flex-2/3" : "flex-1/3" } grow cursor-pointer` }
                disabled={ isPending || isSubmitting }
                onClick={ handleCancel }
        >
            { isSuccess ? "Go back" : "Cancel" }
        </Button>
        <Button type="submit"
                className={ `${ variant === "destructive" ? "flex-1/3" : "flex-2/3" } grow cursor-pointer` }
                disabled={ disableOnSuccess ? isPending || isSubmitting || isSuccess : isPending || isSubmitting }
                form={ formId }
                variant={ variant || "default" }
        >
            { isPending ?
                <>
                    { "Processing" }
                    <Spinner/>
                </>
                : label
            }
        </Button>
    </Field>
}

export { FormActions }