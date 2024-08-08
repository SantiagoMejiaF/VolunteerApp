package com.constructiveactivists.authenticationmodule.services;

import com.constructiveactivists.authenticationmodule.models.TokenModel;
import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.RoleService;
import com.constructiveactivists.usermanagementmodule.services.UserService;
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
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final NetHttpTransport transport;
    private final JacksonFactory jacksonFactory;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final RoleService roleService;

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=";
    private static final RoleType DEFAULT_ROLE_TYPE = RoleType.SIN_ASIGNAR;

    public UserEntity authenticationByGoogle(TokenModel tokenDto) throws IOException {

        Map<String, Object> userInfo = fetchUserInfo(tokenDto.getValue());
        String email = (String) userInfo.get("email");

        return userService.findByEmail(email).orElseGet(() -> registerNewUser(userInfo));
    }

    private Map<String, Object> fetchUserInfo(String accessToken) throws IOException {

        HttpRequestFactory requestFactory = transport.createRequestFactory(request -> request.setParser(new JsonObjectParser(jacksonFactory)));
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(USER_INFO_URL + accessToken));
        HttpResponse response = request.execute();
        return objectMapper.readValue(response.getContent(), new TypeReference<>() {});
    }

    private UserEntity registerNewUser(Map<String, Object> userInfo) {

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleType(DEFAULT_ROLE_TYPE);
        RoleEntity defaultRole = roleService.createRole(roleEntity);

        UserEntity user = new UserEntity();
        user.setRoleId(defaultRole.getId());
        user.setAuthorizationType(AuthorizationStatus.PENDIENTE);
        user.setFirstName((String) userInfo.get("given_name"));
        user.setLastName((String) userInfo.get("family_name"));
        user.setEmail((String) userInfo.get("email"));
        user.setImage((String) userInfo.get("picture"));
        user.setRegistrationDate(java.time.LocalDate.now());

        return userService.saveUser(user);
    }
}
