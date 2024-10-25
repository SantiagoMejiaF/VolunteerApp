package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.AttendanceEntity;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.AttendanceRepository;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupMembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private ActivityService activityService;

    @Mock
    private MissionService missionService;

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @Mock
    private PostulationService postulationService;

    @Mock
    private VolunteerGroupMembershipService volunteerGroupMembershipService;

    @InjectMocks
    private AttendanceService attendanceService;

    private UserEntity user;
    private VolunteerEntity volunteer;
    private ActivityEntity activity;
    private AttendanceEntity attendance;
    private VolunteerOrganizationEntity volunteerOrg;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserEntity();
        user.setId(1);
        user.setEmail("test@example.com");

        volunteer = new VolunteerEntity();
        volunteer.setId(1);
        volunteer.setUserId(1);

        activity = new ActivityEntity();
        activity.setId(1);
        activity.setStartTime(LocalTime.of(10, 0));
        activity.setEndTime(LocalTime.of(12, 0));

        attendance = new AttendanceEntity();
        attendance.setActivity(activity);
        attendance.setVolunteerId(volunteer.getId());

        volunteerOrg = new VolunteerOrganizationEntity();
        volunteerOrg.setId(1);
        volunteerOrg.setOrganizationId(1);
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
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(volunteerService.getVolunteerByUserId(user.getId())).thenReturn(Optional.empty());

        AttendanceException thrown = assertThrows(AttendanceException.class, () -> {
            attendanceService.handleCheckIn(user.getEmail(), 1);
        });

        assertEquals("El usuario no está autorizado para esta actividad.", thrown.getMessage());
    }

    @Test
    void testHandleCheckOut_AttendanceNotFound() {
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(volunteerService.getVolunteerByUserId(user.getId())).thenReturn(Optional.of(volunteer));
        when(attendanceRepository.findByActivityIdAndVolunteerId(activity.getId(), volunteer.getId()))
                .thenReturn(Optional.empty());

        AttendanceException thrown = assertThrows(AttendanceException.class, () -> {
            attendanceService.handleCheckOut(user.getEmail(), 1);
        });

        assertEquals("El usuario no está autorizado para esta actividad.", thrown.getMessage());
    }

    @Test
    void testHandleCheckOut_CheckInNotRegistered() {
        attendance.setCheckInTime(null);
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(volunteerService.getVolunteerByUserId(user.getId())).thenReturn(Optional.of(volunteer));
        when(attendanceRepository.findByActivityIdAndVolunteerId(activity.getId(), volunteer.getId()))
                .thenReturn(Optional.of(attendance));

        AttendanceException thrown = assertThrows(AttendanceException.class, () -> {
            attendanceService.handleCheckOut(user.getEmail(), 1);
        });

        assertEquals("El usuario no está autorizado para esta actividad.", thrown.getMessage());
    }
}