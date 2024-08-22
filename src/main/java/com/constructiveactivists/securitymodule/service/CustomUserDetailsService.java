package com.constructiveactivists.securitymodule.service;

import com.constructiveactivists.securitymodule.model.CustomUserDetails;
import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.services.RoleService;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public CustomUserDetailsService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + username));

        List<RoleEntity> roles = roleService.getRoleById(userEntity.getRole().getId())
                .map(List::of)
                .orElseThrow(() -> new UsernameNotFoundException("Rol no encontrado con el ID: " + userEntity.getRole().getId()));
        return new CustomUserDetails(userEntity, roles);
    }
}
