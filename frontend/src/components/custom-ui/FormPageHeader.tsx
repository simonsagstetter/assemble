/*
 * assemble
 * FormPageHeader.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { ReactNode } from "react";
import { Separator } from "@/components/ui/separator";

type FormPageHeaderProps = {
    title: string;
    description: string;
    children: Readonly<ReactNode>
    entity?: string;
}

export default function FormPageHeader( { title, description, children, entity }: FormPageHeaderProps ) {
    return <>
        <CardHeader className={ "px-8 py-5" }>
            { entity != null ? <small className="text-xs uppercase leading-0 pt-1">{ entity }</small> : null }
            <CardTitle className={ "text-2xl leading-6" }>{ title }</CardTitle>
            <CardDescription className={ "leading-6" }>{ description }</CardDescription>
        </CardHeader>
        <Separator/>
        <CardContent className={ "px-0" }>
            { children }
        </CardContent>
    </>
}
