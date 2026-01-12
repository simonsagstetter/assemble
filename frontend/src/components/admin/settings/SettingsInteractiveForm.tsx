/*
 * assemble
 * SettingsInteractiveForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import {
    AppSettingsDTO, AppSettingsDTOHolidaySubdivisionCode
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { SettingsFormData, SettingsSchema, SubdivisionLabels } from "@/types/settings/settings.types";
import { FormActionContext } from "@/store/formActionStore";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import useModalContext from "@/hooks/useModalContext";
import { InputField, SelectField } from "@/components/custom-ui/form/fields";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FormActions } from "@/components/custom-ui/form/actions";
import { getGetAppSettingsQueryKey, useUpdateAppSettings } from "@/api/rest/generated/query/app-settings/app-settings";
import { toast } from "sonner";
import { invalidateQueries } from "@/utils/query";
import { useQueryClient } from "@tanstack/react-query";
import { Separator } from "@/components/ui/separator";
import {
    getGetCompanySubdivisionCodeQueryKey, getGetHolidaysByYearAndSubdivisionCodeQueryKey,
} from "@/api/rest/generated/query/holidays/holidays";
import { format } from "date-fns";

type SettingsInteractiveFormProps = {
    settings: AppSettingsDTO
}

export default function SettingsInteractiveForm( { settings }: SettingsInteractiveFormProps ) {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const formId = "settings-form";
    const form = useForm( {
        resolver: zodResolver( SettingsSchema ),
        defaultValues: {
            companyName: settings.companyName ?? "",
            companyAddress: {
                street: settings.companyAddress?.street ?? "",
                number: settings.companyAddress?.number ?? "",
                city: settings.companyAddress?.city ?? "",
                postalCode: settings.companyAddress?.postalCode ?? "",
                state: settings.companyAddress?.state ?? "",
                country: settings.companyAddress?.country ?? ""
            },
            holidaySubdivisionCode: settings.holidaySubdivisionCode
        }
    } )
    const isSubmitting = form.formState.isSubmitting;

    const { mutate, isError, isPending, isSuccess } = useUpdateAppSettings();

    const disabled = isPending || isSubmitting;

    const handleUpdateSettings = ( data: SettingsFormData ) => {
        mutate(
            {
                data
            },
            {
                onSuccess: async () => {
                    await invalidateQueries( queryClient, [
                        getGetAppSettingsQueryKey(),
                        getGetHolidaysByYearAndSubdivisionCodeQueryKey( {
                            subdivisionCode: "DE-" + settings.holidaySubdivisionCode,
                            year: format( new Date(), "yyyy" )
                        } ) as readonly string[],
                        getGetCompanySubdivisionCodeQueryKey()
                    ] );
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "Settings were updated"
                    } );
                },
                onError: () => {
                    form.setError( "root", {
                        type: "manual",
                        message: "Could not update settings. Please try again later. If the problem persists, please contact the administrator."
                    } );
                }
            }
        )
    }

    return <FormActionContext.Provider value={ {
        isPending, isSuccess, isError, disableOnSuccess: false,
        handleCancel: () => {
        }
    } }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleUpdateSettings ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldLegend>Company Settings</FieldLegend>
                            <FieldDescription>Information about the company which is using this
                                software</FieldDescription>
                            <FieldGroup>
                                <InputField fieldName={ "companyName" } formControl={ form.control }
                                            label={ "Company Name" } placeholder={ "Enter your company name" }
                                            disabled={ disabled }
                                />
                                <SelectField fieldName={ "holidaySubdivisionCode" }
                                             formControl={ form.control }
                                             label={ "Subdivision Code" }
                                             placeholder={ "Choose a subdivision code" }
                                             disabled={ disabled }
                                             options={ Object.keys( AppSettingsDTOHolidaySubdivisionCode )
                                                 .map( key => {
                                                     return {
                                                         value: AppSettingsDTOHolidaySubdivisionCode[ key as keyof typeof AppSettingsDTOHolidaySubdivisionCode ],
                                                         label: SubdivisionLabels[ key as keyof typeof SubdivisionLabels ],
                                                     }
                                                 } ) }
                                >
                                    Choose a subdivision code. This is used to determine the holidays for the selected
                                    year.
                                </SelectField>
                                <div className={ "grid grid-cols-2 gap-16" }>
                                    <InputField fieldName={ "companyAddress.street" }
                                                formControl={ form.control }
                                                label={ "Street" }
                                                placeholder={ "e.g. Berliner Straße" }
                                                disabled={ disabled }
                                    />
                                    <InputField fieldName={ "companyAddress.number" }
                                                formControl={ form.control }
                                                label={ "Number" }
                                                placeholder={ "e.g. 100" }
                                                disabled={ disabled }
                                    />
                                </div>
                                <div className={ "grid grid-cols-2 gap-16" }>
                                    <InputField fieldName={ "companyAddress.postalCode" }
                                                formControl={ form.control }
                                                label={ "Postal Code" }
                                                placeholder={ "e.g. 12345" }
                                                disabled={ disabled }
                                    />
                                    <InputField fieldName={ "companyAddress.city" }
                                                formControl={ form.control }
                                                label={ "City" }
                                                placeholder={ "e.g. Berlin" }
                                                disabled={ disabled }
                                    />
                                </div>
                                <div className={ "grid grid-cols-2 gap-16" }>
                                    <InputField fieldName={ "companyAddress.state" }
                                                formControl={ form.control }
                                                label={ "State" }
                                                placeholder={ "e.g. Berlin" }
                                                disabled={ disabled }
                                    />
                                    <InputField fieldName={ "companyAddress.country" }
                                                formControl={ form.control }
                                                label={ "Country" }
                                                placeholder={ "e.g. Germany" }
                                                disabled={ disabled }
                                    />
                                </div>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Settings were updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ "Save" } hideCancel/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}