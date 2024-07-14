package com.constructiveactivists.usermanagementmodule.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.constructiveactivists.usermanagementmodule.entities.User;
import com.constructiveactivists.usermanagementmodule.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

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

        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("User1");
        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("User2");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }

    @Test
    void testGetUserById() {

        User user = new User();
        user.setId(1);
        user.setFirstName("User1");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testSaveUser() {

        User user = new User();
        user.setId(1);
        user.setFirstName("User1");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals(user, result);
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
