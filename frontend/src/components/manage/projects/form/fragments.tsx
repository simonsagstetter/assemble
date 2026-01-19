/*
 * assemble
 * fragments.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { InputField, SelectField, SwitchField, TextareaField } from "@/components/custom-ui/form/fields";
import { ProjectDTOColor, ProjectDTOStage, ProjectDTOType } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { colorClasses, colorMobileClasses } from "@/config/calendar/calendar.config";
import { useFormContext } from "react-hook-form";
import useFormActionContext from "@/hooks/useFormActionContext";

function ProjectFormFragment() {
    const form = useFormContext();
    const { isSubmitting } = form.formState;
    const { isPending } = useFormActionContext();
    const disabled = isPending || isSubmitting;
    return <FieldSet>
        <FieldLegend>Details</FieldLegend>
        <FieldDescription>Detailed information about the new project</FieldDescription>
        <FieldGroup>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "name" }
                            formControl={ form.control }
                            label={ "Name" }
                            placeholder={ "e.g. BERLINSTR 200 SANIERUNG" }
                            disabled={ disabled }
                            autoFocus
                >
                    This field is required and must be unique.
                </InputField>
                <SwitchField fieldName={ "active" }
                             formControl={ form.control }
                             label={ "Status" }
                             disabled={ disabled }
                >
                    Employees wont be able to create time entries on inactive projects.
                </SwitchField>
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <SelectField fieldName={ "type" }
                             formControl={ form.control }
                             label={ "Project Type" }
                             placeholder={ "Choose a type" }
                             options={ [
                                 { label: "External", value: ProjectDTOType.EXTERNAL },
                                 { label: "Internal", value: ProjectDTOType.INTERNAL },
                             ] }
                             disabled={ disabled }
                />
                <SelectField fieldName={ "stage" }
                             formControl={ form.control }
                             label={ "Project Stage" }
                             placeholder={ "Choose a stage" }
                             options={ [
                                 { label: "Proposal", value: ProjectDTOStage.PROPOSAL },
                                 { label: "Negotiation", value: ProjectDTOStage.NEGOTIATION },
                                 { label: "Assigned", value: ProjectDTOStage.ASSIGNED },
                                 { label: "Implementation", value: ProjectDTOStage.IMPLEMENTATION },
                                 { label: "Final Billing", value: ProjectDTOStage.FINAL_BILLING },
                                 { label: "Completed", value: ProjectDTOStage.COMPLETED },
                                 { label: "Closed", value: ProjectDTOStage.CLOSED },
                             ] }
                             disabled={ disabled }
                />
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "category" }
                            formControl={ form.control }
                            label={ "Category" }
                            placeholder={ "e.g. Sanierung" }
                            disabled={ disabled }
                />
                <SelectField fieldName={ "color" }
                             formControl={ form.control }
                             label={ "Project Color" }
                             placeholder={ "Choose a color" }
                             options={ Object.keys( colorClasses )
                                 .filter( key => key !== "gray" )
                                 .map( key => {
                                     const value = ProjectDTOColor[ key.toUpperCase() as keyof typeof ProjectDTOColor ];
                                     return {
                                         label: key,
                                         value,
                                         elem: <>
                                                             <span
                                                                 className={ "size-2 rounded-full " + colorMobileClasses[ key as keyof typeof colorClasses ] }>
                                                             </span>
                                             { value.toLowerCase() }
                                         </>
                                     }
                                 } ) }
                             disabled={ disabled }>
                    These colors are used to visualize the project in the calendar.
                </SelectField>
            </div>
            <TextareaField fieldName={ "description" }
                           formControl={ form.control }
                           label={ "Description" }
                           placeholder={ "e.g. The project is about the construction of a new building in Berlin." }
                           disabled={ disabled }
            />
        </FieldGroup>
    </FieldSet>
}

export {
    ProjectFormFragment
}