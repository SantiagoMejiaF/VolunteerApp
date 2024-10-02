package com.constructiveactivists.externalservicesmodule.controllers.configuration;

import com.constructiveactivists.externalservicesmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.externalservicesmodule.controllers.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Módulo de Servicios Externos", description = "Servicios de la aplicación que se comunican con API's externas")
public interface AuthenticationAPI {

    @Operation(summary = "Registrarse o iniciar sesión con Google",
            description = "Permite a los usuarios registrarse o iniciar sesión en la aplicación utilizando su cuenta de Google.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de Google válido"),
            @ApiResponse(responseCode = "400", description = "Token de Google inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/google")
    ResponseEntity<AuthenticationResponse> authenticationByGoogle(@RequestBody AuthenticationRequest tokenDto);
}
