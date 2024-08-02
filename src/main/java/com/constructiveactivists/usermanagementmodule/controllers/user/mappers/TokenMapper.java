package com.constructiveactivists.usermanagementmodule.controllers.user.mappers;

import com.constructiveactivists.usermanagementmodule.controllers.user.request.TokenRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.TokenModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    TokenModel toDomain(TokenRequest tokenRequest);
}
