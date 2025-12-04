package com.assemble.backend.configurations.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorAwareImpl")
public class MongoAuditingConfiguration {
}
