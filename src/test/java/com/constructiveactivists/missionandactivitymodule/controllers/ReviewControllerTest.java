package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ReviewRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.mappers.activity.ReviewMapper;
import com.constructiveactivists.missionandactivitymodule.services.activity.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReviewForActivity_Success() {
        Integer activityId = 1;
        ReviewRequest reviewRequest = new ReviewRequest();

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setId(activityId);
        activityEntity.setTitle("Plantación de Árboles");

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setDescription("Gran actividad!");
        reviewEntity.setRating(5);
        reviewEntity.setActivity(activityEntity);

        when(reviewMapper.toDomain(reviewRequest)).thenReturn(reviewEntity);
        when(reviewService.createReviewForActivity(activityId, reviewEntity)).thenReturn(reviewEntity);

        ResponseEntity<String> response = reviewController.createReviewForActivity(activityId, reviewRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("¡Reseña Creada con Éxito!"));
        assertTrue(response.getBody().contains("Plantación de Árboles"));
        verify(reviewService, times(1)).createReviewForActivity(activityId, reviewEntity);
    }

    @Test
    void testCreateReviewForActivity_EntityNotFound() {
        Integer activityId = 1;
        ReviewRequest reviewRequest = new ReviewRequest();

        when(reviewMapper.toDomain(reviewRequest)).thenReturn(new ReviewEntity());
        doThrow(new EntityNotFoundException("Actividad no encontrada")).when(reviewService).createReviewForActivity(anyInt(), any(ReviewEntity.class));

        ResponseEntity<String> response = reviewController.createReviewForActivity(activityId, reviewRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Actividad no encontrada"));
        verify(reviewService, times(1)).createReviewForActivity(anyInt(), any(ReviewEntity.class));
    }

    @Test
    void testCreateReviewForActivity_IllegalStateException() {
        Integer activityId = 1;
        ReviewRequest reviewRequest = new ReviewRequest();

        when(reviewMapper.toDomain(reviewRequest)).thenReturn(new ReviewEntity());
        doThrow(new IllegalStateException("Esta actividad ya tiene una reseña")).when(reviewService).createReviewForActivity(anyInt(), any(ReviewEntity.class));

        ResponseEntity<String> response = reviewController.createReviewForActivity(activityId, reviewRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Esta actividad ya tiene una reseña"));
        verify(reviewService, times(1)).createReviewForActivity(anyInt(), any(ReviewEntity.class));
    }
}
