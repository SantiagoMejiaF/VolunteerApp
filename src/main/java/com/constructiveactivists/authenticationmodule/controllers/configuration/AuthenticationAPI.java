package com.constructiveactivists.authenticationmodule.controllers.configuration;

import com.constructiveactivists.authenticationmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.authenticationmodule.controllers.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación en la aplicación.")
public interface AuthenticationAPI {

    @Operation(summary = "Verificar token de Google")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de Google válido"),
            @ApiResponse(responseCode = "400", description = "Token de Google inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/google")
    ResponseEntity<AuthenticationResponse> authenticationByGoogle(@RequestBody AuthenticationRequest tokenDto);
}
