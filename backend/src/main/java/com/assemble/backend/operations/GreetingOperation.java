package com.assemble.backend.operations;

import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GreetingOperation {

    @Autowired
    private DocumentRepository documentRepository;

    @QueryMapping(name = "greetings")
    public List<DocumentGreeting> greetings() {
        return this.documentRepository.findAll();
    }
}
