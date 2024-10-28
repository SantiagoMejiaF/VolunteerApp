package com.constructiveactivists.dashboardsandreportsmodule.controllers.configuration;

import com.constructiveactivists.dashboardsandreportsmodule.controllers.response.CardsOrganizationVolunteerResponse;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Month;
import java.util.List;
import java.util.Map;


@Tag(name = "Módulo de Tableros de Control y Reportes", description = "Servicios relacionados con los tableros de control que tiene" +
        "el administrador, los voluntarios y las organizaciones junto con sus reportes.")
public interface DashboardOrganizationAPI {

    @Operation(summary = "Obtener el número de organizaciones activas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/active-count")
    ResponseEntity<Long> getActiveOrganizationsCount();


    @Operation(summary = "Obtener el número de misiones completadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/complete-missions-count")
    ResponseEntity<Long>getCompleteMissionsCount();


    @Operation(summary = "Obtener el promedio de horas de una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/average-hours")
    ResponseEntity<Double> getAverageHoursForOrganization(Integer organizationId);


    @Operation(summary = "Obtener el promedio de horas mensuales de una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/average-monthly-hours")
    ResponseEntity<Double> getAverageMonthlyHoursByOrganization(Integer organizationId);

    @Operation(summary = "Obtener los voluntarios de una organización en un mes específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/volunteers")
    ResponseEntity<List<VolunteerOrganizationEntity>> getVolunteersByOrganizationAndMonth(Integer organizationId, Integer month, Integer year);

    @Operation(summary = "Obtener las diez organizaciones más recientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/recent-ten-organizations")
    ResponseEntity<List<OrganizationEntity>> getTenRecentOrganizations();

    @Operation(summary = "Obtener el número de organizaciones por mes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/organizations-count-by-month/{year}")
    ResponseEntity<Map<Month, Long>> getOrganizationsCountByMonth(@PathVariable int year);

    @Operation(summary = "Obtener el número de misiones completadas por una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/completed-missions-count-by-organization/{organizationId}")
    ResponseEntity<Long> getCompletedMissionsCountByOrganization(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener el número de skills de todos los voluntarios aceptados por una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/skill-counts-by-organization/{organizationId}")
    ResponseEntity<Map<SkillEnum, Integer>> getSkillCountsByOrganization(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener el número de voluntarios por disponibilidad aceptados por una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/volunteer-availability-count-by-organization/{organizationId}")
    ResponseEntity<Map<AvailabilityEnum, Long>> getVolunteerAvailabilityCountByOrganization(
            @PathVariable Integer organizationId);

    @Operation(summary = "Obtener el número total de beneficiarios impactados por una organización")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/total-beneficiaries-impacted-by-organization/{organizationId}")
    ResponseEntity<Integer> getTotalBeneficiariesImpactedByOrganization(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener el historial por una organización ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/history-by-organization/{organizationId}")
    ResponseEntity<List<ReviewEntity>> getHistoryByOrganization(@PathVariable Integer organizationId);

    @Operation(summary = "Obtener el historial de un coordinador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/coordinator-review-history/{userId}")
    ResponseEntity<List<ReviewEntity>> getCoordinatorReviewHistory(@PathVariable Integer userId);

    @Operation(summary = "Obtener el número de actividades por organización especificando el año")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/activities-count-by-organization-and-year")
    ResponseEntity<Map<String, Long>> getActivitiesCountByOrganizationAndYear(
            @RequestParam Integer organizationId,
            @RequestParam int year);

    @Operation(summary = "Obtener cards de todas las  organizaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    })
    @GetMapping("/get-cards-all-organizations")
    ResponseEntity<List<CardsOrganizationVolunteerResponse>> getAllOrganizations();
}
