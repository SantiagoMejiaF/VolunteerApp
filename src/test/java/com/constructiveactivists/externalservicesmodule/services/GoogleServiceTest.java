package com.constructiveactivists.externalservicesmodule.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GoogleServiceTest {

    @Mock
    private NetHttpTransport transport;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GoogleService googleService;

    @Mock
    private HttpRequestFactory requestFactory;

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private HttpServletResponse servletResponse;

    private String authorizationCode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(googleService, "clientId", "yourClientId");
        ReflectionTestUtils.setField(googleService, "clientSecret", "yourClientSecret");

        authorizationCode = "authCode";
    }

    @Test
    void testFetchUserInfo() throws IOException {

        Map<String, Object> userInfo = Map.of("email", "user@example.com", "name", "John Doe");

        when(transport.createRequestFactory(any())).thenReturn(requestFactory);
        when(requestFactory.buildGetRequest(any())).thenReturn(httpRequest);
        when(httpRequest.execute()).thenReturn(httpResponse);
        when(httpResponse.getContent()).thenReturn(mock(InputStream.class));
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(userInfo);

        String accessToken = "fakeAccessToken";
        Map<String, Object> result = googleService.fetchUserInfo(accessToken);

        assertNotNull(result);
        assertEquals("user@example.com", result.get("email"));
        assertEquals("John Doe", result.get("name"));

        verify(requestFactory, times(1)).buildGetRequest(any());
        verify(httpRequest, times(1)).execute();
        verify(objectMapper, times(1)).readValue(any(InputStream.class), any(TypeReference.class));
    }

    @Test
    void testRedirectToGoogleForCheckIn() throws IOException {
        Integer activityId = 123;

        doNothing().when(servletResponse).sendRedirect(anyString());

        googleService.redirectToGoogleForCheckIn(servletResponse, activityId);

        verify(servletResponse, times(1)).sendRedirect(contains("state=" + activityId));
    }

    @Test
    void testRedirectToGoogleForCheckOut() throws IOException {
        Integer activityId = 456;

        doNothing().when(servletResponse).sendRedirect(anyString());

        googleService.redirectToGoogleForCheckOut(servletResponse, activityId);

        verify(servletResponse, times(1)).sendRedirect(contains("state=" + activityId));
    }

    @Test
    void testExchangeAuthorizationCodeForAccessToken() throws IOException {

        Map<String, Object> tokenResponse = Map.of("access_token", "testAccessToken");

        when(transport.createRequestFactory()).thenReturn(requestFactory);
        when(requestFactory.buildPostRequest(any(), any())).thenReturn(httpRequest);
        when(httpRequest.execute()).thenReturn(httpResponse);
        when(httpResponse.parseAsString()).thenReturn("{\"access_token\":\"testAccessToken\"}");
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(tokenResponse);

        String accessToken = googleService.exchangeAuthorizationCodeForAccessToken(authorizationCode, true);

        assertNotNull(accessToken);
        assertEquals("testAccessToken", accessToken);

        verify(requestFactory, times(1)).buildPostRequest(any(), any());
        verify(httpRequest, times(1)).execute();
        verify(objectMapper, times(1)).readValue(anyString(), any(TypeReference.class));
    }
}
