/*
 * assemble
 * fragments.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { InputField } from "@/components/custom-ui/form/fields";
import { FieldGroup } from "@/components/ui/field";
import { useFormContext } from "react-hook-form";
import useFormActionContext from "@/hooks/useFormActionContext";

function IdentityFragment() {
    const form = useFormContext();
    const { isPending, isSuccess, disableOnSuccess } = useFormActionContext();
    const { isSubmitting } = form.formState;
    const disabled = disableOnSuccess ? isPending || isSubmitting || isSuccess : isPending || isSubmitting;
    return <FieldGroup>
        <InputField fieldName={ "username" }
                    formControl={ form.control }
                    label={ "Username" }
                    disabled={ disabled }
                    placeholder={ "e.g. musterpersonmax" }
                    autoFocus
        >
            This field is required and must have a unique value
        </InputField>
        <div className={ "grid grid-cols-2 gap-16" }>
            <InputField fieldName={ "firstname" }
                        formControl={ form.control }
                        label={ "Firstname" }
                        placeholder={ "e.g. Max" }
                        disabled={ disabled }
            >
                This field is required
            </InputField>
            <InputField fieldName={ "lastname" }
                        formControl={ form.control }
                        label={ "Lastname" }
                        placeholder={ "e.g. Musterperson" }
                        disabled={ disabled }
            >
                This field is required
            </InputField>
        </div>
        <InputField fieldName={ "email" }
                    formControl={ form.control }
                    label={ "E-Mail" }
                    placeholder={ "e.g. max.musterperson@example.com" }
                    disabled={ disabled }
                    type={ "email" }
        >
            This field is required
        </InputField>
    </FieldGroup>
}

export { IdentityFragment }