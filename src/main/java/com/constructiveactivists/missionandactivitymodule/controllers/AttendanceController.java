package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.externalservicesmodule.services.GoogleService;
import com.constructiveactivists.missionandactivitymodule.services.activity.AttendanceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

            attendanceService.handleCheckIn(email, activityId);

            String successHtml = "<html>" +
                    "<body>" +
                    "<h1>Check-in Registrado Exitosamente</h1>" +
                    "<p>El check-in para la actividad con ID " + activityId + " ha sido registrado exitosamente.</p>" +
                    "</body>" +
                    "</html>";

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(successHtml);
        } catch (AttendanceException e) {
            // HTML de error con el mensaje de la excepción
            String errorHtml = "<html>" +
                    "<body>" +
                    "<h1>Error al Procesar el Check-in</h1>" +
                    "<p>Hubo un error al procesar el check-in: " + escapeHtml(e.getMessage()) + "</p>" +
                    "</body>" +
                    "</html>";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        } catch (Exception e) {
            String errorHtml = "<html>" +
                    "<body>" +
                    "<h1>Error al Procesar el Check-in</h1>" +
                    "<p>Hubo un error al procesar el check-in. Por favor, intente de nuevo más tarde.</p>" +
                    "</body>" +
                    "</html>";
            // Mensaje de error genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);        }
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
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
