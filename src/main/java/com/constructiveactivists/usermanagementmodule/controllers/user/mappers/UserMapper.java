package com.constructiveactivists.usermanagementmodule.controllers.user.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.user.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toDomain (UserRequest userRequest);
}
