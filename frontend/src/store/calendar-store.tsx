/*
 * assemble
 * calendar-store.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { createContext, ReactNode, useCallback, useReducer } from "react";
import { addMonths, subMonths } from "date-fns";
import { HolidayDTO, TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";

type TimeEntriesByDate = Partial<Record<string, TimeEntryDTO[]>>;
type HolidaysByDate = Partial<Record<string, HolidayDTO[]>>;

type CalendarSettings = {
    newLink: string;
    view: "week" | "month";
    subdivisionCode: string;
    disabled: boolean
}

type CalendarState = {
    currentDate: Date,
    selectedDate: Date,
    events: TimeEntriesByDate,
    holidays: HolidaysByDate,
    settings: CalendarSettings,
    isLoading: boolean
}

const DEFAULT_STATE: CalendarState = {
    currentDate: new Date(),
    selectedDate: new Date(),
    events: {},
    holidays: {},
    settings: {
        newLink: "",
        view: "month",
        subdivisionCode: "",
        disabled: false
    },
    isLoading: false
}

interface CalendarContext extends CalendarState {
    previousMonth: () => void,
    nextMonth: () => void,
    today: () => void,
    setCurrentDate: ( date: Date ) => void,
    setSelectedDate: ( date: Date ) => void,
    setEvents: ( events: TimeEntriesByDate ) => void,
    setHolidays: ( holidays: HolidaysByDate ) => void,
    setSettings: ( settings: CalendarSettings ) => void
    setIsLoading: ( isLoading: boolean ) => void
}

const CalendarContext = createContext<CalendarContext | null>( null );

enum ActionKind {
    PREVIOUS_MONTH,
    NEXT_MONTH,
    TODAY,
    SET_CURRENT_DATE,
    SET_SELECTED_DATE,
    SET_EVENTS,
    SET_HOLIDAYS,
    SET_SETTINGS,
    SET_IS_LOADING
}

type Action = | {
    type: ActionKind.PREVIOUS_MONTH
} | {
    type: ActionKind.NEXT_MONTH
} | {
    type: ActionKind.TODAY
} | {
    type: ActionKind.SET_CURRENT_DATE, payload: Date
} | {
    type: ActionKind.SET_SELECTED_DATE, payload: Date
} | {
    type: ActionKind.SET_EVENTS, payload: TimeEntriesByDate
} | {
    type: ActionKind.SET_SETTINGS, payload: CalendarSettings
} | {
    type: ActionKind.SET_IS_LOADING, payload: boolean
} | {
    type: ActionKind.SET_HOLIDAYS, payload: HolidaysByDate
}

const reducer = ( state: CalendarState, action: Action ) => {
    // HEADER ACTIONS
    if ( action.type === ActionKind.PREVIOUS_MONTH ) return ( {
        ...state,
        currentDate: subMonths( state.currentDate, 1 )
    } )
    if ( action.type === ActionKind.NEXT_MONTH ) return ( {
        ...state,
        currentDate: addMonths( state.currentDate, 1 )
    } )
    if ( action.type === ActionKind.TODAY ) return ( {
        ...state,
        currentDate: new Date(),
    } )
    if ( action.type === ActionKind.SET_IS_LOADING ) return ( {
        ...state,
        isLoading: action.payload
    } )

    // DATE ACTIONS
    if ( action.type === ActionKind.SET_CURRENT_DATE ) return ( {
        ...state,
        currentDate: action.payload
    } )
    if ( action.type === ActionKind.SET_SELECTED_DATE ) return ( {
        ...state,
        selectedDate: action.payload
    } )
    if ( action.type === ActionKind.SET_HOLIDAYS ) return ( {
        ...state,
        holidays: action.payload
    } )

    // DATA ACTIONS
    if ( action.type === ActionKind.SET_EVENTS ) return ( {
        ...state,
        events: action.payload
    } )

    // SETTINGS ACTIONS
    if ( action.type === ActionKind.SET_SETTINGS ) return ( {
            ...state,
            settings: action.payload
        }
    )

    return state;
}

function CalendarProvider(
    { children }: { children: ReactNode }
) {
    const [ state, dispatch ] = useReducer( reducer, DEFAULT_STATE );

    const previousMonth = useCallback( () => dispatch( { type: ActionKind.PREVIOUS_MONTH } ), [] );
    const nextMonth = useCallback( () => dispatch( { type: ActionKind.NEXT_MONTH } ), [] );
    const today = useCallback( () => dispatch( { type: ActionKind.TODAY } ), [] );
    const setIsLoading = useCallback( ( isLoading: boolean ) =>
            dispatch( { type: ActionKind.SET_IS_LOADING, payload: isLoading } )
        , [] );

    const setCurrentDate = useCallback( ( date: Date ) => dispatch( {
        type: ActionKind.SET_CURRENT_DATE,
        payload: date
    } ), [] );
    const setSelectedDate = useCallback( ( date: Date ) => dispatch( {
        type: ActionKind.SET_SELECTED_DATE,
        payload: date
    } ), [] );

    const setEvents = useCallback( ( events: TimeEntriesByDate ) => dispatch( {
        type: ActionKind.SET_EVENTS,
        payload: events
    } ), [] );
    const setHolidays = useCallback( ( holidays: HolidaysByDate ) => dispatch( {
        type: ActionKind.SET_HOLIDAYS,
        payload: holidays
    } ), [] )

    const setSettings = useCallback( ( settings: CalendarSettings ) => dispatch( {
        type: ActionKind.SET_SETTINGS,
        payload: settings
    } ), [] )

    const ctx = {
        ...state,
        previousMonth,
        nextMonth,
        today,
        setCurrentDate,
        setSelectedDate,
        setEvents,
        setHolidays,
        setSettings,
        setIsLoading,
    } satisfies CalendarContext;

    return <CalendarContext.Provider value={ ctx }>{ children }</CalendarContext.Provider>;
}

export default CalendarProvider;
export { CalendarContext, type CalendarSettings, type TimeEntriesByDate, type HolidaysByDate };