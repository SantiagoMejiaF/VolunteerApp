package com.constructiveactivists.usermanagementmodule.controllers.user.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.user.request.TokenRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenRequest toDomain(TokenRequest tokenRequest);
}
