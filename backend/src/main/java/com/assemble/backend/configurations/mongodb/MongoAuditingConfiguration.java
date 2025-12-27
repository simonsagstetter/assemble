/*
 * assemble
 * MongoAuditingConfiguration.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.mongodb;

import lombok.Generated;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Generated
@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorAwareImpl")
public class MongoAuditingConfiguration {
}
