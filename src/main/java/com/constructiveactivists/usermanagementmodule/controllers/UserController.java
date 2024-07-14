package com.constructiveactivists.usermanagementmodule.controllers;

import com.constructiveactivists.usermanagementmodule.controllers.mappers.UserMapper;
import com.constructiveactivists.usermanagementmodule.controllers.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.User;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.user}")
public class UserController implements UserAPI {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseEntity<User> getUserById(Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<User> createUser(@Valid UserRequest userRequest) {
        User createdUser = userService.saveUser(userMapper.toDomain(userRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
