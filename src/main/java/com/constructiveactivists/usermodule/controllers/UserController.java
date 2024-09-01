package com.constructiveactivists.usermodule.controllers;

import com.constructiveactivists.usermodule.controllers.configuration.UserAPI;
import com.constructiveactivists.usermodule.controllers.request.UserRequest;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.mapper.UserUpdateMapper;
import com.constructiveactivists.usermodule.services.ApprovalService;
import com.constructiveactivists.usermodule.services.UserService;
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
    private final UserUpdateMapper userUpdateMapper;

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
    public ResponseEntity<Void> updateUser(@PathVariable Integer userId, @RequestBody UserRequest user) {
        userService.updateUser(userId, userUpdateMapper.toDomain(user));
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

    @Override
    public ResponseEntity<List<UserEntity>> getUsersByAuthorizationStatus(@PathVariable AuthorizationStatus authorizationStatus) {
        List<UserEntity> users = userService.findUsersByAuthorizationStatus(authorizationStatus);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<UserEntity>> getPendingVolunteersAndOrganizations() {
        List<UserEntity> users = userService.findPendingVolunteersAndOrganizations();
        return ResponseEntity.ok(users);
    }
}
