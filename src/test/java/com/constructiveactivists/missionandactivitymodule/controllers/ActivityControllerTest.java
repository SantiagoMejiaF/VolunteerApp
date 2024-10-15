package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ActivityRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.activity.AttendanceEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.mappers.activity.ActivityMapper;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ActivityControllerTest {

    @Mock
    private ActivityService activityService;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityController activityController;

    private ActivityEntity mockActivity;
    private ActivityRequest mockActivityRequest;
    private ReviewEntity mockReview;
    private AttendanceEntity mockAttendance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockReview = new ReviewEntity();
        mockReview.setId(1);
        mockReview.setDescription("Great activity, well organized.");
        mockReview.setCreationDate(LocalDate.of(2024, 9, 3));
        mockReview.setLikes(50);
        mockReview.setRating(5);
        mockReview.setImageUrls(List.of("image1.jpg", "image2.jpg"));

        mockAttendance = new AttendanceEntity();
        mockAttendance.setId(1);
        mockAttendance.setVolunteerId(101);
        mockAttendance.setCheckInTime(LocalTime.of(8, 0));
        mockAttendance.setCheckOutTime(LocalTime.of(12, 0));

        mockActivity = new ActivityEntity();
        mockActivity.setId(1);
        mockActivity.setTitle("Plantación de árboles");
        mockActivity.setDate(LocalDate.of(2024, 9, 3));
        mockActivity.setCity("Bogotá");
        mockActivity.setLocality("Usaquén");
        mockActivity.setAddress("Parque Central, Calle 123");
        mockActivity.setNumberOfVolunteersRequired(20);
        mockActivity.setRequiredHours(4);
        mockActivity.setVisibility(VisibilityEnum.PUBLICA);
        mockActivity.setNumberOfBeneficiaries(100);
        mockActivity.setActivityStatus(ActivityStatusEnum.EN_CURSO);
        mockActivity.setReview(mockReview);
        mockActivity.setAttendances(List.of(mockAttendance));

        mockActivityRequest = new ActivityRequest();
        mockActivityRequest.setTitle("Plantación de árboles");
        mockActivityRequest.setDate(LocalDate.of(2024, 9, 3));
        mockActivityRequest.setCity("Bogotá");
        mockActivityRequest.setLocality("Usaquén");
        mockActivityRequest.setAddress("Parque Central, Calle 123");
        mockActivityRequest.setNumberOfVolunteersRequired(20);
        mockActivityRequest.setRequiredHours(4);
        mockActivityRequest.setVisibility(VisibilityEnum.PUBLICA);
        mockActivityRequest.setNumberOfBeneficiaries(100);
    }

    @Test
    void testGetAllActivities() {
        when(activityService.getAll()).thenReturn(List.of(mockActivity));

        ResponseEntity<List<ActivityEntity>> response = activityController.getAllActivities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Plantación de árboles", response.getBody().get(0).getTitle());

        verify(activityService, times(1)).getAll();
    }

    @Test
    void testGetActivityById_Found() {
        when(activityService.getById(anyInt())).thenReturn(Optional.of(mockActivity));

        ResponseEntity<ActivityEntity> response = activityController.getActivityById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Plantación de árboles", response.getBody().getTitle());

        verify(activityService, times(1)).getById(1);
    }

    @Test
    void testGetActivityById_NotFound() {
        when(activityService.getById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<ActivityEntity> response = activityController.getActivityById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(activityService, times(1)).getById(1);
    }

    @Test
    void testCreateActivity() {
        when(activityMapper.toDomain(any(ActivityRequest.class))).thenReturn(mockActivity);
        when(activityService.save(any(ActivityEntity.class))).thenReturn(mockActivity);

        ResponseEntity<ActivityEntity> response = activityController.createActivity(mockActivityRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Plantación de árboles", response.getBody().getTitle());

        verify(activityMapper, times(1)).toDomain(any(ActivityRequest.class));
        verify(activityService, times(1)).save(any(ActivityEntity.class));
    }

    @Test
    void testGetCheckInQrCode() {
        byte[] mockQrCode = new byte[]{1, 2, 3};
        when(activityService.getCheckInQrCode(anyInt())).thenReturn(mockQrCode);

        ResponseEntity<byte[]> response = activityController.getCheckInQrCode(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(mockQrCode, response.getBody());
        assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());

        verify(activityService, times(1)).getCheckInQrCode(1);
    }

    @Test
    void testDeleteActivity_Success() {
        doNothing().when(activityService).deleteActivityById(anyInt());

        ResponseEntity<String> response = activityController.deleteActivity(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(activityService, times(1)).deleteActivityById(1);
    }

    @Test
    void testDeleteActivity_NotFound() {
        doThrow(new EntityNotFoundException()).when(activityService).deleteActivityById(anyInt());

        ResponseEntity<String> response = activityController.deleteActivity(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Activity with ID 1 not found.", response.getBody());

        verify(activityService, times(1)).deleteActivityById(1);
    }

    @Test
    void testDeleteActivity_ServerError() {
        doThrow(new RuntimeException()).when(activityService).deleteActivityById(anyInt());

        ResponseEntity<String> response = activityController.deleteActivity(1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An error occurred while trying to delete the activity.", response.getBody());

        verify(activityService, times(1)).deleteActivityById(1);
    }
}
