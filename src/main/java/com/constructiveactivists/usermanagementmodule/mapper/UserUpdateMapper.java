package com.constructiveactivists.usermanagementmodule.mapper;

import com.constructiveactivists.usermanagementmodule.controllers.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {
    UserEntity toDomain(UserRequest userRequest);
}
