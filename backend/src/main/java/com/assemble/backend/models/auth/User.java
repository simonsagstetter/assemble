package com.assemble.backend.models.auth;

import com.assemble.backend.models.core.BaseJPAEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Schema(name = "User", description = "User entity")
public class User extends BaseJPAEntity implements Serializable {

    @Schema(
            description = "user authentication identifier",
            maxLength = 15,
            minLength = 6,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "mustermannmax",
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @Size(min = 6, max = 15, message = "Username must be between 6 and 15 characters long")
    @NonNull
    @Column(unique = true, nullable = false, length = 15, name = "USERNAME")
    private String username;


    @Schema(
            hidden = true
    )
    @JsonIgnore
    @NonNull
    @Column(nullable = false, name = "PASSWORD")
    private String password;


    @Schema(
            description = "email address",
            format = "email",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "max.mustermann@example.com"
    )
    @NonNull
    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false, name = "EMAIL")
    private String email;


    @Schema(
            description = "user roles",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NonNull
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
    @Column(nullable = false, name = "IS_ENABLED")
    @Builder.Default
    private boolean enabled = true;


    @Schema(
            description = "identifies if user is locked",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @Column(nullable = false, name = "IS_LOCKED")
    @Builder.Default
    private boolean locked = false;
}
