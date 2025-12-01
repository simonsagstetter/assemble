import type { CodegenConfig } from '@graphql-codegen/cli'

const config: CodegenConfig = {
    schema: 'http://localhost:8080/graphql',
    documents: './src/services/graphql/**/*.graphql',
    ignoreNoDocuments: false,
    generates: {
        './src/api/graphql/generated/': {
            preset: 'client',
            config: {
                documentMode: 'string'
            },
        },
        './src/api/graphql/generated/schema.graphql': {
            plugins: [ 'schema-ast' ],
            config: {
                includeDirectives: true
            }
        },
    }
}

export default config