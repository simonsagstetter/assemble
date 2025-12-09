import { defineConfig } from 'orval';

export default defineConfig( {
    restApi: {
        input: {
            target: "http://localhost:8080/v3/api-docs",
            filters: {
                mode: "exclude",
                tags: [ "Authentication" ]
            }
        },
        output: {
            mode: "tags-split",
            target: "./src/api/rest/generated/query/",
            client: "react-query",
            baseUrl: "http://localhost:8080",
            override: {
                query: {
                    useQuery: true,
                    useMutation: true,
                    useSuspenseQuery: true,
                    useInfiniteQueryParam: "nextId",
                    useInfinite: true,
                    version: 5,
                    options: {
                        staleTime: 3600
                    }
                }
            }
        }
    },
    authApi: {
        input: {
            target: "http://localhost:8080/v3/api-docs",
        },
        output: {
            mode: "tags-split",
            target: "./src/api/rest/generated/fetch",
            client: "fetch",
            baseUrl: "http://localhost:8080",
        }
    }
} )