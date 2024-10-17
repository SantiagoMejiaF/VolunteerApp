package com.constructiveactivists.externalservicesmodule.controllers;

import com.constructiveactivists.externalservicesmodule.controllers.request.AuthenticationRequest;
import com.constructiveactivists.externalservicesmodule.controllers.response.AuthenticationResponse;
import com.constructiveactivists.externalservicesmodule.mappers.AuthenticationMapper;
import com.constructiveactivists.externalservicesmodule.models.TokenModel;
import com.constructiveactivists.externalservicesmodule.services.AuthenticationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationMapper authenticationMapper;

    private AuthenticationRequest authenticationRequest;
    private TokenModel tokenModel;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setValue("googleToken123");

        tokenModel = new TokenModel("googleToken123");

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setEmail("testuser@example.com");

        authenticationResponse = new AuthenticationResponse("jwtToken123", user);
    }

    @Test
    void testAuthenticationByGoogle_Success() throws Exception {

        when(authenticationMapper.toDomain(authenticationRequest)).thenReturn(tokenModel);
        when(authenticationService.authenticationByGoogle(tokenModel)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.authenticationByGoogle(authenticationRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("jwtToken123", responseEntity.getBody().getToken());
        assertEquals("testuser@example.com", responseEntity.getBody().getUser().getEmail());

        verify(authenticationMapper, times(1)).toDomain(authenticationRequest);
        verify(authenticationService, times(1)).authenticationByGoogle(tokenModel);
    }

    @Test
    void testAuthenticationByGoogle_ThrowsException() throws Exception {

        when(authenticationMapper.toDomain(authenticationRequest)).thenReturn(tokenModel);
        when(authenticationService.authenticationByGoogle(tokenModel)).thenThrow(new RuntimeException("Error de autenticación"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticationController.authenticationByGoogle(authenticationRequest);
        });

        assertTrue(exception.getMessage().contains("Error de autenticación"));

        verify(authenticationMapper, times(1)).toDomain(authenticationRequest);
        verify(authenticationService, times(1)).authenticationByGoogle(tokenModel);
    }
}
