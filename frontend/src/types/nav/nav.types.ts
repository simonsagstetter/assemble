/*
 * assemble
 * nav.types..css
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import * as react from "react";
import { LucideProps } from "lucide-react";

type MenuItem = {
    href: string;
    label: string;
    Icon: react.ForwardRefExoticComponent<Omit<LucideProps, "ref"> & react.RefAttributes<SVGSVGElement>>;
    actions?: MenuItemAction[];
}

type MenuItemAction = {
    label: string;
    href: string;
    Icon: react.ForwardRefExoticComponent<Omit<LucideProps, "ref"> & react.RefAttributes<SVGSVGElement>>;
}

export {
    type MenuItem,
    type MenuItemAction
}