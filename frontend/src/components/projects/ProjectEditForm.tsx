/*
 * assemble
 * ProjectEditForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { ProjectDTO, } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { useUpdateProject } from "@/api/rest/generated/query/projects/projects";
import { ProjectFormFragment } from "@/components/projects/form/fragments";
import { zodResolver } from "@hookform/resolvers/zod";
import { ProjectUpdateSchema } from "@/types/projects/project.types";
import { FormBuilder } from "@/components/custom-ui/form/form";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateProjectInvalidations } from "@/components/projects/form/invalidations";

type ProjectEditFormProps = {
    project: ProjectDTO;
}

export default function ProjectEditForm( { project }: Readonly<ProjectEditFormProps> ) {
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( ProjectUpdateSchema ),
        defaultValues: {
            name: project.name,
            active: project.active,
            type: project.type,
            stage: project.stage,
            category: project.category,
            description: project.description,
            color: project.color,
        }
    } );

    const { mutate, isPending, isSuccess, isError } = useUpdateProject();

    const handleCancel = () => {
        router.back();
    }

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            id: project.id,
            data: {
                ...data
            }
        } ),
        onSuccessCallbackAction: handleCancel,
        setInvalidations: ( data ) => getUpdateProjectInvalidations( data || project ),
        setToastSuccessOptions: ( data ) => ( {
            description: `Project ${ data?.no ?? project.no } was updated`,
            action: {
                label: "View Project",
                onClick: () => router.push( `/app/manage/projects/${ data?.id ?? project.id }` )
            }
        } )
    } );

    return <FormBuilder form={ form }
                        formId={ "project-edit-formt" }
                        status={ {
                            isPending,
                            isSuccess,
                            isError
                        } }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "Project was updated successfully",
                        } }
                        submitHandler={ handleSubmit }
                        cancelHandler={ handleCancel }>
        <ProjectFormFragment/>
    </FormBuilder>
}
