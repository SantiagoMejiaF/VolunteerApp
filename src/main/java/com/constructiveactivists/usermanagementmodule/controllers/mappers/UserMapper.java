package com.constructiveactivists.usermanagementmodule.controllers.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain (UserRequest userRequest);
}
