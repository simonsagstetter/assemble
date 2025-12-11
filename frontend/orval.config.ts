import { defineConfig } from 'orval';

export default defineConfig( {
    clientApi: {
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
            headers: true,
            override: {
                mutator: {
                    path: "./src/services/rest/axios-instance.ts",
                    name: "instance"
                },
                query: {
                    useQuery: true,
                    useMutation: true,
                    useInvalidate: true,
                    useSuspenseQuery: true,
                    version: 5,
                },
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