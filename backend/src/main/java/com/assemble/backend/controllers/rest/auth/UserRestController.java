package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.models.dtos.auth.ChangePasswordDTO;
import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.dtos.global.ErrorResponse;
import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.services.auth.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Basic user endpoints")
public class UserRestController {

    private final UserServiceImpl userService;

    @Operation(
            description = "Returns a user dto with basic information about the authenticated"
                    + " user that made the request",
            summary = "Retrieve basic user information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Not authenticated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping(
            path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDTO> me( @AuthenticationPrincipal SecurityUser user ) {
        return ResponseEntity.ok( userService.findMe( user ) );
    }

    @Operation(
            summary = "Changes the current password",
            description = "Changes the password of the currently authenticated user. " +
                    "Body must contain the correct old password and a valid new password"
    )
    @ApiResponse(
            responseCode = "204",
            description = "No Content"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ValidationErrorResponse.class)
            )
    )
    @PostMapping(
            path = "/change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Boolean> changePassword(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO ) {
        return ResponseEntity.status( HttpStatus.NO_CONTENT )
                .body( userService.changePassword(
                        user.getUsername(),
                        changePasswordDTO.getOldPassword(),
                        changePasswordDTO.getNewPassword()
                ) );
    }

}
