package com.constructiveactivists.externalservicesmodule.services;

import com.constructiveactivists.externalservicesmodule.models.CityModel;
import com.constructiveactivists.externalservicesmodule.models.DepartmentModel;
import com.constructiveactivists.externalservicesmodule.models.LocalityModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    private static final String BASE_URL = "https://api-colombia.com/api/v1";
    private static final String DEPARTMENTS_URI = "/Department";
    private static final String CITIES_BY_DEPARTMENT_URI = "/Department/{departmentId}/cities";
    private static final String LOCALITIES_BASE_URL = "https://overpass-api.de/api/interpreter?data=";
    private static final String LOCALITIES_QUERY_FORMAT = "[out:json][timeout:25];area[\"name\"=\"%s\"]->.searchArea;(relation[\"boundary\"=\"administrative\"][\"admin_level\"=\"8\"](area.searchArea););out tags;";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public LocationService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.objectMapper = objectMapper;
    }

    public Mono<List<DepartmentModel>> getDepartments() {
        return webClient.get()
                .uri(DEPARTMENTS_URI)
                .retrieve()
                .bodyToFlux(DepartmentModel.class)
                .collectList();
    }

    public Mono<List<CityModel>> getCitiesByDepartment(Integer departmentId) {
        return webClient.get()
                .uri(CITIES_BY_DEPARTMENT_URI, departmentId)
                .retrieve()
                .bodyToFlux(CityModel.class)
                .collectList();
    }

    public Mono<Optional<Integer>> getDepartmentIdByName(String departmentName) {
        return getDepartments()
                .map(departments -> departments.stream()
                        .filter(department -> department.getName().equalsIgnoreCase(departmentName))
                        .map(DepartmentModel::getId)
                        .findFirst()
                );
    }

    public Mono<List<LocalityModel>> getLocalitiesByCity(String cityName) {
        String url = LOCALITIES_BASE_URL + String.format(LOCALITIES_QUERY_FORMAT, cityName);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(this::parseToLocalityModels)
                .collectList();
    }

    private Flux<LocalityModel> parseToLocalityModels(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode elementsNode = rootNode.path("elements");
            return Flux.fromIterable(objectMapper.convertValue(elementsNode, new TypeReference<List<LocalityModel>>() {}));
        } catch (IOException e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }
}
