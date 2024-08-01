package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.controllers.user.configuration.SocialProperties;
import com.constructiveactivists.usermanagementmodule.controllers.user.request.TokenRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.user.enums.Role;
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

    public UserEntity google(TokenRequest tokenDto) throws IOException {

            final NetHttpTransport transport = new NetHttpTransport();
            final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
            HttpRequestFactory requestFactory = transport.createRequestFactory(request -> request.setParser(new JsonObjectParser(jacksonFactory)));
            String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + tokenDto.getValue();
            HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(userInfoUrl));
            HttpResponse response = request.execute();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> userInfo = objectMapper.readValue(response.getContent(), new TypeReference<>() {
            });

            UserEntity user = new UserEntity();
            user.setEmail((String) userInfo.get("email"));
            user.setFirstName((String) userInfo.get("given_name"));
            user.setLastName((String) userInfo.get("family_name"));
            user.setRegistrationDate(java.time.LocalDate.now());
            user.setRole(Role.valueOf("NINGUNO"));
            userRepository.save(user);
            return user;
    }


}
