/*
 * assemble
 * StageProgress.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Progress } from "@/components/ui/progress";
import { useCallback } from "react";

type StageProgressProps<T> = {
    label: string;
    values: T[],
    value: string
}

export default function StageProgress<T>( { label, values, value }: StageProgressProps<T> ) {
    const getStageProgressPercent = useCallback( ( stage: T ) => {
        return Math.round( ( ( values.indexOf( stage ) + 1 ) / values.length ) * 100 );
    }, [ values ] );

    return <div className={ "flex flex-col pb-1 px-8" }>
        <small className={ "text-xs uppercase leading-2" }>{ label }</small>
        <h2 className={ "font-bold text-2xl pb-1 tracking-wide" }>{ value }</h2>
        <Progress value={ getStageProgressPercent( value as unknown as T ) } className={ "h-3 [&_div]:bg-blue-800" }/>
    </div>
}