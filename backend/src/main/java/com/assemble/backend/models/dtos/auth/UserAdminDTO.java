package com.assemble.backend.models.dtos.auth;

import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.auth.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Schema(name = "UserAdmin", description = "Contains all information about a user including administrative fields")
public class UserAdminDTO {
    private String id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private List<UserRole> roles;
    private Boolean enabled;
    private Boolean locked;
    private Instant createdDate;
    private UserAudit createdBy;
    private Instant lastModifiedDate;
    private UserAudit lastModifiedBy;
}
