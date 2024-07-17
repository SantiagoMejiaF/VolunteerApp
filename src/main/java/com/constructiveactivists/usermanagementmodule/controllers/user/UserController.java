package com.constructiveactivists.usermanagementmodule.controllers.user;

import com.constructiveactivists.usermanagementmodule.controllers.user.configuration.UserAPI;
import com.constructiveactivists.usermanagementmodule.controllers.user.mappers.UserMapper;
import com.constructiveactivists.usermanagementmodule.controllers.user.request.UserRequest;
import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
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
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public ResponseEntity<UserEntity> getUserById(Integer id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<UserEntity> createUser(@Valid UserRequest userRequest) {
        UserEntity createdUserEntity = userService.saveUser(userMapper.toDomain(userRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserEntity);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
