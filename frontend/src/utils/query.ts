/*
 * assemble
 * query.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { type QueryClient } from "@tanstack/react-query";

async function invalidateQueries( queryClient: QueryClient, queryKeys: Array<ReadonlyArray<string>> ) {
    const promises = queryKeys.map(
        async queryKey => await queryClient.invalidateQueries( { queryKey } )
    )
    return await Promise.all( promises );
}

export { invalidateQueries }