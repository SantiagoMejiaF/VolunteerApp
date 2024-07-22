package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.controllers.user.configuration.SocialProperties;
import com.constructiveactivists.usermanagementmodule.controllers.user.request.TokenRequest;
import com.constructiveactivists.usermanagementmodule.controllers.user.request.UserGoogleRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SocialProperties socialProperties;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<UserGoogleRequest> google(TokenRequest tokenDto) {
        try {
            final NetHttpTransport transport = new NetHttpTransport();
            final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
            HttpRequestFactory requestFactory = transport.createRequestFactory(request -> request.setParser(new JsonObjectParser(jacksonFactory)));

            String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + tokenDto.getValue();
            HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(userInfoUrl));
            HttpResponse response = request.execute();

            if (response.getStatusCode() != 200) {
                throw new IOException("Failed to get user info: " + response.getStatusMessage());
            }


            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> userInfo = objectMapper.readValue(response.getContent(), new TypeReference<>() {
            });


            UserGoogleRequest user = new UserGoogleRequest();
            user.setEmail((String) userInfo.get("email"));
            user.setEmailVerified((Boolean) userInfo.get("email_verified"));
            user.setId((String) userInfo.get("sub"));
            user.setName((String) userInfo.get("name"));
            user.setPicture((String) userInfo.get("picture"));

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    public ResponseEntity<User> facebook(TokenRequest tokenDto){
    try {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        User user = facebook.fetchObject("me", User.class);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }
}
