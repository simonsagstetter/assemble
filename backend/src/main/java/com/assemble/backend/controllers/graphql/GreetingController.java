/*
 * assemble
 * GreetingController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.graphql;

import com.assemble.backend.models.entities.db.DocumentGreeting;
import com.assemble.backend.repositories.db.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GreetingController {

    @Autowired
    private DocumentRepository documentRepository;

    @QueryMapping(name = "greetings")
    public List<DocumentGreeting> greetings() {
        return this.documentRepository.findAll();
    }
}
