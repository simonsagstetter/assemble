package com.assemble.backend.models.auth;

import com.assemble.backend.models.core.BaseJPAEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
@Schema(name = "User", description = "User entity")
public class User extends BaseJPAEntity {

    @Schema(
            description = "user authentication identifier",
            maxLength = 15,
            minLength = 6,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "mustermannmax",
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    @Size(min = 6, max = 15, message = "Username must be between 6 and 15 characters long")
    @Column(unique = true, nullable = false, length = 15, name = "USERNAME")
    private String username;


    @Schema(
            hidden = true
    )
    @JsonIgnore
    @Column(nullable = false, name = "PASSWORD")
    @NotNull
    private String password;


    @Schema(
            description = "email address",
            format = "email",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "max.mustermann@example.com"
    )
    @Column(unique = true, nullable = false, name = "EMAIL")
    @NotNull
    @Email(message = "Please provide a valid email address")
    private String email;


    @Schema
    @NotNull
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private List<UserRole> roles;


    @Schema(
            description = "identifies if user is enabled",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    @Column(nullable = false, name = "IS_ENABLED")
    private boolean enabled = true;


    @Schema(
            description = "identifies if user is locked",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    @Column(nullable = false, name = "IS_LOCKED")
    private boolean locked = false;
}
