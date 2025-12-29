/*
 * assemble
 * EmployeeDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.employee;

import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.employee.BankAccount;
import com.assemble.backend.models.entities.employee.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@Schema(name = "Employee", description = "Employee details")
public class EmployeeDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid",
            example = "6c0a7fd9872e47b29ec68fb10ea251de"
    )
    @NonNull
    private String id;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "E00001"
    )
    @NonNull
    private String no;

    @JsonManagedReference
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserDTO user;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max"
    )
    private String firstname;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Mustermann"
    )
    private String lastname;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max Mustermann"
    )
    @NonNull
    private String fullname;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "max.mustermannl@example.com"
    )
    private String email;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "+491234567890"
    )
    private String phone;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "date",
            example = "2025-01-01"
    )
    private LocalDate dateOfBirth;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "Berlin"
    )
    private String placeOfBirth;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "MaritalStatus",
            example = "Single"
    )
    private MaritalStatus maritalStatus;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "Address"
    )
    private Address address;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "BankAccount"
    )
    private BankAccount bankAccount;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            example = "german"
    )
    private String citizenship;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "TK Nord"
    )
    private String healthInsurance;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String nationalInsuranceNumber;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String taxIdentificationNumber;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant createdDate;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserAudit createdBy;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant lastModifiedDate;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserAudit lastModifiedBy;
}
