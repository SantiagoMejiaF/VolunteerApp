package com.constructiveactivists.externalservicesmodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.externalservicesmodule.models.CityModel;
import com.constructiveactivists.externalservicesmodule.models.DepartmentModel;
import com.constructiveactivists.externalservicesmodule.models.LocalityModel;
import com.constructiveactivists.externalservicesmodule.services.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    private DepartmentModel departmentModel;
    private CityModel cityModel;
    private LocalityModel localityModel;

    @BeforeEach
    void setUp() {
        departmentModel = new DepartmentModel();
        departmentModel.setId(1);
        departmentModel.setName("Bogotá");

        cityModel = new CityModel();
        cityModel.setName("Bogotá");

        localityModel = new LocalityModel();
        LocalityModel.Tags tags = new LocalityModel.Tags();
        tags.setName("Chapinero");
        localityModel.setTags(tags);
    }

    @Test
    void testGetDepartments_Success() {
        when(locationService.getDepartments()).thenReturn(Mono.just(List.of(departmentModel)));

        Mono<List<DepartmentModel>> result = locationController.getDepartments();

        assertNotNull(result);
        List<DepartmentModel> departmentList = result.block();
        assert departmentList != null;
        assertEquals(1, departmentList.size());
        assertEquals(departmentModel.getName(), departmentList.get(0).getName());

        verify(locationService, times(1)).getDepartments();
    }

    @Test
    void testGetDepartments_Error() {
        when(locationService.getDepartments()).thenReturn(Mono.error(new RuntimeException("Internal Error")));

        Mono<List<DepartmentModel>> result = locationController.getDepartments();

        Exception exception = assertThrows(BusinessException.class, result::block);
        assertTrue(exception.getMessage().contains("Error al obtener los departamentos"));

        verify(locationService, times(1)).getDepartments();
    }

    @Test
    void testGetDepartmentIdByName_Success() {
        when(locationService.getDepartmentIdByName("Bogotá")).thenReturn(Mono.just(Optional.of(1)));

        Mono<Optional<Integer>> result = locationController.getDepartmentIdByName("Bogotá");

        Optional<Integer> departmentId = result.block();
        assert Objects.requireNonNull(departmentId).isPresent();
        assertTrue(true);
        assertEquals(1, departmentId.get());

        verify(locationService, times(1)).getDepartmentIdByName("Bogotá");
    }

    @Test
    void testGetDepartmentIdByName_NotFound() {
        when(locationService.getDepartmentIdByName("Unknown")).thenReturn(Mono.empty());

        Mono<Optional<Integer>> result = locationController.getDepartmentIdByName("Unknown");

        BusinessException exception = assertThrows(BusinessException.class, result::block);
        assertTrue(exception.getMessage().contains("Departamento no encontrado"));

        verify(locationService, times(1)).getDepartmentIdByName("Unknown");
    }

    @Test
    void testGetCitiesByDepartment_Success() {
        when(locationService.getCitiesByDepartment(1)).thenReturn(Mono.just(List.of(cityModel)));

        Mono<List<CityModel>> result = locationController.getCitiesByDepartment(1);

        List<CityModel> cities = result.block();
        assertEquals(1, cities.size());
        assertEquals(cityModel.getName(), cities.get(0).getName());

        verify(locationService, times(1)).getCitiesByDepartment(1);
    }

    @Test
    void testGetCitiesByDepartment_InvalidDepartmentId() {
        Mono<List<CityModel>> result = locationController.getCitiesByDepartment(-1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("ID del departamento no válido"));

        verify(locationService, times(0)).getCitiesByDepartment(-1);
    }

    @Test
    void testGetLocalitiesByCity_Success() {
        when(locationService.getLocalitiesByCity("Bogotá")).thenReturn(Mono.just(List.of(localityModel)));

        Mono<List<LocalityModel>> result = locationController.getLocalitiesByCity("Bogotá");

        List<LocalityModel> localities = result.block();
        assert localities != null;
        assertEquals(1, localities.size());
        assertEquals(localityModel.getTags().getName(), localities.get(0).getTags().getName());

        verify(locationService, times(1)).getLocalitiesByCity("Bogotá");
    }

    @Test
    void testGetLocalitiesByCity_InvalidCityName() {
        Mono<List<LocalityModel>> result = locationController.getLocalitiesByCity("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("El nombre de la ciudad no puede estar vacío"));

        verify(locationService, times(0)).getLocalitiesByCity("");
    }

    @Test
    void testGetLocalitiesByCity_NotFound() {
        when(locationService.getLocalitiesByCity("Unknown")).thenReturn(Mono.empty());

        Mono<List<LocalityModel>> result = locationController.getLocalitiesByCity("Unknown");

        BusinessException exception = assertThrows(BusinessException.class, result::block);
        assertTrue(exception.getMessage().contains("No se encontraron localidades"));

        verify(locationService, times(1)).getLocalitiesByCity("Unknown");
    }

    @Test
    void testGetDepartmentIdByName_EmptyName() {
        Mono<Optional<Integer>> result = locationController.getDepartmentIdByName("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("El nombre del departamento no puede estar vacío"));

        verify(locationService, times(0)).getDepartmentIdByName(anyString());
    }

    @Test
    void testGetCitiesByDepartment_NullDepartmentId() {
        Mono<List<CityModel>> result = locationController.getCitiesByDepartment(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("ID del departamento no válido"));

        verify(locationService, times(0)).getCitiesByDepartment(any());
    }

    @Test
    void testGetLocalitiesByCity_EmptyCityName() {
        Mono<List<LocalityModel>> result = locationController.getLocalitiesByCity("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("El nombre de la ciudad no puede estar vacío"));

        verify(locationService, times(0)).getLocalitiesByCity(anyString());
    }

    @Test
    void testGetCitiesByDepartment_ZeroDepartmentId() {
        Mono<List<CityModel>> result = locationController.getCitiesByDepartment(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("ID del departamento no válido"));

        verify(locationService, times(0)).getCitiesByDepartment(anyInt());
    }

    @Test
    void testGetDepartmentIdByName_NullName() {
        Mono<Optional<Integer>> result = locationController.getDepartmentIdByName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("El nombre del departamento no puede estar vacío"));

        verify(locationService, times(0)).getDepartmentIdByName(anyString());
    }

    @Test
    void testGetLocalitiesByCity_NullCityName() {
        Mono<List<LocalityModel>> result = locationController.getLocalitiesByCity(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, result::block);
        assertTrue(exception.getMessage().contains("El nombre de la ciudad no puede estar vacío"));

        verify(locationService, times(0)).getLocalitiesByCity(anyString());
    }

    @Test
    void testGetCitiesByDepartment_ServiceError() {
        when(locationService.getCitiesByDepartment(1)).thenReturn(Mono.error(new RuntimeException("Error en el servicio")));

        Mono<List<CityModel>> result = locationController.getCitiesByDepartment(1);

        BusinessException exception = assertThrows(BusinessException.class, result::block);
        assertTrue(exception.getMessage().contains("Error al obtener las ciudades: Error en el servicio"));

        verify(locationService, times(1)).getCitiesByDepartment(1);
    }
}
