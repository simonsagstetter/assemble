package com.assemble.backend.operations;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GreetingOperation {

    @QueryMapping(name = "greetings")
    public List<Greeting> greetings() {
        return List.of( new Greeting( "Hello, world!" ), new Greeting( "Hello, world2!" ) );
    }

    public record Greeting(
            String message
    ) {
    }
}
