/*
 * assemble
 * handlers.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { FieldValues, Path, SubmitHandler, UseFormReturn } from "react-hook-form";
import { InvalidateQueryFilters, UseMutateFunction, useQueryClient } from "@tanstack/react-query";
import { invalidateAllQueries } from "@/utils/query";
import { ExternalToast, toast } from "sonner";
import { isAxiosError } from "axios";
import { MUTATION_ERRORS } from "@/config/messages/mutation.errors";
import { FieldValidationError } from "@/api/rest/generated/query/openAPIDefinition.schemas";

type ToastFeedback =
    | { showToast: true; toastOptions: ExternalToast }
    | { showToast?: false; toastOptions?: never };
 
type FormErrorFeedback<Input extends FieldValues> =
    | {
    showFormError: true;
    formErrorLocation: Path<Input>;
    formErrorDescription?: string;
}
    | {
    showFormError?: false;
    formErrorLocation?: never;
    formErrorDescription?: never;
};

type ErrorFeedback<Input extends FieldValues> =
    ToastFeedback & FormErrorFeedback<Input>;

type UseFormHandlerProps<
    Input extends FieldValues = FieldValues,
    Context = unknown,
    Output = FieldValues,
    TData = unknown,
    TError = unknown,
    TVariables = Input,
    TOnMutateResult = unknown> = {
    form: UseFormReturn<Input, Context, Output>,
    setToastSuccessOptions: ( data: TData ) => ExternalToast,
    onSuccessCallbackAction: () => void,
    setInvalidations: ( data: TData ) => Array<InvalidateQueryFilters>,
    mutate: UseMutateFunction<TData, TError, TVariables, TOnMutateResult>;
    variables: ( data: Output ) => TVariables,
    onErrorCallbackAction?: ( error: TError ) => void,
}

function useFormHandler<
    Input extends FieldValues = FieldValues,
    Context = unknown,
    Output = FieldValues,
    TData = unknown,
    TError = unknown,
    TVariables = Output,
    TOnMutateResult = unknown>(
    {
        form,
        mutate,
        variables,
        setInvalidations,
        setToastSuccessOptions,
        onSuccessCallbackAction,
        onErrorCallbackAction,
    }:
    UseFormHandlerProps<Input, Context, Output, TData, TError, TVariables, TOnMutateResult>
) {
    const queryClient = useQueryClient();

    const reportToUI = (
        {
            showToast = false,
            toastOptions,
            showFormError = false,
            formErrorLocation,
            formErrorDescription
        }: ErrorFeedback<Input>
    ) => {
        if ( showToast ) {
            toast.error( "Error", toastOptions )
        }

        if ( showFormError ) {
            form.setError( formErrorLocation ?? "root", {
                type: "manual",
                message: formErrorDescription ?? MUTATION_ERRORS.UNKOWN
            } )
        }
    }

    const onSuccess = ( data: TData ) => {
        const invalidations = setInvalidations( data );
        invalidateAllQueries( queryClient, invalidations ).then( () => {
            form.clearErrors();
            const toastSuccessOptions = setToastSuccessOptions( data );
            toast.success( "Success", toastSuccessOptions )
            onSuccessCallbackAction();
        } )
    }

    const onError = ( error: TError ) => {
        if ( onErrorCallbackAction ) {
            onErrorCallbackAction( error );
            return;
        }

        if ( !isAxiosError( error ) || !error.response?.data || !error.response?.status ) {
            reportToUI( { showToast: true, toastOptions: { description: MUTATION_ERRORS.UNKOWN } } )
            return;
        }

        const data = error.response?.data;
        const status = error.response?.status;

        if ( status === 400 && data && "errors" in data ) {
            data.errors.forEach( ( error: FieldValidationError ) => {
                reportToUI( {
                    showFormError: true,
                    formErrorDescription: error.errorMessage,
                    formErrorLocation: error.fieldName as Path<Input>
                } )
            } )
        } else if ( data && "message" in data ) {
            reportToUI( {
                showFormError: true,
                formErrorDescription: data.message,
                formErrorLocation: "root" as Path<Input>
            } )
        } else {
            reportToUI( { showToast: true, toastOptions: { description: MUTATION_ERRORS.UNKOWN } } )
        }

    }

    const handleSubmit: SubmitHandler<Output> = ( data ) => mutate(
        variables( data ),
        {
            onSuccess,
            onError,
        }
    )

    return handleSubmit;
}

export default useFormHandler;