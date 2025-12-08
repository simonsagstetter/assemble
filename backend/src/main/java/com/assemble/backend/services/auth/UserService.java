package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;

public interface UserService {

    Boolean changePassword( String username, String oldPassword, String newPassword );

    UserDTO findMe( SecurityUser securityUser );

}
