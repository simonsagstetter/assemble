/*
 * assemble
 * Employee.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.employee;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.core.BaseJPAEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
@Table(name = "employees")
public class Employee extends BaseJPAEntity implements Serializable {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @NotBlank
    @Column(nullable = false, name = "FIRST_NAME")
    private String firstname;

    @NonNull
    @NotBlank
    @Column(nullable = false, name = "LAST_NAME")
    private String lastname;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Size(min = 3, max = 15)
    @Column(name = "PHONE", length = 15)
    private String phone;

    @Past
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Size(min = 1, max = 100)
    @Column(name = "PLACE_OF_BIRTH", length = 100)
    private String placeOfBirth;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Nullable
    private Address address;

    @Nullable
    private BankAccount bankAccount;

    @Column(name = "CITIZENSHIP", length = 30)
    @Size(min = 1, max = 30)
    private String citizenship;

    @Column(name = "HEALTH_INSURANCE", length = 100)
    @Size(min = 1, max = 100)
    private String healthInsurance;

    @Column(name = "NATIONAL_INSURANCE_NUMBER", length = 100)
    @Size(min = 1, max = 100)
    private String nationalInsuranceNumber;

    @Column(name = "TAX_IDENTIFICATION_NUMBER", length = 100)
    @Size(min = 1, max = 100)
    private String taxIdentificationNumber;
    
    @Transient
    public String getFullname() {
        return this.getFirstname() + " " + this.getLastname();
    }

}
