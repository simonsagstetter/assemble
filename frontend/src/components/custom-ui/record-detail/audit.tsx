/*
 * assemble
 * audit.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import {
    Detail,
    DetailLabel,
    DetailRow,
    DetailSection,
    DetailValue
} from "@/components/custom-ui/record-detail/detail";
import { format, parseISO } from "date-fns";
import Link from "next/link";
import { UserAudit } from "@/api/rest/generated/query/openAPIDefinition.schemas";

type AuditDetailProps = {
    id: string,
    createdDate: string,
    createdBy: UserAudit,
    lastModifiedDate: string,
    lastModifiedBy: UserAudit
}

const basePath = "/app/admin/users/";

function AuditDetail(
    { id, createdDate, lastModifiedDate, createdBy, lastModifiedBy }: AuditDetailProps
) {

    return <AccordionItem value={ "system" }>
        <AccordionTrigger>Audit</AccordionTrigger>
        <AccordionContent className={ "pb-10" }>
            <DetailSection>
                <DetailRow>
                    <Detail>
                        <DetailLabel>Id</DetailLabel>
                        <DetailValue>{ id }</DetailValue>
                    </Detail>
                </DetailRow>
                <DetailRow>
                    <Detail>
                        <DetailLabel>Created Date</DetailLabel>
                        <DetailValue>{ format( parseISO( createdDate ), "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                    </Detail>
                    <Detail>
                        <DetailLabel>Created By</DetailLabel>
                        <DetailValue>
                            { createdBy.id != null ?
                                <Link
                                    href={ basePath + createdBy.id }
                                    className={ "hover:underline text-primary" }
                                >
                                    { createdBy.username }
                                </Link>
                                :
                                <span className={ "text-primary" }>
                                    { createdBy.username }
                                </span>
                            }
                        </DetailValue>
                    </Detail>
                </DetailRow>
                <DetailRow>
                    <Detail>
                        <DetailLabel>Last Modified Date</DetailLabel>
                        <DetailValue>{ format( parseISO( lastModifiedDate ), "dd.MM.yyyy - HH:mm:ss" ) }</DetailValue>
                    </Detail>
                    <Detail>
                        <DetailLabel>Last Modified By</DetailLabel>
                        <DetailValue>
                            { lastModifiedBy.id != null ?
                                <Link
                                    href={ basePath + lastModifiedBy.id }
                                    className={ "hover:underline text-primary" }
                                >
                                    { lastModifiedBy.username }
                                </Link>
                                :
                                <span className={ "text-primary" }>
                                { lastModifiedBy.username }
                                </span>
                            }
                        </DetailValue>
                    </Detail>
                </DetailRow>
            </DetailSection>
        </AccordionContent>
    </AccordionItem>
}

export {
    AuditDetail
};