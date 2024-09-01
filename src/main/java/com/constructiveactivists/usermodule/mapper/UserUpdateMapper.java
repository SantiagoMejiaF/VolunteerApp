package com.constructiveactivists.usermodule.mapper;

import com.constructiveactivists.usermodule.controllers.request.UserRequest;
import com.constructiveactivists.usermodule.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {
    UserEntity toDomain(UserRequest userRequest);
}
