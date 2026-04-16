/*
 * assemble
 * query.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { InvalidateQueryFilters, type QueryClient } from "@tanstack/react-query";

async function invalidateAllQueries( queryClient: QueryClient, queries: Array<InvalidateQueryFilters<readonly unknown[]>> ) {
    const promises = queries.map(
        async ( { queryKey, refetchType } ) => queryClient.invalidateQueries( {
            queryKey,
            refetchType: refetchType || "none"
        } )
    );

    return await Promise.all( promises );
}

export { invalidateAllQueries }