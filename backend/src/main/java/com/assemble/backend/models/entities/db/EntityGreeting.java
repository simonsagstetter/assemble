package com.assemble.backend.models.entities.db;

import com.assemble.backend.models.entities.core.BaseJPAEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "greetings")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EntityGreeting extends BaseJPAEntity {

    @NonNull
    @NotBlank
    private String message;
}
