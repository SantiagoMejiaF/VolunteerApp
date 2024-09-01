package com.constructiveactivists.externalservicesmodule.mappers;

import com.constructiveactivists.externalservicesmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.externalservicesmodule.models.TokenModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    TokenModel toDomain(AuthenticationRequest authenticationRequest);
}
