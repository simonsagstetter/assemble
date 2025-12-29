/*
 * assemble
 * BaseEntity.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.core;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "id", source = "id")
@Mapping(target = "createdDate", source = "createdDate")
@Mapping(target = "createdBy", source = "createdBy")
@Mapping(target = "lastModifiedDate", source = "lastModifiedDate")
@Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
public @interface BaseEntity {
}
