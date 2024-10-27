package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.externalservicesmodule.services.GoogleService;
import com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity.AttendanceAPI;
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
public class AttendanceController implements AttendanceAPI {

    private final AttendanceService attendanceService;
    private final GoogleService googleService;


    @Override
    public void redirectToGoogleForCheckIn(@RequestParam("activityId") Integer activityId, HttpServletResponse response) throws IOException {
        googleService.redirectToGoogleForCheckIn(response, activityId);
    }

    @Override
    public void redirectToGoogleForCheckOut(@RequestParam("activityId") Integer activityId, HttpServletResponse response) throws IOException {
        googleService.redirectToGoogleForCheckOut(response, activityId);
    }

    @Override
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

            String successHtml = buildHtmlCheckInMessage(true, activityId, null);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(successHtml);
        } catch (AttendanceException e) {
            // HTML de error con el mensaje de la excepción
            String errorHtml = buildHtmlCheckInMessage(false, activityId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        } catch (Exception e) {
            String errorHtml = buildHtmlCheckInMessage(false, activityId, "Hubo un error al procesar el check-in. Por favor, intente de nuevo más tarde.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        }
    }

    private String buildHtmlCheckInMessage(boolean success, Integer activityId, String errorMessage) {
        String backgroundColor = success ? "#1E1450" : "#ED4B4B";
        String statusMessage = success ? "Check-in Registrado Exitosamente" : "Error al Procesar el Check-in";
        String imageUrl = success ? "https://static.vecteezy.com/system/resources/previews/004/459/449/non_2x/email-confirmation-color-icon-e-mail-approval-response-hiring-letter-email-with-check-mark-employment-verification-letter-isolated-illustration-vector.jpg" : "https://e7.pngegg.com/pngimages/133/899/png-clipart-computer-icons-agar-io-access-denied-logo-shield-thumbnail.png";

        // Mensaje del cuerpo basado en si es un éxito o error
        String bodyMessage;
        if (success) {
            bodyMessage = "El check-in para la actividad con ID " + activityId + " ha sido registrado exitosamente.";
        } else {
            bodyMessage = "Hubo un error al procesar el check-in: " + (errorMessage != null ? escapeHtml(errorMessage) : "Inténtelo nuevamente más tarde.");
        }

        return String.format(
                "<html>" +
                        "<body style='font-family: Inter, sans-serif; color: #000000;'>" +
                        "<div style='background-color: %s; padding: 30px 10px; text-align: center; color: white;'>" +
                        "<span style='font-size: 28px; font-weight: bold;'>%s</span>" +
                        "<br><img src='%s' alt='Check-in Status' style='display: block; margin: 20px auto; width: 50px;'>" +
                        "</div>" +
                        "<p style='color: #000000;'>%s</p>" +
                        "<p style='color: #000000;'>Atentamente,</p>" +
                        "<p style='font-weight: bold; color: #000000;'>Volunteer App</p>" +
                        "</body>" +
                        "</html>",
                backgroundColor, statusMessage, imageUrl, bodyMessage
        );
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

    @Override
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

            String successHtml = buildHtmlCheckOutMessage(true, activityId, null);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(successHtml);
        } catch (AttendanceException e) {
            String errorHtml = buildHtmlCheckOutMessage(false, activityId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        } catch (Exception e) {
            String errorHtml = buildHtmlCheckOutMessage(false, activityId, "Hubo un error al procesar el check-out. Por favor, intente de nuevo más tarde.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        }
    }

    private String buildHtmlCheckOutMessage(boolean success, Integer activityId, String errorMessage) {
        String backgroundColor = success ? "#1E1450" : "#ED4B4B";
        String statusMessage = success ? "Check-out Registrado Exitosamente" : "Error al Procesar el Check-out";

        // Asignar el mensaje de cuerpo basado en si es un éxito o error
        String bodyMessage;
        if (success) {
            bodyMessage = "El check-out para la actividad con ID " + activityId + " ha sido registrado exitosamente.";
        } else {
            bodyMessage = "Hubo un error al procesar el check-out: " + (errorMessage != null ? escapeHtml(errorMessage) : "Inténtelo nuevamente más tarde.");
        }

        return String.format(
                "<html>" +
                        "<body style='font-family: Inter, sans-serif; color: #000000;'>" +
                        "<div style='background-color: %s; padding: 30px 10px; text-align: center; color: white;'>" +
                        "<span style='font-size: 28px; font-weight: bold;'>%s</span>" +
                        "</div>" +
                        "<p style='color: #000000;'>%s</p>" +
                        "<p style='color: #000000;'>Atentamente,</p>" +
                        "<p style='font-weight: bold; color: #000000;'>Volunteer App</p>" +
                        "</body>" +
                        "</html>",
                backgroundColor, statusMessage, bodyMessage
        );
    }



}
