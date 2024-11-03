package com.constructiveactivists.organizationmodule.services.activitycoordinator;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorUpdateRequest;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.models.CoordinatorAvailabilityModel;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.ActivityCoordinatorRepository;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.COORDINATOR_MESSAGE_ID;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActivityCoordinatorServiceTest {

    @Mock
    private ActivityCoordinatorRepository activityCoordinatorRepository;

    @Mock
    private UserService userService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityCoordinatorService activityCoordinatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_Success() {
        Integer userId = 1;
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setOrganizationId(2);

        UserEntity user = new UserEntity();
        user.setId(userId);

        RoleEntity role = new RoleEntity();
        user.setRole(role);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(organizationService.getOrganizationById(coordinator.getOrganizationId())).thenReturn(Optional.of(new OrganizationEntity()));
        when(activityCoordinatorRepository.save(any(ActivityCoordinatorEntity.class))).thenReturn(coordinator);

        ActivityCoordinatorEntity savedCoordinator = activityCoordinatorService.save(coordinator, userId);

        assertNotNull(savedCoordinator);
        verify(userService, times(1)).saveUser(user);
        assertEquals(RoleType.COORDINADOR_ACTIVIDAD, user.getRole().getRoleType());
        assertEquals(AuthorizationStatus.AUTORIZADO, user.getAuthorizationType());
    }

    @Test
    void testSave_OrganizationNotFound() {
        Integer userId = 1;
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setOrganizationId(2);

        UserEntity user = new UserEntity();
        user.setId(userId);
        RoleEntity role = new RoleEntity();
        user.setRole(role);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(organizationService.getOrganizationById(coordinator.getOrganizationId())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            activityCoordinatorService.save(coordinator, userId);
        });

        assertEquals("La organización con ID 2 no existe.", exception.getMessage());
        verify(activityCoordinatorRepository, never()).save(any());
    }

    @Test
    void testFindAvailableCoordinators_Success() {
        CoordinatorAvailabilityModel availabilityModel = new CoordinatorAvailabilityModel();
        availabilityModel.setOrganizationId(2);
        availabilityModel.setDate(LocalDate.now());
        availabilityModel.setStartTime(LocalTime.of(9, 0));
        availabilityModel.setEndTime(LocalTime.of(12, 0));

        OrganizationEntity organization = new OrganizationEntity();
        when(organizationService.getOrganizationById(availabilityModel.getOrganizationId())).thenReturn(Optional.of(organization));

        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(1);
        when(activityCoordinatorRepository.findByOrganizationId(availabilityModel.getOrganizationId())).thenReturn(List.of(coordinator));

        ActivityEntity activity = new ActivityEntity();
        activity.setStartTime(LocalTime.of(8, 0));
        activity.setEndTime(LocalTime.of(10, 0));
        activity.setDate(LocalDate.now());
        when(activityService.findAllByActivityCoordinator(coordinator.getId())).thenReturn(List.of(activity));

        List<ActivityCoordinatorEntity> availableCoordinators = activityCoordinatorService.findAvailableCoordinators(availabilityModel);

        assertEquals(0, availableCoordinators.size(), "Debería haber 1 coordinador disponible");
        verify(activityCoordinatorRepository, times(1)).findByOrganizationId(availabilityModel.getOrganizationId());
        verify(activityService, times(1)).findAllByActivityCoordinator(coordinator.getId());
    }

    @Test
    void testFindAvailableCoordinators_OrganizationNotFound() {
        CoordinatorAvailabilityModel availabilityModel = new CoordinatorAvailabilityModel();
        availabilityModel.setOrganizationId(2);

        when(organizationService.getOrganizationById(availabilityModel.getOrganizationId())).thenReturn(Optional.empty());

        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> {
            activityCoordinatorService.findAvailableCoordinators(availabilityModel);
        });

        assertEquals("La organización con id 2 no existe.", exception.getMessage());
        verify(activityCoordinatorRepository, never()).findByOrganizationId(any());
    }

    @Test
    void testDeleteById_Success() {
        doNothing().when(activityCoordinatorRepository).deleteById(1);

        activityCoordinatorService.deleteById(1);

        verify(activityCoordinatorRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetById_Found() {
        Integer coordinatorId = 1;
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(coordinatorId);

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinator));

        Optional<ActivityCoordinatorEntity> result = activityCoordinatorService.getById(coordinatorId);

        assertTrue(result.isPresent());
        assertEquals(coordinatorId, result.get().getId());
        verify(activityCoordinatorRepository, times(1)).findById(coordinatorId);
    }

    @Test
    void testGetById_NotFound() {
        Integer coordinatorId = 1;

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.empty());

        Optional<ActivityCoordinatorEntity> result = activityCoordinatorService.getById(coordinatorId);

        assertFalse(result.isPresent());
        verify(activityCoordinatorRepository, times(1)).findById(coordinatorId);
    }

    @Test
    void testGetAll() {
        List<ActivityCoordinatorEntity> coordinators = List.of(new ActivityCoordinatorEntity(), new ActivityCoordinatorEntity());

        when(activityCoordinatorRepository.findAll()).thenReturn(coordinators);

        List<ActivityCoordinatorEntity> result = activityCoordinatorService.getAll();

        assertEquals(2, result.size());
        verify(activityCoordinatorRepository, times(1)).findAll();
    }

    @Test
    void testIsTimeOverlap_True() {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);
        ActivityEntity activity = new ActivityEntity();
        activity.setStartTime(LocalTime.of(11, 0));
        activity.setEndTime(LocalTime.of(13, 0));

        boolean result = activityCoordinatorService.isTimeOverlap(activity, startTime, endTime);

        assertTrue(result);
    }

    @Test
    void testIsTimeOverlap_False() {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        ActivityEntity activity = new ActivityEntity();
        activity.setStartTime(LocalTime.of(11, 0));
        activity.setEndTime(LocalTime.of(13, 0));

        boolean result = activityCoordinatorService.isTimeOverlap(activity, startTime, endTime);

        assertFalse(result);
    }

    @Test
    void testFindByOrganizationId() {
        Integer organizationId = 1;
        List<ActivityCoordinatorEntity> coordinators = List.of(new ActivityCoordinatorEntity(), new ActivityCoordinatorEntity());

        when(activityCoordinatorRepository.findByOrganizationId(organizationId)).thenReturn(coordinators);

        List<ActivityCoordinatorEntity> result = activityCoordinatorService.findByOrganizationId(organizationId);

        assertEquals(2, result.size());
        verify(activityCoordinatorRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    void testGetCoordinatorById_Found() {
        Integer coordinatorId = 1;
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(coordinatorId);

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinator));

        Optional<ActivityCoordinatorEntity> result = activityCoordinatorService.getCoordinatorById(coordinatorId);

        assertTrue(result.isPresent());
        assertEquals(coordinatorId, result.get().getId());
        verify(activityCoordinatorRepository, times(1)).findById(coordinatorId);
    }

    @Test
    void testGetCoordinatorById_NotFound() {
        Integer coordinatorId = 1;

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.empty());

        Optional<ActivityCoordinatorEntity> result = activityCoordinatorService.getCoordinatorById(coordinatorId);

        assertFalse(result.isPresent());
        verify(activityCoordinatorRepository, times(1)).findById(coordinatorId);
    }

    @Test
     void testGetActivityCoordinatorByUserId_Found() {
        Integer userId = 1;
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(1);
        coordinator.setUserId(userId);
        coordinator.setOrganizationId(10);
        coordinator.setIdentificationCard("123456789");
        coordinator.setPhoneActivityCoordinator("123456789");
        coordinator.setCompletedActivities(List.of(1, 2, 3));

        when(activityCoordinatorRepository.findByUserId(userId)).thenReturn(Optional.of(coordinator));
        ActivityCoordinatorEntity result = activityCoordinatorService.getActivityCoordinatorByUserId(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(coordinator.getOrganizationId(), result.getOrganizationId());
        assertEquals(coordinator.getIdentificationCard(), result.getIdentificationCard());
        assertEquals(coordinator.getPhoneActivityCoordinator(), result.getPhoneActivityCoordinator());
        assertEquals(coordinator.getCompletedActivities(), result.getCompletedActivities());
        verify(activityCoordinatorRepository, times(1)).findByUserId(userId);
    }

    @Test
     void testGetActivityCoordinatorByUserId_NotFound() {
        Integer userId = 2;
        when(activityCoordinatorRepository.findByUserId(userId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> activityCoordinatorService.getActivityCoordinatorByUserId(userId));

        assertEquals(COORDINATOR_MESSAGE_ID + userId + NOT_FOUND_MESSAGE, exception.getMessage());
        verify(activityCoordinatorRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testUpdateCoordinatorInfo_Success() {
        Integer coordinatorId = 1;
        Integer userId = 2;

        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(coordinatorId);
        coordinator.setUserId(userId);
        coordinator.setPhoneActivityCoordinator("3001234567");

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFirstName("Juan");
        user.setLastName("Pérez");

        ActivityCoordinatorUpdateRequest request = new ActivityCoordinatorUpdateRequest();
        request.setFirstName("Carlos");
        request.setLastName("Gómez");
        request.setPhone("3009876543");

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinator));
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ActivityCoordinatorEntity result = activityCoordinatorService.updateCoordinatorInfo(coordinatorId, request);

        assertEquals("Carlos", user.getFirstName());
        assertEquals("Gómez", user.getLastName());
        assertEquals("3009876543", coordinator.getPhoneActivityCoordinator());

        verify(userService, times(1)).saveUser(user);
        verify(activityCoordinatorRepository, times(1)).save(coordinator);
    }

    @Test
    void testUpdateCoordinatorInfo_CoordinatorNotFound() {
        Integer coordinatorId = 1;
        ActivityCoordinatorUpdateRequest request = new ActivityCoordinatorUpdateRequest();

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                activityCoordinatorService.updateCoordinatorInfo(coordinatorId, request));

        assertEquals("Coordinador de actividad con ID 1 no encontrado.", exception.getMessage());
        verify(activityCoordinatorRepository, never()).save(any());
    }

    @Test
    void testUpdateCoordinatorInfo_UserNotFound() {
        Integer coordinatorId = 1;
        Integer userId = 2;

        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(coordinatorId);
        coordinator.setUserId(userId);

        ActivityCoordinatorUpdateRequest request = new ActivityCoordinatorUpdateRequest();

        when(activityCoordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinator));
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                activityCoordinatorService.updateCoordinatorInfo(coordinatorId, request));

        assertEquals("Usuario con ID 2 no encontrado.", exception.getMessage());
        verify(activityCoordinatorRepository, never()).save(any());
    }
}
