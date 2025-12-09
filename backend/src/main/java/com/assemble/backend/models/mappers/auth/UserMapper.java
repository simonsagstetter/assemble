package com.assemble.backend.models.mappers.auth;

import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    UserDTO toUserDTO( User user );

}
