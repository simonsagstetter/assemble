/*
 * assemble
 * status.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { CircleCheckIcon, CircleXIcon } from "lucide-react";
import { Badge } from "@/components/ui/badge";

function Status(
    { label, variant = "green", className }:
    { label?: string, variant?: "green" | "red", className?: string }
) {
    const isGreen = variant === "green";
    return <Badge className={ `${ className }  **:text-primary-foreground` }
                  variant={ isGreen ? "default" : "destructive" }>
        { isGreen ? <CircleCheckIcon/> : <CircleXIcon/> }
        { label ? <span>{ label }</span> : null }
    </Badge>
}


export default Status;