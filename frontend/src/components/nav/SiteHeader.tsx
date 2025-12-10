/*
 * assemble
 * SiteHeader.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { SidebarTrigger } from "@/components/ui/sidebar";
import { Separator } from "@/components/ui/separator";
import { useSelectedLayoutSegments } from "next/navigation";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbList,
    BreadcrumbLink,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb";
import Link from "next/link";
import { Button } from "../ui/button";
import GithubLogo from "@/assets/images/brands/github.svg";
import Image from "next/image";
import { Fragment } from "react";

export function SiteHeader() {
    const segements = useSelectedLayoutSegments();
    return (
        <header
            className="flex h-14 shrink-0 items-center gap-2 border-b transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-(--header-height)">
            <div className="flex w-full items-center gap-1 px-4 lg:gap-2 lg:px-6">
                <SidebarTrigger className="-ml-1"/>
                <Separator
                    orientation="vertical"
                    className="mx-2 data-[orientation=vertical]:h-4"
                />
                <Breadcrumb>
                    <BreadcrumbList>
                        <>
                            <BreadcrumbItem key={ "app" }>
                                <BreadcrumbLink asChild>
                                    <Link href={ "/app" }>{ "app" }</Link>
                                </BreadcrumbLink>
                            </BreadcrumbItem>
                            { segements.map( ( segment, index, array ) => (
                                <Fragment key={ segment }>
                                    { index === 0 ? <BreadcrumbSeparator/> : null }
                                    <BreadcrumbItem>
                                        <BreadcrumbLink asChild>
                                            <Link href={ `/app/${ segements.join( "/" ) }` }>{ segment }</Link>
                                        </BreadcrumbLink>
                                    </BreadcrumbItem>
                                    { index !== array.length - 1 ? <BreadcrumbSeparator/> : null }
                                </Fragment>

                            ) ) }
                        </>
                    </BreadcrumbList>
                </Breadcrumb>
                <div className="ml-auto flex items-center gap-2">
                    <Button variant="ghost" asChild size="sm" className="hidden sm:flex">
                        <a
                            href="https://github.com/simonsagstetter/assemble"
                            rel="noopener noreferrer"
                            target="_blank"
                            className="dark:text-foreground"
                        >
                            Github
                            <Image src={ GithubLogo } alt={ "Github Logo" } width={ 16 } height={ 16 }/>
                        </a>
                    </Button>
                </div>
            </div>
        </header>
    )
}
