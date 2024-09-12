package com.constructiveactivists.missionandactivitymodule.controllers;

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
            @RequestParam("access_token") String accessToken,
            @RequestParam("activityId") Integer activityId) {
        try {
            // Validar el token y obtener la información del usuario
            Map<String, Object> userInfo = googleService.fetchUserInfo(accessToken);
            String email = (String) userInfo.get("email");

            // Registrar el check-in
            attendanceService.handleCheckIn(email, activityId);
            return ResponseEntity.ok("Check-in registrado exitosamente.");
        } catch (Exception e) {
            // Manejar excepciones y devolver mensaje de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el check-in.");
        }
    }

    @GetMapping("/google/checkout")
    public ResponseEntity<String> googleCheckOutCallback(
            @RequestParam("access_token") String accessToken,
            @RequestParam("activityId") Integer activityId) {
        try {
            // Validar el token y obtener la información del usuario
            Map<String, Object> userInfo = googleService.fetchUserInfo(accessToken);
            String email = (String) userInfo.get("email");

            // Registrar el check-out
            attendanceService.handleCheckOut(email, activityId);
            return ResponseEntity.ok("Check-out registrado exitosamente.");
        } catch (Exception e) {
            // Manejar excepciones y devolver mensaje de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el check-out.");
        }
    }

}
