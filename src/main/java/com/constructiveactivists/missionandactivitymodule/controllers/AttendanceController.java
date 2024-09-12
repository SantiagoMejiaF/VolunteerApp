package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.externalservicesmodule.services.GoogleService;
import com.constructiveactivists.missionandactivitymodule.services.activity.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.attendace}")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final GoogleService googleService;


    @GetMapping("/google/auth/checkin")
    public void redirectToGoogleForCheckIn(@RequestParam("activityId") Integer activityId, HttpServletResponse response) throws IOException {
        googleService.redirectToGoogleForCheckIn(response, activityId);
    }

    @GetMapping("/google/auth/checkout")
    public void redirectToGoogleForCheckOut(@RequestParam("activityId") Integer activityId, HttpServletResponse response) throws IOException {
        googleService.redirectToGoogleForCheckOut(response, activityId);
    }

    @GetMapping("/google/checkin")
    public ResponseEntity<String> googleCheckInCallback(
            @RequestParam("code") String authorizationCode,
            @RequestParam("state") Integer activityId) {
        try {
            // Intercambiar el authorization code por un access token para Check-in
            String accessToken = googleService.exchangeAuthorizationCodeForAccessToken(authorizationCode, true);

            // Validar el token y obtener la información del usuario
            Map<String, Object> userInfo = googleService.fetchUserInfo(accessToken);
            String email = (String) userInfo.get("email");

            System.out.println("Email: " + email);
            System.out.println("Activity ID: " + activityId);
            // Registrar el check-in
            attendanceService.handleCheckIn(email, activityId);
            return ResponseEntity.ok("Check-in registrado exitosamente.");
        } catch (AttendanceException e) {
            // Mensajes de error específicos para problemas en el registro de asistencia
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el check-in: " + e.getMessage());
        } catch (Exception e) {
            // Mensaje de error genérico
            e.printStackTrace(); // Esto es útil para la depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el check-in: " + e.getMessage());
        }
    }

    @GetMapping("/google/checkout")
    public ResponseEntity<String> googleCheckOutCallback(
            @RequestParam("code") String authorizationCode,
            @RequestParam("state") Integer activityId) {
        try {
            // Intercambiar el authorization code por un access token para Check-out
            String accessToken = googleService.exchangeAuthorizationCodeForAccessToken(authorizationCode, false);

            // Validar el token y obtener la información del usuario
            Map<String, Object> userInfo = googleService.fetchUserInfo(accessToken);
            String email = (String) userInfo.get("email");

            // Registrar el check-out
            attendanceService.handleCheckOut(email, activityId);
            return ResponseEntity.ok("Check-out registrado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el check-out.");
        }
    }

}
