package com.assemble.backend.models.auth;

import com.assemble.backend.models.core.BaseJPAEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER_ROLE")
@Schema(name = "UserRole", description = "User roles entity")
public class UserRole implements Serializable {

    @Schema(
            description = "unique identifier of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "123e4567e89b12d3a456426655440000"
    )
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Schema(
            description = "unique identifier of a user role",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "USER",
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @Column(name = "NAME", unique = true, nullable = false, updatable = false)
    @NotNull
    private String name;

    public UserRole( String name ) {
        this.id = null;
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
