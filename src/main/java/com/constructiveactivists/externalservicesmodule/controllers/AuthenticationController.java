package com.constructiveactivists.externalservicesmodule.controllers;

import com.constructiveactivists.externalservicesmodule.controllers.configuration.AuthenticationAPI;
import com.constructiveactivists.externalservicesmodule.mappers.AuthenticationMapper;
import com.constructiveactivists.externalservicesmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.externalservicesmodule.controllers.response.AuthenticationResponse;
import com.constructiveactivists.externalservicesmodule.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.authentication}")
public class AuthenticationController implements AuthenticationAPI {

    private final AuthenticationMapper authenticationMapper;
    private final AuthenticationService authenticationService;

    @SneakyThrows
    @Override
    public ResponseEntity<AuthenticationResponse> authenticationByGoogle(@Valid AuthenticationRequest authenticationRequest){
        AuthenticationResponse response = authenticationService.authenticationByGoogle(authenticationMapper.toDomain(authenticationRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
