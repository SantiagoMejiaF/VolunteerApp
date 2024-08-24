package com.constructiveactivists.resportsanddashboardsmodule.services;

import com.constructiveactivists.organizationmanagementmodule.services.OrganizationService;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DashboardOrganizationService {

    private final OrganizationService organizationService;

    private final UserService userService;

    public long getActiveOrganizationCount() {
        return organizationService.getAllOrganizations().stream()
                .filter(organization -> {
                    Optional<UserEntity> user = userService.getUserById(organization.getUserId());
                    return user.isPresent() && user.get().getAuthorizationType() == AuthorizationStatus.AUTORIZADO;
                })
                .count();
    }
}
