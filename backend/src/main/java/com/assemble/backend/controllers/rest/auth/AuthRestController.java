package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.dtos.global.ErrorResponse;
import com.assemble.backend.dtos.auth.LoginRequest;
import com.assemble.backend.dtos.auth.LoginResponse;
import com.assemble.backend.dtos.global.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            description = "Login successful",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = LoginResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ValidationErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Invalid Credentials",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "403",
            description = "Access Denied",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
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
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest credentials,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        Authentication authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated( credentials.getUsername(), credentials.getPassword() );

        Authentication authResult = authenticationManager.authenticate( authRequest );

        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

        SecurityContext context = strategy.createEmptyContext();
        context.setAuthentication( authResult );
        strategy.setContext( context );

        securityContextRepository.saveContext( context, request, response );

        log.info( "User {} logged in successfully", authResult.getName() );

        LoginResponse loginResponse = LoginResponse.builder()
                .statusCode( HttpStatus.OK.value() )
                .statusText( HttpStatus.OK.getReasonPhrase() )
                .sessionId( request.getSession().getId() )
                .build();

        return ResponseEntity.ok( loginResponse );
    }

    @Operation(
            description = "Logout",
            summary = "Successful logout deletes cookies and invalidates session"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Logout successful"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Not authenticated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping(
            path = "/logout"
    )
    public ResponseEntity<Void> logout(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        securityContextLogoutHandler.logout( request, response, authentication );
        log.info( "User {} logged out successfully", authentication.getName() );

        return ResponseEntity.noContent().build();
    }
}
