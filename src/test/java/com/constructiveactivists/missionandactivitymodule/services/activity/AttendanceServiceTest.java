package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private VolunteerService volunteerService;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleCheckIn_UserNotFound() {
        String email = "test@example.com";
        Integer activityId = 1;

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        AttendanceException thrown = assertThrows(AttendanceException.class, () -> {
            attendanceService.handleCheckIn(email, activityId);
        });

        assertEquals("El usuario no está autorizado para esta actividad.", thrown.getMessage());
        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    void testHandleCheckIn_VolunteerNotFound() {
        String email = "test@example.com";
        Integer activityId = 1;

        UserEntity user = new UserEntity();
        user.setId(1);

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(volunteerService.getVolunteerByUserId(user.getId())).thenReturn(Optional.empty());

        AttendanceException thrown = assertThrows(AttendanceException.class, () -> {
            attendanceService.handleCheckIn(email, activityId);
        });

        assertEquals("El usuario no está autorizado para esta actividad.", thrown.getMessage());
        verify(userService, times(1)).findByEmail(email);
    }
}
