/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { useGetAllGreetingsQuery } from "@/api/graphql/queries/graphql";
import { getAllGreetings } from "@/api/rest/generated/greetings/greetings";

export default async function Home() {
    const data = await useGetAllGreetingsQuery.fetcher()();

    const data2 = await getAllGreetings();

    console.log( "data", data );
    console.log( "data2", data2.data );

    return (
        <>
            { data.greetings.map( ( greeting, index ) => ( <p key={ index }>{ greeting.message }</p> ) ) }
        </>
    );
}
