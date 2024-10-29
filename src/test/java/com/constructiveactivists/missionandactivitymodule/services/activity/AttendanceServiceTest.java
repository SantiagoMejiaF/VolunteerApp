package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.AttendanceEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupMembershipService;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.AttendanceRepository;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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
    private VolunteerGroupMembershipService volunteerGroupMembershipService;

    @Mock
    private PostulationService postulationService;

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @InjectMocks
    private AttendanceService attendanceService;

    private UserEntity user;
    private VolunteerEntity volunteer;
    private ActivityEntity activity;
    private AttendanceEntity attendance;

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

        VolunteerOrganizationEntity volunteerOrg = new VolunteerOrganizationEntity();
        volunteerOrg.setId(1);
        volunteerOrg.setOrganizationId(1);
    }

    @Test
    void testHandleCheckIn_Success() {
        String email = "test@example.com";
        Integer activityId = 1;

        LocalTime startTime = LocalTime.now().minusMinutes(15);
        activity.setStartTime(startTime);
        activity.setRequiredHours(2);

        MissionEntity missionEntity = new MissionEntity();
        missionEntity.setId(1);
        missionEntity.setOrganizationId(1);

        VolunteerOrganizationEntity volunteerOrganizationEntity = new VolunteerOrganizationEntity();
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationEntity.setOrganizationId(1);

        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(volunteerOrganizationEntity.getId());

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(volunteerService.getVolunteerByUserId(user.getId())).thenReturn(Optional.of(volunteer));
        when(activityService.getById(activityId)).thenReturn(Optional.of(activity));
        when(missionService.getMissionById(activity.getMissionId())).thenReturn(Optional.of(missionEntity));
        when(attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteer.getId())).thenReturn(Optional.empty());
        when(volunteerGroupMembershipService.isVolunteerInGroup(activityId, volunteer.getId())).thenReturn(true);
        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteer.getId(), missionEntity.getOrganizationId()))
                .thenReturn(volunteerOrganizationEntity);
        when(postulationService.findById(volunteerOrganizationEntity.getId()))
                .thenReturn(postulationEntity);

        attendanceService.handleCheckIn(email, activityId);

        verify(attendanceRepository, times(1)).save(any(AttendanceEntity.class));
        verify(volunteerService, times(1)).addVolunteerHours(volunteer.getId(), activity.getRequiredHours());
        verify(volunteerService, times(1)).addVolunteerActivity(volunteer.getId(), activityId);
    }

    @Test
    void testRegisterCheckIn_Success() {
        String email = "test@example.com";
        Integer activityId = 1;

        user.setId(1);
        volunteer.setId(1);
        activity.setId(activityId);
        activity.setRequiredHours(2);
        MissionEntity missionEntity = new MissionEntity();
        missionEntity.setOrganizationId(1);
        VolunteerOrganizationEntity volunteerOrganizationEntity = new VolunteerOrganizationEntity();
        volunteerOrganizationEntity.setId(1);
        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(volunteerOrganizationEntity.getId());

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(volunteerService.getVolunteerByUserId(user.getId())).thenReturn(Optional.of(volunteer));
        when(activityService.getById(activityId)).thenReturn(Optional.of(activity));
        when(missionService.getMissionById(activity.getMissionId())).thenReturn(Optional.of(missionEntity));
        when(attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteer.getId())).thenReturn(Optional.empty());
        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteer.getId(), missionEntity.getOrganizationId()))
                .thenReturn(volunteerOrganizationEntity);
        when(postulationService.findById(volunteerOrganizationEntity.getId())).thenReturn(postulationEntity);

        attendanceService.registerCheckIn(email, activityId);

        verify(attendanceRepository, times(1)).save(any(AttendanceEntity.class));
        verify(volunteerService, times(1)).addVolunteerHours(volunteer.getId(), activity.getRequiredHours());
        verify(volunteerService, times(1)).addVolunteerActivity(volunteer.getId(), activityId);
    }

    @Test
    void testRegisterCheckIn_AlreadyCheckedIn() {
        String email = "test@example.com";
        Integer activityId = 1;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        VolunteerEntity volunteerEntity = new VolunteerEntity();
        volunteerEntity.setId(1);

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(activityId);

        MissionEntity missionEntity = new MissionEntity();
        missionEntity.setOrganizationId(100);

        VolunteerOrganizationEntity volunteerOrganizationEntity = new VolunteerOrganizationEntity();
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationEntity.setOrganizationId(100);

        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setVolunteerId(volunteerEntity.getId());
        attendanceEntity.setActivity(activityEntity);

        when(userService.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(volunteerService.getVolunteerByUserId(userEntity.getId())).thenReturn(Optional.of(volunteerEntity));
        when(activityService.getById(activityId)).thenReturn(Optional.of(activityEntity));
        when(missionService.getMissionById(activityEntity.getMissionId())).thenReturn(Optional.of(missionEntity));
        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerEntity.getId(), missionEntity.getOrganizationId()))
                .thenReturn(volunteerOrganizationEntity);
        when(attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId()))
                .thenReturn(Optional.of(attendanceEntity));

        AttendanceException exception = assertThrows(AttendanceException.class, () -> {
            attendanceService.registerCheckIn(email, activityId);
        });
        assertEquals("Check-in esta registrado", exception.getMessage());
    }

    @Test
    void testRegisterCheckOut_Success() {
        String email = "test@example.com";
        Integer activityId = 1;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        VolunteerEntity volunteerEntity = new VolunteerEntity();
        volunteerEntity.setId(1);

        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setVolunteerId(volunteerEntity.getId());
        attendanceEntity.setCheckInTime(LocalTime.now().minusHours(1));

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(activityId);
        activityEntity.setEndTime(LocalTime.now().plusMinutes(30));

        when(userService.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(volunteerService.getVolunteerByUserId(userEntity.getId())).thenReturn(Optional.of(volunteerEntity));
        when(attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId())).thenReturn(Optional.of(attendanceEntity));
        when(activityService.getById(activityId)).thenReturn(Optional.of(activityEntity));

        attendanceService.registerCheckOut(email, activityId);

        assertNotNull(attendanceEntity.getCheckOutTime());
        verify(attendanceRepository, times(1)).save(attendanceEntity);
    }

    @Test
    void testRegisterCheckOut_NoCheckInRecorded() {
        String email = "test@example.com";
        Integer activityId = 1;
        UserEntity userEntity = new UserEntity();
        VolunteerEntity volunteerEntity = new VolunteerEntity();
        AttendanceEntity attendanceEntity = new AttendanceEntity();

        when(userService.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(volunteerService.getVolunteerByUserId(userEntity.getId())).thenReturn(Optional.of(volunteerEntity));
        when(attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId())).thenReturn(Optional.of(attendanceEntity));

        AttendanceException exception = assertThrows(AttendanceException.class, () -> attendanceService.registerCheckOut(email, activityId));
        assertEquals("Check-in not registered, cannot perform check-out", exception.getMessage());
    }

    @Test
    void testCheckInTimeValidity_Success() {
        Integer activityId = 1;
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setStartTime(LocalTime.now().plusMinutes(15));

        when(activityService.getById(activityId)).thenReturn(Optional.of(activityEntity));

        assertDoesNotThrow(() -> attendanceService.checkInTimeValidity(activityId));
    }

    @Test
    void testCheckInTimeValidity_OutOfRange() {
        Integer activityId = 1;
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setStartTime(LocalTime.now().plusMinutes(120));

        when(activityService.getById(activityId)).thenReturn(Optional.of(activityEntity));

        AttendanceException exception = assertThrows(AttendanceException.class, () -> attendanceService.checkInTimeValidity(activityId));
        assertEquals("Check-in time not within allowed range.", exception.getMessage());
    }

    @Test
    void testCheckOutTimeValidity_Success() {
        Integer activityId = 1;
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setEndTime(LocalTime.now().minusMinutes(15)); // Valid check-out window

        when(activityService.getById(activityId)).thenReturn(Optional.of(activityEntity));

        assertDoesNotThrow(() -> attendanceService.checkOutTimeValidity(activityId));
    }

    @Test
    void testCheckOutTimeValidity_OutOfRange() {
        Integer activityId = 1;
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setEndTime(LocalTime.now().plusMinutes(120));

        when(activityService.getById(activityId)).thenReturn(Optional.of(activityEntity));

        AttendanceException exception = assertThrows(AttendanceException.class, () -> attendanceService.checkOutTimeValidity(activityId));
        assertEquals("Check-out time not within allowed range.", exception.getMessage());
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