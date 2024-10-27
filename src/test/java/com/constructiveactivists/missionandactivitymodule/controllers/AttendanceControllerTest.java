package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.externalservicesmodule.services.GoogleService;
import com.constructiveactivists.missionandactivitymodule.services.activity.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private GoogleService googleService;

    @InjectMocks
    private AttendanceController attendanceController;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRedirectToGoogleForCheckIn() throws IOException {
        doNothing().when(googleService).redirectToGoogleForCheckIn(any(HttpServletResponse.class), anyInt());

        attendanceController.redirectToGoogleForCheckIn(1, response);

        verify(googleService, times(1)).redirectToGoogleForCheckIn(response, 1);
    }

    @Test
    void testRedirectToGoogleForCheckOut() throws IOException {
        doNothing().when(googleService).redirectToGoogleForCheckOut(any(HttpServletResponse.class), anyInt());

        attendanceController.redirectToGoogleForCheckOut(1, response);

        verify(googleService, times(1)).redirectToGoogleForCheckOut(response, 1);
    }

    @Test
    void testGoogleCheckInCallback_Success() throws Exception {
        String authorizationCode = "validCode";
        Integer activityId = 1;

        // Mock successful token exchange and user info retrieval
        when(googleService.exchangeAuthorizationCodeForAccessToken(eq(authorizationCode), eq(true)))
                .thenReturn("accessToken");
        when(googleService.fetchUserInfo(anyString()))
                .thenReturn(Map.of("email", "test@example.com"));

        doNothing().when(attendanceService).handleCheckIn(anyString(), anyInt());

        ResponseEntity<String> response = attendanceController.googleCheckInCallback(authorizationCode, activityId);

        assertEquals(MediaType.TEXT_HTML, response.getHeaders().getContentType());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Check-in Registrado Exitosamente"));
        verify(attendanceService, times(1)).handleCheckIn("test@example.com", activityId);
    }

    @Test
    void testGoogleCheckInCallback_AttendanceException() throws Exception {
        String authorizationCode = "validCode";
        Integer activityId = 1;

        // Mock token exchange and user info retrieval
        when(googleService.exchangeAuthorizationCodeForAccessToken(eq(authorizationCode), eq(true)))
                .thenReturn("accessToken");
        when(googleService.fetchUserInfo(anyString()))
                .thenReturn(Map.of("email", "test@example.com"));

        doThrow(new AttendanceException("Check-in failed")).when(attendanceService)
                .handleCheckIn(anyString(), anyInt());

        ResponseEntity<String> response1 = attendanceController.googleCheckInCallback(authorizationCode, activityId);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertTrue(Objects.requireNonNull(response1.getBody()).contains("Error al Procesar el Check-in"));
        verify(attendanceService, times(1)).handleCheckIn("test@example.com", activityId);
    }

    @Test
    void testGoogleCheckInCallback_Exception() throws Exception {
        String authorizationCode = "validCode";
        Integer activityId = 1;

        // Mock token exchange and user info retrieval
        when(googleService.exchangeAuthorizationCodeForAccessToken(eq(authorizationCode), eq(true)))
                .thenReturn("accessToken");
        when(googleService.fetchUserInfo(anyString()))
                .thenReturn(Map.of("email", "test@example.com"));

        doThrow(new RuntimeException("Unknown error")).when(attendanceService)
                .handleCheckIn(anyString(), anyInt());

        ResponseEntity<String> response2 = attendanceController.googleCheckInCallback(authorizationCode, activityId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response2.getStatusCode());
        assertTrue(Objects.requireNonNull(response2.getBody()).contains("Hubo un error al procesar el check-in."));
        verify(attendanceService, times(1)).handleCheckIn("test@example.com", activityId);
    }

    @Test
    void testGoogleCheckOutCallback_Success() throws Exception {
        String authorizationCode = "validCode";
        Integer activityId = 1;

        // Mock successful token exchange and user info retrieval
        when(googleService.exchangeAuthorizationCodeForAccessToken(eq(authorizationCode), eq(false)))
                .thenReturn("accessToken");
        when(googleService.fetchUserInfo(anyString()))
                .thenReturn(Map.of("email", "test@example.com"));

        doNothing().when(attendanceService).handleCheckOut(anyString(), anyInt());

        ResponseEntity<String> response3 = attendanceController.googleCheckOutCallback(authorizationCode, activityId);

        assertEquals(MediaType.TEXT_HTML, response3.getHeaders().getContentType());
        assertTrue(Objects.requireNonNull(response3.getBody()).contains("Check-out Registrado Exitosamente"));
        verify(attendanceService, times(1)).handleCheckOut("test@example.com", activityId);
    }

    @Test
    void testGoogleCheckOutCallback_AttendanceException() throws Exception {
        String authorizationCode = "validCode";
        Integer activityId = 1;

        when(googleService.exchangeAuthorizationCodeForAccessToken(eq(authorizationCode), eq(false)))
                .thenReturn("accessToken");
        when(googleService.fetchUserInfo(anyString()))
                .thenReturn(Map.of("email", "test@example.com"));

        doThrow(new AttendanceException("Check-out failed")).when(attendanceService)
                .handleCheckOut(anyString(), anyInt());

        ResponseEntity<String> response4 = attendanceController.googleCheckOutCallback(authorizationCode, activityId);

        assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode());
        assertTrue(Objects.requireNonNull(response4.getBody()).contains("Error al Procesar el Check-out"));
        verify(attendanceService, times(1)).handleCheckOut("test@example.com", activityId);
    }

    @Test
    void testGoogleCheckOutCallback_Exception() throws Exception {
        String authorizationCode = "validCode";
        Integer activityId = 1;

        // Mock token exchange and user info retrieval
        when(googleService.exchangeAuthorizationCodeForAccessToken(eq(authorizationCode), eq(false)))
                .thenReturn("accessToken");
        when(googleService.fetchUserInfo(anyString()))
                .thenReturn(Map.of("email", "test@example.com"));

        doThrow(new RuntimeException("Unknown error")).when(attendanceService)
                .handleCheckOut(anyString(), anyInt());

        ResponseEntity<String> response5 = attendanceController.googleCheckOutCallback(authorizationCode, activityId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response5.getStatusCode());
        assertTrue(Objects.requireNonNull(response5.getBody()).contains("Hubo un error al procesar el check-out."));
        verify(attendanceService, times(1)).handleCheckOut("test@example.com", activityId);
    }
}
