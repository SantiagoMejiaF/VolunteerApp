package com.constructiveactivists.authenticationmodule.controllers.mappers;

import com.constructiveactivists.authenticationmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.authenticationmodule.models.TokenModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    TokenModel toDomain(AuthenticationRequest authenticationRequest);
}
