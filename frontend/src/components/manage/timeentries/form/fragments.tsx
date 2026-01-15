/*
 * assemble
 * fragments.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Field, FieldDescription, FieldGroup, FieldLabel, FieldLegend, FieldSet } from "@/components/ui/field";
import { ProjectAssignmentLookupField } from "@/components/manage/projects/form/custom-fields";
import { CalendarField, TextareaField, TimeField } from "@/components/custom-ui/form/fields";
import { Badge } from "@/components/ui/badge";
import { Fragment } from "react";
import { useFormContext } from "react-hook-form";
import useFormActionContext from "@/hooks/useFormActionContext";
import { TimeEntryDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import {
    Item,
    ItemContent,
    ItemDescription,
    ItemGroup,
    ItemMedia,
    ItemTitle
} from "@/components/ui/item";
import Link from "next/link";
import { CalendarIcon } from "lucide-react";
import { isoDurationToMs, msToHHmm } from "@/utils/duration";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";

function DescriptionFragment( { disabled }: { disabled: boolean } ) {
    const form = useFormContext();
    return <TextareaField fieldName={ "description" } formControl={ form.control }
                          label={ "Description" } disabled={ disabled }
                          className={ "min-h-[200px]" }>
        You must enter at least 10 characters.
    </TextareaField>
}

function RelatedTimeEntriesFragment( { relatedTimeEntries }: { relatedTimeEntries: TimeEntryDTO[] } ) {
    return <Field>
        <FieldLabel>Today related time entries</FieldLabel>
        <ItemGroup className={ "gap-2" }>
            { relatedTimeEntries.map( item => (
                <Tooltip key={ item.id }>
                    <TooltipTrigger asChild>
                        <Item variant={ "outline" } asChild
                              role={ "listitem" }
                              className={ "px-4 py-3 shadow-xs" }>
                            <Link href={ `/app/timetracking/timeentries/${ item.id }/update` }>
                                <ItemMedia variant={ "image" }
                                           className={ "bg-primary" }>
                                    <CalendarIcon
                                        className={ "text-primary-foreground stroke-1 size-5" }/>
                                </ItemMedia>
                                <ItemContent>
                                    <ItemTitle
                                        className={ "line-clamp-1" }>{ item.project.name }</ItemTitle>
                                    <ItemDescription>{ item.description }</ItemDescription>
                                </ItemContent>
                                <ItemContent className={ "self-start! text-center" }>
                                    <ItemDescription>{ msToHHmm( isoDurationToMs( item.totalTime ) - isoDurationToMs( item.pauseTime ) ) }</ItemDescription>
                                </ItemContent>
                            </Link>
                        </Item>
                    </TooltipTrigger>
                    <TooltipContent className={ "max-w-[400px]" }>
                        { item.description }
                    </TooltipContent>
                </Tooltip>
            ) ) }
        </ItemGroup>
    </Field>
}

function TimeEntryFragment(
    { projectId, employeeId, total, isRestrictedSearch = true, relatedTimeEntries = [] }:
    {
        projectId?: string,
        employeeId: string,
        total: string,
        isRestrictedSearch?: boolean,
        relatedTimeEntries?: TimeEntryDTO[]
    }
) {
    const form = useFormContext();
    const { isPending, isSuccess, disableOnSuccess } = useFormActionContext();
    const { isSubmitting } = form.formState;
    const disabled = disableOnSuccess ? isPending || isSubmitting || isSuccess : isPending || isSubmitting;
    const hasRelatedTimeEntries = relatedTimeEntries?.length > 0
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
                                                  isRestrictedSearch={ isRestrictedSearch }
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
                { hasRelatedTimeEntries ?
                    <div className={ "grid grid-cols-2 gap-16" }>
                        <DescriptionFragment disabled={ disabled }/>
                        <RelatedTimeEntriesFragment relatedTimeEntries={ relatedTimeEntries }/>
                    </div>
                    : <DescriptionFragment disabled={ disabled }/>
                }
            </FieldGroup>
        </FieldSet>
    </Fragment>
}

export { TimeEntryFragment }