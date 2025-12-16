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
    @Size(min = 1, max = 100)
    @Column(name = "ADDRESS_STREET", length = 100)
    private String street;

    @Nullable
    @Size(min = 1, max = 20)
    @Column(name = "ADDRESS_NUMBER", length = 20)
    private String number;

    @Nullable
    @Size(min = 1, max = 20)
    @Column(name = "ADDRESS_POSTAL_CODE", length = 20)
    private String postalCode;

    @Nullable
    @Size(min = 1, max = 100)
    @Column(name = "ADDRESS_CITY", length = 100)
    private String city;

    @Nullable
    @Size(min = 1, max = 100)
    @Column(name = "ADDRESS_COUNTRY", length = 100)
    private String country;

    @Nullable
    @Size(min = 1, max = 100)
    @Column(name = "ADDRESS_STATE", length = 100)
    private String state;
}
