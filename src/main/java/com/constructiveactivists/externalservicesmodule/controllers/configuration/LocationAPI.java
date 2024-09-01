package com.constructiveactivists.externalservicesmodule.controllers.configuration;

import com.constructiveactivists.externalservicesmodule.models.CityModel;
import com.constructiveactivists.externalservicesmodule.models.DepartmentModel;
import com.constructiveactivists.externalservicesmodule.models.LocalityModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Tag(name = "Módulo de Servicios Externos", description = "Servicios de la aplicación que se comunican con API's externas")
public interface LocationAPI {

    @Operation(summary = "Obtener una lista con todos los departamentos registrados en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/departments")
    Mono<List<DepartmentModel>> getDepartments();

    @Operation(summary = "Obtener el id de un departamento por su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/departments/byName")
    Mono<Optional<Integer>> getDepartmentIdByName(@RequestParam String departmentName);

    @Operation(summary = "Obtener una lista con todas las ciudades por departamento registradas en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/departments/{departmentId}/cities")
    Mono<List<CityModel>> getCitiesByDepartment(Integer departmentid);

    @Operation(summary = "Obtener una lista con todas las localidades registradas en la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/localities/{city}")
    Mono<List<LocalityModel>> getLocalitiesByCity(String cityName);
}
