package com.assemble.backend.testutils;

import com.assemble.backend.models.auth.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String userId() default "1";

    String username() default "testuser";

    UserRole[] roles() default { UserRole.USER };
}
