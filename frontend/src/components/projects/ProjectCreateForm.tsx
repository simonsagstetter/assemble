/*
 * assemble
 * ProjectCreateForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    ProjectCreateDTOType,
    ProjectDTOColor,
    ProjectDTOStage
} from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ProjectCreateSchema } from "@/types/projects/project.types";
import { useCreateProject } from "@/api/rest/generated/query/projects/projects";
import { ProjectFormFragment } from "@/components/projects/form/fragments";
import { FormBuilder } from "@/components/custom-ui/form/form";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getCreateProjectInvalidations } from "@/components/projects/form/invalidations";

export default function ProjectCreateForm() {
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( ProjectCreateSchema ),
        defaultValues: {
            name: "",
            active: true,
            type: ProjectCreateDTOType.EXTERNAL,
            stage: ProjectDTOStage.PROPOSAL,
            category: "",
            description: "",
            color: ProjectDTOColor.PURPLE,
        }
    } )

    const { mutate, isPending, isSuccess, isError } = useCreateProject();

    const handleCancel = () => {
        router.back();
    }

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            data
        } ),
        onSuccessCallbackAction: handleCancel,
        setInvalidations: () => getCreateProjectInvalidations(),
        setToastSuccessOptions: ( data ) => (
            {
                description: "Project " + data.no + " was created",
                action: {
                    label: "View Project",
                    onClick: () => router.push( "/app/manage/projects/" + data.id )
                }
            }
        )
    } )

    return <FormBuilder
        form={ form }
        formId={ "project-create-form" }
        status={ {
            isSuccess,
            isError,
            isPending
        } }
        options={ {
            actionLabel: "New",
            successMessage: "Project was created successfully",
            disabledOnSuccess: true
        } }
        submitHandler={ handleSubmit }
        cancelHandler={ handleCancel }>
        <ProjectFormFragment/>
    </FormBuilder>
}