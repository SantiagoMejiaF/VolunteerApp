package com.constructiveactivists.organizationmodule.controllers;

import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.ActivityCoordinatorRequest;
import com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator.CoordinatorAvailabilityRequest;
import com.constructiveactivists.organizationmodule.entities.activitycoordinator.ActivityCoordinatorEntity;
import com.constructiveactivists.organizationmodule.models.CoordinatorAvailabilityModel;
import com.constructiveactivists.organizationmodule.mappers.activitycoordinator.ActivityCoordinatorMapper;
import com.constructiveactivists.organizationmodule.mappers.activitycoordinator.CoordinatorAvailabilityMapper;
import com.constructiveactivists.organizationmodule.services.activitycoordinator.ActivityCoordinatorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityCoordinatorControllerTest {

    @Mock
    private ActivityCoordinatorService activityCoordinatorService;

    @Mock
    private ActivityCoordinatorMapper activityCoordinatorMapper;

    @Mock
    private CoordinatorAvailabilityMapper coordinatorAvailabilityMapper;

    @InjectMocks
    private ActivityCoordinatorController activityCoordinatorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllActivityCoordinators() {
        List<ActivityCoordinatorEntity> coordinators = List.of(new ActivityCoordinatorEntity(), new ActivityCoordinatorEntity());

        when(activityCoordinatorService.getAll()).thenReturn(coordinators);

        ResponseEntity<List<ActivityCoordinatorEntity>> response = activityCoordinatorController.getAllActivityCoordinators();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(activityCoordinatorService, times(1)).getAll();
    }

    @Test
    void testCreateActivityCoordinator_Success() {
        ActivityCoordinatorRequest request = new ActivityCoordinatorRequest(456, 123, "0987654321", "3109876543");
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(1);

        when(activityCoordinatorMapper.toDomain(request)).thenReturn(coordinator);
        when(activityCoordinatorService.save(coordinator, request.getUserId())).thenReturn(coordinator);

        ResponseEntity<ActivityCoordinatorEntity> response = activityCoordinatorController.createActivityCoordinator(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        verify(activityCoordinatorService, times(1)).save(coordinator, request.getUserId());
    }

    @Test
    void testDeleteActivityCoordinator() {
        Integer coordinatorId = 1;

        doNothing().when(activityCoordinatorService).deleteById(coordinatorId);

        ResponseEntity<Void> response = activityCoordinatorController.deleteActivityCoordinator(coordinatorId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(activityCoordinatorService, times(1)).deleteById(coordinatorId);
    }

    @Test
    void testGetCoordinatorById_Found() {
        Integer coordinatorId = 1;
        ActivityCoordinatorEntity coordinator = new ActivityCoordinatorEntity();
        coordinator.setId(coordinatorId);

        when(activityCoordinatorService.getCoordinatorById(coordinatorId)).thenReturn(Optional.of(coordinator));

        ResponseEntity<ActivityCoordinatorEntity> response = activityCoordinatorController.getCoordinatorById(coordinatorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(coordinatorId, response.getBody().getId());
        verify(activityCoordinatorService, times(1)).getCoordinatorById(coordinatorId);
    }

    @Test
    void testGetCoordinatorById_NotFound() {
        Integer coordinatorId = 1;

        when(activityCoordinatorService.getCoordinatorById(coordinatorId)).thenReturn(Optional.empty());

        ResponseEntity<ActivityCoordinatorEntity> response = activityCoordinatorController.getCoordinatorById(coordinatorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(activityCoordinatorService, times(1)).getCoordinatorById(coordinatorId);
    }

    @Test
    void testFindAvailableCoordinators_Success() {
        Integer organizationId = 1;
        LocalDate date = LocalDate.now();
        String startTime = "08:00";
        String endTime = "12:00";

        CoordinatorAvailabilityRequest request = new CoordinatorAvailabilityRequest();
        request.setOrganizationId(organizationId);
        request.setDate(date);
        request.setStartTime(startTime);
        request.setEndTime(endTime);

        CoordinatorAvailabilityModel availabilityModel = new CoordinatorAvailabilityModel();
        availabilityModel.setOrganizationId(organizationId);
        availabilityModel.setDate(date);
        availabilityModel.setStartTime(LocalTime.parse(startTime));
        availabilityModel.setEndTime(LocalTime.parse(endTime));

        ActivityCoordinatorEntity coordinator1 = new ActivityCoordinatorEntity();
        ActivityCoordinatorEntity coordinator2 = new ActivityCoordinatorEntity();

        when(coordinatorAvailabilityMapper.toDomain(any(CoordinatorAvailabilityRequest.class)))
                .thenReturn(availabilityModel);
        when(activityCoordinatorService.findAvailableCoordinators(any(CoordinatorAvailabilityModel.class)))
                .thenReturn(List.of(coordinator1, coordinator2));

        ResponseEntity<List<ActivityCoordinatorEntity>> response = activityCoordinatorController.findAvailableCoordinators(
                organizationId, date, startTime, endTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(activityCoordinatorService, times(1)).findAvailableCoordinators(any(CoordinatorAvailabilityModel.class));
    }

    @Test
    void testGetCoordinatorsByOrganization_Success() {
        Integer organizationId = 1;

        ActivityCoordinatorEntity coordinator1 = new ActivityCoordinatorEntity();
        ActivityCoordinatorEntity coordinator2 = new ActivityCoordinatorEntity();

        when(activityCoordinatorService.findByOrganizationId(organizationId))
                .thenReturn(List.of(coordinator1, coordinator2));

        ResponseEntity<List<ActivityCoordinatorEntity>> response = activityCoordinatorController.getCoordinatorsByOrganization(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(activityCoordinatorService, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    void testGetCoordinatorsByOrganization_NotFound() {
        Integer organizationId = 1;

        when(activityCoordinatorService.findByOrganizationId(organizationId)).thenReturn(List.of());

        ResponseEntity<List<ActivityCoordinatorEntity>> response = activityCoordinatorController.getCoordinatorsByOrganization(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(activityCoordinatorService, times(1)).findByOrganizationId(organizationId);
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
        when(activityCoordinatorService.getActivityCoordinatorByUserId(userId)).thenReturn(coordinator);
        ResponseEntity<ActivityCoordinatorEntity> response = activityCoordinatorController.getActivityCoordinatorByUserId(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coordinator, response.getBody());
        verify(activityCoordinatorService, times(1)).getActivityCoordinatorByUserId(userId);
    }
    @Test
     void testGetActivityCoordinatorByUserId_NotFound() {
        Integer userId = 2;
        when(activityCoordinatorService.getActivityCoordinatorByUserId(userId)).thenThrow(new EntityNotFoundException());
        ResponseEntity<ActivityCoordinatorEntity> response = activityCoordinatorController.getActivityCoordinatorByUserId(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull( response.getBody());
        verify(activityCoordinatorService, times(1)).getActivityCoordinatorByUserId(userId);
    }
}
