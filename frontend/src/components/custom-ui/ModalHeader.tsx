/*
 * assemble
 * ModalHeader.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { ReactNode } from "react";
import { Separator } from "@/components/ui/separator";

type ModalHeaderProps = {
    title: string;
    description: string;
    entity?: string;
    children: Readonly<ReactNode>
}

export default function ModalHeader( { children, description, entity, title }: ModalHeaderProps ) {
    return <>
        <DialogHeader className={ "py-5 px-8 " }>
            { entity != null ? <small className="text-xs uppercase leading-0 pt-1">{ entity }</small> : null }
            <DialogTitle className={ "text-2xl leading-6" }>{ title }</DialogTitle>
            <DialogDescription className={ "leading-6" }>
                { description }
            </DialogDescription>
        </DialogHeader>
        <Separator/>
        { children }
    </>
}
