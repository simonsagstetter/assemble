/*
 * assemble
 * MonthHeader.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

const dayNames = [ "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su" ];

export default function MonthHeader() {
    return <div className="grid grid-cols-7">
        { dayNames.map( day => (
            <div key={ day }
                 className="relative flex w-full flex-col items-center justify-center gap-1.5 bg-white p-2 md:flex-row md:gap-1 before:pointer-events-none before:absolute before:inset-0 before:border-gray-200 not-last:before:border-r before:border-b">
                <span className="text-xs font-medium text-gray-500">{ day }</span>
            </div>
        ) ) }
    </div>
}