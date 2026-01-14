import { defineConfig } from 'orval';
import { config } from 'dotenv';

const isDev = process.env.NODE_ENV === 'development';

config( { path: isDev ? '.env.development' : '.env.production' } );

if ( process.env.BACKEND_API_URL === undefined ) throw new Error( "BACKEND_API_URL is not defined in .env file." )

export default defineConfig( {
    clientApi: {
        input: {
            target: "./openapi.json",
            filters: {
                mode: "exclude",
                tags: [
                    "Authentication"
                ]
            }
        },
        output: {
            mode: "tags-split",
            target: "./src/api/rest/generated/query/",
            client: "react-query",
            baseUrl: "/rest",
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
            filters: {
                mode: "include",
                tags: [
                    "Authentication",
                    "Users"
                ]
            }
        },
        output: {
            mode: "tags-split",
            target: "./src/api/rest/generated/fetch",
            client: "fetch",
            baseUrl: process.env.BACKEND_API_URL
        }
    }
} )