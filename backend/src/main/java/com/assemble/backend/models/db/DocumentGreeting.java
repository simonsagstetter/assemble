package com.assemble.backend.models.db;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(value = "greetings")
public record DocumentGreeting(
        @Id String id,
        @NonNull String message
) {
}
