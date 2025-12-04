package com.assemble.backend.models.auth;

import com.assemble.backend.models.core.BaseJPAEntitiy;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
@Schema(name = "User", description = "User entity")
public class User extends BaseJPAEntitiy implements UserDetails {

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
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long")
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map( role -> new SimpleGrantedAuthority( role.getName() ) )
                .toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                "} " + super.toString();
    }
}
