package com.constructiveactivists.externalservicesmodule.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class GoogleService {

    private final NetHttpTransport transport;
    private final JacksonFactory jacksonFactory;
    private final ObjectMapper objectMapper;

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=";

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
}
