/*
 * assemble
 * form.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { type FieldValues, FormProvider, SubmitHandler, UseFormReturn } from "react-hook-form";
import { FormActionContext } from "@/store/form-action-store";
import { ComponentProps, ReactNode, useMemo } from "react";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import useModalContext from "@/hooks/use-modal";

type FormBuilderOptions = {
    disabledOnSuccess?: boolean,
    successMessage?: string,
    maxScrollHeightClassName?: string
    actionLabel?: string
    actionVariant?: ComponentProps<typeof FormActions>["variant"];
    actionClassName?: string;
    actionHideCancel?: boolean;
    formGroupSpacing?: string;
}

type FormBuilderActionStatus = {
    isPending: boolean,
    isSuccess: boolean,
    isError: boolean
}

type FormBuilderProps<Input extends FieldValues = FieldValues, Context = never, Output = FieldValues> = {
    form: UseFormReturn<Input, Context, Output>,
    formId: string,
    status: FormBuilderActionStatus,
    submitHandler: SubmitHandler<Output>
    cancelHandler: () => void,
    children: ReactNode
    options?: FormBuilderOptions
}

function FormBuilder<Input extends FieldValues = FieldValues, Context = never, Output = FieldValues>(
    { form, formId, status, submitHandler, cancelHandler, options, children, ...props }:
        FormBuilderProps<Input, Context, Output> & ComponentProps<"form">
) {
    const modalContext = useModalContext();
    const scrollAreaClassNames = `${ options?.maxScrollHeightClassName ?? "h-auto" } my-0`;

    const formActionCtxValue = useMemo( () => ( {
        isPending: status.isPending,
        isSuccess: status.isSuccess,
        isError: status.isError,
        disableOnSuccess: options?.disabledOnSuccess ?? false,
        handleCancel: cancelHandler
    } satisfies FormActionContext ), [ status, cancelHandler, options ] )

    return <FormActionContext.Provider value={ formActionCtxValue }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( submitHandler ) } className="space-y-8" { ...props }>
                <ScrollArea className={ `${ modalContext ? scrollAreaClassNames : "" }` }>
                    <FieldGroup className={ options?.formGroupSpacing ?? "py-4 px-8" }>
                        { children }
                        <ErrorMessage/>
                        <SuccessMessage message={ options?.successMessage ?? "Action successful" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ options?.actionLabel ?? "New" }
                             variant={ options?.actionVariant } className={ options?.actionClassName }
                             hideCancel={ options?.actionHideCancel ?? false }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}

export {
    FormBuilder
};