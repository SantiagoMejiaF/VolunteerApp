package com.constructiveactivists.externalservicesmodule.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class GoogleService {

    private final NetHttpTransport transport;
    private final JacksonFactory jacksonFactory;
    private final ObjectMapper objectMapper;

    @Value("${social.googleClientId}")
    private String clientId;

    @Value("${social.googleClientSecret}")
    private String clientSecret;

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=";
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String REDIRECT_URI_CHECKOUT = "https://volunteer-app.online/api/v1/back-volunteer-app/attendances/google/checkout";
    private static final String REDIRECT_URI_CHECKIN  = "https://volunteer-app.online/api/v1/back-volunteer-app/attendances/google/checkin";

    public GoogleService(NetHttpTransport transport, JacksonFactory jacksonFactory, ObjectMapper objectMapper) {
        this.transport = transport;
        this.jacksonFactory = jacksonFactory;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> fetchUserInfo(String accessToken) throws IOException {
        HttpRequestFactory requestFactory = transport.createRequestFactory(request -> request.setParser(new JsonObjectParser(jacksonFactory)));
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(USER_INFO_URL + accessToken));
        HttpResponse response = request.execute();
        return objectMapper.readValue(response.getContent(), new TypeReference<>() {});
    }

    public void redirectToGoogleForCheckIn(HttpServletResponse response, Integer activityId) throws IOException {
        String googleAuthUrl = GOOGLE_AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + REDIRECT_URI_CHECKIN +
                "&response_type=code" +
                "&scope=email profile" +
                "&state=" + activityId;
        response.sendRedirect(googleAuthUrl);
    }

    public void redirectToGoogleForCheckOut(HttpServletResponse response, Integer activityId) throws IOException {
        String googleAuthUrl = GOOGLE_AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + REDIRECT_URI_CHECKOUT +
                "&response_type=code" +
                "&scope=email profile" +
                "&state=" + activityId;
        response.sendRedirect(googleAuthUrl);
    }
    // Método para intercambiar el authorization code por un access token
    public String exchangeAuthorizationCodeForAccessToken(String authorizationCode, boolean isCheckIn) throws IOException {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        String redirectUri = isCheckIn ? REDIRECT_URI_CHECKIN : REDIRECT_URI_CHECKOUT;

        HttpRequestFactory requestFactory = transport.createRequestFactory();
        HttpRequest request = requestFactory.buildPostRequest(
                new GenericUrl(tokenUrl),
                new UrlEncodedContent(Map.of(
                        "code", authorizationCode,
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "redirect_uri", redirectUri,
                        "grant_type", "authorization_code"
                ))
        );

        HttpResponse response = request.execute();
        String responseBody = response.parseAsString();
        Map<String, Object> tokenResponse = objectMapper.readValue(responseBody, new TypeReference<>() {});

        return (String) tokenResponse.get("access_token");
    }
}
