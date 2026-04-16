import type { NextConfig } from "next";
import { loadEnvConfig } from '@next/env'

const isDev = process.env.NODE_ENV === "development";
const projectDir = process.cwd()

loadEnvConfig( isDev ? projectDir + "/.env.development" : projectDir + ".env.production" )

if ( process.env.BACKEND_API_URL === undefined ) throw new Error( "BACKEND_API_URL is not defined in .env file." )

const nextConfig: NextConfig = {
    reactCompiler: true,
    async rewrites() {
        return [
            {
                source: "/rest/:path*",
                destination: `${ process.env.BACKEND_API_URL }/:path*`
            }
        ]
    }
};

export default nextConfig;
