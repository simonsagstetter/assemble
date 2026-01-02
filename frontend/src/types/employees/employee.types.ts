/*
 * assemble
 * employee.types.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { z } from "zod";
import { EmployeeDTOMaritalStatus } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { optionalString, past } from "@/utils/zod";

const AddressSchema = z.object( {
    street: optionalString( z.string().trim()
        .max( 100, "Street cannot be longer than 100 characters." ) ),
    number: optionalString( z.string().trim()
        .max( 20, "Number cannot be longer than 20 characters." ) ),
    postalCode: optionalString( z.string().trim()
        .max( 20, "Postal code cannot be longer than 20 characters." ) ),
    city: optionalString( z.string().trim()
        .max( 100, "City cannot be longer than 100 characters." ) ),
    state: optionalString( z.string().trim()
        .max( 100, "State cannot be longer than 100 characters." ) ),
    country: optionalString( z.string().trim()
        .max( 100, "Country cannot be longer than 100 characters." ) ),

} )

const BankAccountSchema = z.object( {
    holderName: optionalString( z.string().trim().max( 100, "Holder cannot be longer than 100 characters." ) ),
    institutionName: optionalString( z.string().trim()
        .max( 100, "Institution name cannot be longer than 100 characters." ) ),
    iban: optionalString( z.string().trim()
        .min( 15, "Iban must be at least 15 characters" )
        .max( 34, "IBAN cannot be longer than 34 characters." ) ),
    bic: optionalString( z.string().trim().max( 11, "BIC cannot be longer than 11 characters." ) ),
} )

const EmployeeCreateSchema = z.object( {
    userId: optionalString( z.string() ),
    firstname: z.string().trim().min( 1, "Firstname is required." ),
    lastname: z.string().trim().min( 1, "Lastname is required." ),
    email: z.email(),
    phone: optionalString( z.string().max( 15, "Phone cannot be longer than 15 characters." ) ),
    placeOfBirth: optionalString(
        z.string()
            .max( 100, "Place of birth cannot be longer than 100 characters." )
    ),
    maritalStatus: optionalString( z.enum( EmployeeDTOMaritalStatus ) ),
    citizenship: optionalString(
        z.string().max( 100, "Citizenship cannot be longer than 100 characters." )
    ),
    healthInsurance: optionalString(
        z.string()
            .max( 100, "Health insurance cannot be longer than 100 characters." )
    ),
    nationalInsuranceNumber: optionalString(
        z.string()
            .max( 100, "National Insurance Number cannot be longer than 100 characters." )
    ),
    taxIdentificationNumber: optionalString(
        z.string()
            .max( 100, "Tax ID cannot be longer than 100 characters." )
    ),
    dateOfBirth: past.optional(),
    address: AddressSchema.optional(),
    bankAccount: BankAccountSchema.optional(),
} )

const EmployeeUpdateSchema = EmployeeCreateSchema.omit( { userId: true } );

const EmployeeUpdateUserSchema = z.object( {
    userId: optionalString( z.string() )
} )


type EmployeeCreateFormData = z.infer<typeof EmployeeCreateSchema>;
type EmployeeUpdateFormData = z.infer<typeof EmployeeUpdateSchema>;
type EmployeeUpdateUserFormData = z.infer<typeof EmployeeUpdateUserSchema>;

export {
    type EmployeeCreateFormData,
    type EmployeeUpdateFormData,
    type EmployeeUpdateUserFormData,
    EmployeeCreateSchema,
    EmployeeUpdateSchema,
    EmployeeUpdateUserSchema,
}