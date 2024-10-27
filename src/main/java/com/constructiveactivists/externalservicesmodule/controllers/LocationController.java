package com.constructiveactivists.externalservicesmodule.controllers;


import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.externalservicesmodule.controllers.configuration.LocationAPI;
import com.constructiveactivists.externalservicesmodule.models.CityModel;
import com.constructiveactivists.externalservicesmodule.models.DepartmentModel;
import com.constructiveactivists.externalservicesmodule.models.LocalityModel;
import com.constructiveactivists.externalservicesmodule.services.LocationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("${request-mapping.controller.location}")
public class LocationController implements LocationAPI {

    private final LocationService locationService;

    @Override
    public Mono<List<DepartmentModel>> getDepartments() {
        return locationService.getDepartments()
                .onErrorResume(e -> Mono.error(new BusinessException("Error al obtener los departamentos: " + e.getMessage())));
    }

    @Override
    public Mono<Optional<Integer>> getDepartmentIdByName(String departmentName) {
        if (departmentName == null || departmentName.isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del departamento no puede estar vacío"));
        }
        return locationService.getDepartmentIdByName(departmentName)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Departamento no encontrado con el nombre: " + departmentName)))
                .onErrorResume(e -> Mono.error(new BusinessException("Error al obtener el ID del departamento: " + e.getMessage())));
    }

    @Override
    public Mono<List<CityModel>> getCitiesByDepartment(Integer departmentId) {
        if (departmentId == null || departmentId <= 0) {
            return Mono.error(new IllegalArgumentException("ID del departamento no válido"));
        }
        return locationService.getCitiesByDepartment(departmentId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No se encontraron ciudades para el ID de departamento: " + departmentId)))
                .onErrorResume(e -> Mono.error(new BusinessException("Error al obtener las ciudades: " + e.getMessage())));
    }

    @Override
    public Mono<List<LocalityModel>> getLocalitiesByCity(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la ciudad no puede estar vacío"));
        }
        return locationService.getLocalitiesByCity(cityName)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No se encontraron localidades para la ciudad: " + cityName)))
                .onErrorResume(e -> Mono.error(new BusinessException("Error al obtener las localidades: " + e.getMessage())));
    }
}
