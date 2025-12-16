/*
 * assemble
 * Address.java
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

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Embeddable
public class Address implements Serializable {

    @Nullable
    @Size(min = 1)
    @Column(name = "ADDRESS_STREET")
    private String street;

    @Nullable
    @Size(min = 1)
    @Column(name = "ADDRESS_NUMBER")
    private String number;

    @Nullable
    @Size(min = 1)
    @Column(name = "ADDRESS_POSTAL_CODE")
    private String postalCode;

    @Nullable
    @Size(min = 1)
    @Column(name = "ADDRESS_CITY")
    private String city;

    @Nullable
    @Size(min = 1)
    @Column(name = "ADDRESS_COUNTRY")
    private String country;

    @Nullable
    @Size(min = 1)
    @Column(name = "ADDRESS_STATE")
    private String state;
}
