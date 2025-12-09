/*
 * assemble
 * User.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.auth;

import com.assemble.backend.models.entities.core.BaseJPAEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "USERS")
public class User extends BaseJPAEntity implements Serializable {

    @NonNull
    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters long")
    @Column(unique = true, nullable = false, length = 20, name = "USERNAME")
    private String username;

    @NonNull
    @NotBlank
    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @NonNull
    @NotBlank
    @Column(nullable = false, name = "FIRST_NAME")
    private String firstname;

    @NonNull
    @NotBlank
    @Column(nullable = false, name = "LAST_NAME")
    private String lastname;

    @NonNull
    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false, name = "EMAIL")
    private String email;

    @NonNull
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "ROLE")
    private List<UserRole> roles;

    @Column(nullable = false, name = "IS_ENABLED")
    @Builder.Default
    private boolean enabled = true;

    @Column(nullable = false, name = "IS_LOCKED")
    @Builder.Default
    private boolean locked = false;

    public String getFullName() {
        return this.getFirstname() + " " + this.getLastname();
    }
}
