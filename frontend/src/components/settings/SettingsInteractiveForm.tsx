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
    AppSettingsDTO,
    AppSettingsDTOHolidaySubdivisionCode
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { SettingsSchema, SubdivisionLabels } from "@/types/settings/settings.types";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { InputField, SelectField } from "@/components/custom-ui/form/fields";
import { useUpdateAppSettings } from "@/api/rest/generated/query/app-settings/app-settings";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateAppSettingsInvalidations } from "@/components/settings/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";
import { useForm } from "react-hook-form";

type SettingsInteractiveFormProps = {
    settings: AppSettingsDTO
}

export default function SettingsInteractiveForm( { settings }: Readonly<SettingsInteractiveFormProps> ) {
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

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            data
        } ),
        setInvalidations: ( data ) => getUpdateAppSettingsInvalidations( data ),
        onSuccessCallbackAction: () => null,
        onErrorCallbackAction: () => {
            form.setError( "root", {
                type: "manual",
                message: "Could not edit settings. Please try again later. If the problem persists, please contact the administrator."
            } );
        },
        setToastSuccessOptions: () => ( {
            description: "Settings updated"
        } )
    } )

    return <FormBuilder form={ form }
                        formId={ formId }
                        status={ {
                            isSuccess,
                            isError,
                            isPending,
                        } }
                        submitHandler={ handleSubmit }
                        cancelHandler={ () => null }
                        options={ {
                            successMessage: "Settings were updated successfully",
                            maxScrollHeightClassName: "",
                            actionLabel: "Update",
                            actionHideCancel: true,
                        } }

    >
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
    </FormBuilder>
}