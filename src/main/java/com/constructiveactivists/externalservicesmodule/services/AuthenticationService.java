package com.constructiveactivists.externalservicesmodule.services;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.configurationmodule.security.service.CustomUserDetailsService;
import com.constructiveactivists.configurationmodule.security.model.JwtUtil;
import com.constructiveactivists.externalservicesmodule.controllers.response.AuthenticationResponse;
import com.constructiveactivists.externalservicesmodule.models.TokenModel;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.RoleService;
import com.constructiveactivists.usermodule.services.UserService;
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
    private static final RoleType COORDINATOR_ROLE_TYPE = RoleType.COORDINADOR_ACTIVIDAD;
    private static final String CORREO = "email";

    public AuthenticationResponse authenticationByGoogle(TokenModel tokenDto) throws IOException {
        return authenticateUser(tokenDto, DEFAULT_ROLE_TYPE);
    }

    public AuthenticationResponse authenticationByGoogleCoordinador(TokenModel tokenDto) throws IOException {
        return authenticateNewUser(tokenDto, COORDINATOR_ROLE_TYPE);
    }

    private AuthenticationResponse authenticateUser(TokenModel tokenDto, RoleType roleType) throws IOException {
        Map<String, Object> userInfo = googleService.fetchUserInfo(tokenDto.getValue());
        String email = (String) userInfo.get(CORREO);
        UserEntity userEntity = userService.findByEmail(email).orElseGet(() -> registerNewUser(userInfo, roleType));
        return createAuthenticationResponse(userEntity);
    }

    private AuthenticationResponse authenticateNewUser(TokenModel tokenDto, RoleType roleType) throws IOException {
        Map<String, Object> userInfo = googleService.fetchUserInfo(tokenDto.getValue());
        String email = (String) userInfo.get(CORREO);
        if (userService.findByEmail(email).isPresent()) {
            throw new BusinessException("El usuario con este correo electrónico ya está registrado");
        }
        UserEntity userEntity = registerNewUser(userInfo, roleType);
        return createAuthenticationResponse(userEntity);
    }

    private AuthenticationResponse createAuthenticationResponse(UserEntity userEntity) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEntity.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtUtil.generateToken(authentication);
        return new AuthenticationResponse(token, userEntity);
    }

    private UserEntity registerNewUser(Map<String, Object> userInfo, RoleType roleType) {
        RoleEntity roleEntity = createRole(roleType);
        return userService.saveUser(buildUserEntity(userInfo, roleEntity.getId()));
    }

    private RoleEntity createRole(RoleType roleType) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleType(roleType);
        return roleService.createRole(roleEntity);
    }

    private UserEntity buildUserEntity(Map<String, Object> userInfo, Integer roleId) {
        RoleEntity role = roleService.getRoleById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado para el roleId proporcionado: " + roleId));
        UserEntity user = new UserEntity();
        user.setRole(role);
        user.setAuthorizationType(AuthorizationStatus.PENDIENTE);
        user.setFirstName((String) userInfo.get("given_name"));
        user.setLastName((String) userInfo.get("family_name"));
        user.setEmail((String) userInfo.get(CORREO));
        user.setImage((String) userInfo.get("picture"));
        user.setRegistrationDate(java.time.LocalDate.now());
        return user;
    }
}

