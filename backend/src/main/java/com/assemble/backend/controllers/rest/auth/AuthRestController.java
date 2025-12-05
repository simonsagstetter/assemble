package com.assemble.backend.controllers.rest.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login and Logout authentication endpoints")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    @Operation(
            description = "Login",
            summary = "Successful login returns a secure session cookie"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login successful"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Login failed or already authenticated"
    )
    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            headers = {
                    "Accept=" + MediaType.APPLICATION_JSON_VALUE,
                    "Content-Type=" + MediaType.APPLICATION_JSON_VALUE
            }
    )
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest credentials,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        Authentication authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated( credentials.username(), credentials.password() );

        Authentication authResult = authenticationManager.authenticate( authRequest );

        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

        SecurityContext context = strategy.createEmptyContext();
        context.setAuthentication( authResult );
        strategy.setContext( context );

        securityContextRepository.saveContext( context, request, response );

        log.info( "User {} logged in successfully", authResult.getName() );

        return ResponseEntity.ok( "Login successful" );
    }

    @Operation(
            description = "Logout",
            summary = "Successful logout deletes cookies and invalidates session"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Logout successful"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Logout failed or not authenticated"
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping(
            path = "/logout"
    )
    public ResponseEntity<?> logout(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        securityContextLogoutHandler.logout( request, response, authentication );
        log.info( "User {} logged out successfully", authentication.getName() );
        return ResponseEntity.noContent().build();
    }

    public record LoginRequest( String username, String password ) {
    }
}
