package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.usermanagementmodule.entities.user.UserEntity;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
