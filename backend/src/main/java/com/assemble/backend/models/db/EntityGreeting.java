package com.assemble.backend.models.db;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "greetings")
public class EntityGreeting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NonNull
    private String message;
}
