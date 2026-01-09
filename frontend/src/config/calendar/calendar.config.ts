/*
 * assemble
 * calendar.config.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

const colorClasses = {
    red: "bg-red-50 ring-red-200 hover:bg-red-100 text-red-700",
    orange: "bg-orange-50 ring-orange-200 hover:bg-orange-100 text-orange-700",
    amber: "bg-amber-50 ring-amber-200 hover:bg-amber-100 text-amber-700",
    yellow: "bg-yellow-50 ring-yellow-200 hover:bg-yellow-100 text-yellow-700",
    lime: "bg-lime-50 ring-lime-200 hover:bg-lime-100 text-lime-700",
    green: "bg-green-50 ring-green-200 hover:bg-green-100 text-green-700",
    emerald: "bg-emerald-50 ring-emerald-200 hover:bg-emerald-100 text-emerald-700",
    teal: "bg-teal-50 ring-teal-200 hover:bg-teal-100 text-teal-700",
    cyan: "bg-cyan-50 ring-cyan-200 hover:bg-cyan-100 text-cyan-700",
    sky: "bg-sky-50 ring-sky-200 hover:bg-sky-100 text-sky-700",
    blue: "bg-blue-50 ring-blue-200 hover:bg-blue-100 text-blue-700",
    indigo: "bg-indigo-50 ring-indigo-200 hover:bg-indigo-100 text-indigo-700",
    violet: "bg-violet-50 ring-violet-200 hover:bg-violet-100 text-violet-700",
    purple: "bg-purple-50 ring-purple-200 hover:bg-purple-100 text-purple-700",
    pink: "bg-pink-50 ring-pink-200 hover:bg-pink-100 text-pink-700",
    rose: "bg-rose-50 ring-rose-200 hover:bg-rose-100 text-rose-700",
    gray: "bg-gray-50 ring-gray-200 hover:bg-gray-100 text-gray-700",
} as const;

const colorMobileClasses = {
    red: "bg-red-700",
    orange: "bg-orange-700",
    amber: "bg-amber-700",
    yellow: "bg-yellow-700",
    lime: "bg-lime-700",
    green: "bg-green-700",
    emerald: "bg-emerald-700",
    teal: "bg-teal-700",
    cyan: "bg-cyan-700",
    sky: "bg-sky-700",
    blue: "bg-blue-700",
    indigo: "bg-indigo-700",
    violet: "bg-violet-700",
    purple: "bg-purple-700",
    pink: "bg-pink-700",
    rose: "bg-rose-700",
    gray: "bg-gray-700",
} as const;

export { colorClasses, colorMobileClasses };