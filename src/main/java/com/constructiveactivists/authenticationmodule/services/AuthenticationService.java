package com.constructiveactivists.authenticationmodule.services;

import com.constructiveactivists.externalservicesmodule.services.GoogleService;
import com.constructiveactivists.securitymodule.service.CustomUserDetailsService;
import com.constructiveactivists.securitymodule.model.JwtUtil;
import com.constructiveactivists.authenticationmodule.controllers.response.AuthenticationResponse;
import com.constructiveactivists.authenticationmodule.models.TokenModel;
import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.RoleService;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService customUserDetailsService;
    private final GoogleService googleService;

    private static final RoleType DEFAULT_ROLE_TYPE = RoleType.SIN_ASIGNAR;

    public AuthenticationResponse  authenticationByGoogle(TokenModel tokenDto) throws IOException {

        Map<String, Object> userInfo = googleService.fetchUserInfo(tokenDto.getValue());
        String email = (String) userInfo.get("email");
        UserEntity userEntity = userService.findByEmail(email).orElseGet(() -> registerNewUser(userInfo));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEntity.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtUtil.generateToken(authentication);
        return new AuthenticationResponse(token, userEntity);
    }

    private UserEntity registerNewUser(Map<String, Object> userInfo) {
        RoleEntity defaultRole = createDefaultRole();
        UserEntity user = buildUserEntity(userInfo, defaultRole.getId());
        return userService.saveUser(user);
    }

    private RoleEntity createDefaultRole() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleType(DEFAULT_ROLE_TYPE);
        return roleService.createRole(roleEntity);
    }

    private UserEntity buildUserEntity(Map<String, Object> userInfo, Integer roleId) {
        UserEntity user = new UserEntity();
        user.setRoleId(roleId);
        user.setAuthorizationType(AuthorizationStatus.PENDIENTE);
        user.setFirstName((String) userInfo.get("given_name"));
        user.setLastName((String) userInfo.get("family_name"));
        user.setEmail((String) userInfo.get("email"));
        user.setImage((String) userInfo.get("picture"));
        user.setRegistrationDate(java.time.LocalDate.now());
        return user;
    }
}

