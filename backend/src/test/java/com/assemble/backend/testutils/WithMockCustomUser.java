package com.assemble.backend.testutils;

import com.assemble.backend.models.entities.auth.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String userId() default "1";

    String username() default "testuser";

    String firstname() default "Test";

    String lastname() default "User";

    String email() default "test@example.com";

    UserRole[] roles() default { UserRole.USER };
}
