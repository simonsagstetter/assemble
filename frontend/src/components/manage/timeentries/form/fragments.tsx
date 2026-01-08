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
import { ProjectAssignmentLookupField } from "@/components/manage/projects/form/custom-fields";
import { CalendarField, TextareaField, TimeField } from "@/components/custom-ui/form/fields";
import { Badge } from "@/components/ui/badge";
import { Fragment } from "react";
import { useFormContext } from "react-hook-form";
import useFormActionContext from "@/hooks/useFormActionContext";

function TimeEntryFragment(
    { projectId, employeeId, total, isRestrictedSearch = true }:
    { projectId?: string, employeeId: string, total: string, isRestrictedSearch?: boolean }
) {
    const form = useFormContext();
    const { isPending, isSuccess, disableOnSuccess } = useFormActionContext();
    const { isSubmitting } = form.formState;
    const disabled = disableOnSuccess ? isPending || isSubmitting || isSuccess : isPending || isSubmitting;
    return <Fragment>
        <FieldSet>
            <FieldLegend>Project & date</FieldLegend>
            <FieldDescription>Select the project you worked on and the date the work was
                performed.
                Only projects you are assigned to can be selected.</FieldDescription>
            <FieldGroup>
                <div className={ "grid grid-cols-2 gap-16" }>
                    <ProjectAssignmentLookupField fieldName={ "projectId" }
                                                  formControl={ form.control }
                                                  disabled={ disabled }
                                                  selectedId={ projectId }
                                                  employeeId={ employeeId }
                                                  isRestrictedSerach={ isRestrictedSearch }
                    >
                        You can only select projects that you are assigned to.
                    </ProjectAssignmentLookupField>
                    <CalendarField fieldName={ "date" } formControl={ form.control }
                                   placeholder={ "Select a date" } label={ "Date" }
                                   disabled={ disabled }>
                        Select the date for this time entry.
                    </CalendarField>
                </div>
            </FieldGroup>
        </FieldSet>
        <FieldSet>
            <FieldLegend>Working time</FieldLegend>
            <FieldDescription>Define when you worked on this day.
                You can either enter start & end times or a total duration — not
                both.</FieldDescription>
            <FieldGroup>
                <div className={ "grid grid-cols-2 gap-16" }>
                    <TimeField fieldName={ "totalTime" } formControl={ form.control }
                               label={ "Duration" } disabled={ disabled }>
                        <Badge variant={ "secondary" }>Total { total }</Badge>
                    </TimeField>
                    <TimeField fieldName={ "pauseTime" } formControl={ form.control }
                               label={ "Pause" } disabled={ disabled }>
                        Required. Must be at least 00:00.
                    </TimeField>
                </div>
                <div className={ "grid grid-cols-2 gap-16" }>
                    <TimeField fieldName={ "startTime" } formControl={ form.control }
                               label={ "Starting time" } disabled={ disabled }>
                        Optional. Leave empty if you enter a duration.
                    </TimeField>
                    <TimeField fieldName={ "endTime" } formControl={ form.control }
                               label={ "Ending time" } disabled={ disabled }>
                        Optional. Leave empty if you enter a duration.
                    </TimeField>
                </div>
            </FieldGroup>
        </FieldSet>
        <FieldSet>
            <FieldLegend>Work Description</FieldLegend>
            <FieldDescription>Briefly describe what you worked on.
                This information may be used for reporting, billing, or project
                tracking.</FieldDescription>
            <FieldGroup>
                <TextareaField fieldName={ "description" } formControl={ form.control }
                               label={ "Description" } disabled={ disabled }
                               className={ "min-h-[200px]" }>
                    You must enter at least 10 characters.
                </TextareaField>
            </FieldGroup>
        </FieldSet>
    </Fragment>
}

export { TimeEntryFragment }