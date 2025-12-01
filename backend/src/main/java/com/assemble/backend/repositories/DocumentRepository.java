package com.assemble.backend.repositories;

import com.assemble.backend.models.db.DocumentGreeting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DocumentGreeting, String> {
}
