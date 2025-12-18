/*
 * assemble
 * Modal.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client"

import { Dialog, DialogContent, DialogDescription, DialogTitle, } from "@/components/ui/dialog";
import { createContext, ReactNode, useState } from "react";
import { useRouter } from "@bprogress/next/app";
import { VisuallyHidden } from "@radix-ui/react-visually-hidden";

type ModalProps = {
    children: ReactNode;
}

interface ModalContext {
    open: boolean;
    setOpen: ( open: boolean ) => void;
}

export const ModalContext = createContext<ModalContext>( {
    open: true,
    setOpen: () => {
    }
} );

export default function Modal( { children }: ModalProps ) {
    const [ open, setOpen ] = useState<boolean>( true );
    const router = useRouter();

    const hanldeOpenChange = ( open: boolean ) => {
        if ( !open ) router.back();
    }

    const value: ModalContext = { open, setOpen };

    return <ModalContext.Provider value={ value }>
        <Dialog open={ open } onOpenChange={ hanldeOpenChange }>
            <DialogContent className={ "min-w-[80%] overflow-hidden p-0 gap-0" }>
                <VisuallyHidden asChild>
                    <DialogTitle></DialogTitle>
                </VisuallyHidden>
                <VisuallyHidden asChild>
                    <DialogDescription></DialogDescription>
                </VisuallyHidden>
                { children }
            </DialogContent>
        </Dialog>
    </ModalContext.Provider>
}
