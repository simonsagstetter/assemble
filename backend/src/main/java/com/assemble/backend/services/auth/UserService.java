package com.assemble.backend.services.auth;

import com.assemble.backend.models.auth.SecurityUser;
import com.assemble.backend.models.auth.User;

public interface UserService {

    Boolean changePassword( String username, String oldPassword, String newPassword );

    Boolean requestPasswordReset( String username );

    Boolean completePasswordReset( String token, String newPassword );

    User findMe( SecurityUser securityUser );

    User updateUser( User user );

}
