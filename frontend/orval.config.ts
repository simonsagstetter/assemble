import { defineConfig } from 'orval';
import dotenv from 'dotenv';

dotenv.config();

const isDev = process.env.NODE_ENV === 'development';


export default defineConfig( {
    clientApi: {
        input: {
            target: "./openapi.json",
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
            target: "openapi.json",
        },
        output: {
            mode: "tags-split",
            target: "./src/api/rest/generated/fetch",
            client: "fetch",
            baseUrl: isDev ? "http://localhost:8080" : "http://backend:8080"
        }
    }
} )