/*
 * assemble
 * BankAccount.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount implements Serializable {

    @Nullable
    @Column(name = "BANK_ACCOUNT_HOLDER_NAME", length = 100)
    @Size(min = 1, max = 100)
    private String holderName;

    @Nullable
    @Size(min = 1, max = 100)
    @Column(name = "BANK_ACCOUNT_INSTITUTION_NAME", length = 100)
    private String institutionName;

    @Nullable
    @Size(min = 15, max = 34)
    @Column(name = "BANK_ACCOUNT_IBAN", length = 34)
    private String iban;

    @Nullable
    @Size(min = 1, max = 11)
    @Column(name = "BANK_ACCOUNT_BIC", length = 11)
    private String bic;

}
