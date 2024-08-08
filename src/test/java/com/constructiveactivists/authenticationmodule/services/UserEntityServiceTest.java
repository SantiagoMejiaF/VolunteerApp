package com.constructiveactivists.authenticationmodule.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(1);
        userEntity1.setFirstName("User1");
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(2);
        userEntity2.setFirstName("User2");

        List<UserEntity> userEntities = Arrays.asList(userEntity1, userEntity2);

        when(userRepository.findAll()).thenReturn(userEntities);

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(userEntity1, result.get(0));
        assertEquals(userEntity2, result.get(1));
    }

    @Test
    void testGetUserById() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setFirstName("User1");

        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(userEntity, result.get());
    }

    @Test
    void testSaveUser() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setFirstName("User1");

        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity result = userService.saveUser(userEntity);

        assertEquals(userEntity, result);
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
