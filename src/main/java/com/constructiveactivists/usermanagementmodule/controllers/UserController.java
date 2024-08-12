package com.constructiveactivists.usermanagementmodule.controllers;

import com.constructiveactivists.usermanagementmodule.controllers.configuration.UserAPI;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.usermanagementmodule.services.ApprovalService;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.user}")
public class UserController implements UserAPI {

    private final UserService userService;
    private final ApprovalService approvalService;

    @Override
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseEntity<UserEntity> getUserById(Integer id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<Void> updateUserRoleType(Integer userId, RoleType roleType) {
        userService.updateUserRoleType(userId, roleType);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateAuthorizationStatus(Integer userId, AuthorizationStatus authorizationStatus) {
        userService.updateAuthorizationStatus(userId, authorizationStatus);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> sendApprovalOrRejectionEmail(@RequestParam Integer userId, @RequestParam boolean approved) {
        approvalService.sendApprovalResponse(userId, approved);
        String message = approved ? "Usuario aprobado correctamente." : "Usuario rechazado correctamente.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> getTotalUserCount() {
        long count = userService.getTotalUserCount();
        return ResponseEntity.ok(count);
    }
}
