import type { CodegenConfig } from '@graphql-codegen/cli'

const config: CodegenConfig = {
    schema: 'http://localhost:8080/graphql',
    documents: './src/services/graphql/**/*.graphql',
    ignoreNoDocuments: false,
    config: {
        reactQueryVersion: "5"
    },
    generates: {
        './src/api/graphql/queries/graphql.tsx': {
            plugins: [
                "typescript",
                "typescript-operations",
                "typescript-react-query"
            ],
            config: {
                fetcher: {
                    endpoint: 'http://localhost:8080/graphql',
                    fetchParams: {
                        headers: {
                            "Content-Type": "application/json",
                            "Accept": "application/json",
                        }
                    }
                },
                exposeQueryKeys: true,
                exposeMutationKeys: true,
                exposeFetcher: true
            }
        },
    }
}

export default config