package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.constants.AppConstants.ZONE_PLACE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddLike_Success() {
        ReviewEntity review = new ReviewEntity();
        review.setId(1);
        review.setLikes(5);

        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

        ReviewEntity updatedReview = reviewService.addLike(1);

        assertNotNull(updatedReview);
        assertEquals(6, updatedReview.getLikes());
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testAddLike_ReviewNotFound() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.addLike(1);
        });

        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void testRemoveLike_Success() {
        ReviewEntity review = new ReviewEntity();
        review.setId(1);
        review.setLikes(5);

        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

        ReviewEntity updatedReview = reviewService.removeLike(1);

        assertNotNull(updatedReview);
        assertEquals(4, updatedReview.getLikes());
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testRemoveLike_ReviewNotFound() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.removeLike(1);
        });

        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void testRemoveLike_NoNegativeLikes() {
        ReviewEntity review = new ReviewEntity();
        review.setId(1);
        review.setLikes(0);

        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

        ReviewEntity updatedReview = reviewService.removeLike(1);

        assertNotNull(updatedReview);
        assertEquals(0, updatedReview.getLikes());
        verify(reviewRepository, times(1)).findById(1);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testCreateReviewForActivity_Success() {
        Integer activityId = 1;
        ReviewEntity reviewRequest = new ReviewEntity();
        reviewRequest.setDescription("Great activity");
        reviewRequest.setRating(5);

        ActivityEntity activity = new ActivityEntity();
        activity.setId(activityId);

        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(invocation -> {
            ReviewEntity savedReview = invocation.getArgument(0);
            savedReview.setId(1);
            return savedReview;
        });

        ReviewEntity createdReview = reviewService.createReviewForActivity(activityId, reviewRequest);

        assertNotNull(createdReview);
        assertEquals("Great activity", createdReview.getDescription());
        assertEquals(5, createdReview.getRating());
        assertEquals(0, createdReview.getLikes());
        assertEquals(activity, createdReview.getActivity());

        ZoneId colombiaZoneId = ZoneId.of(ZONE_PLACE);
        ZonedDateTime nowInColombia = ZonedDateTime.now(colombiaZoneId);
        LocalDate expectedDate = nowInColombia.toLocalDate();
        assertEquals(expectedDate, createdReview.getCreationDate());

        verify(activityRepository, times(1)).findById(activityId);
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
    }

    @Test
    void testCreateReviewForActivity_ActivityNotFound() {
        Integer activityId = 999;
        ReviewEntity reviewRequest = new ReviewEntity();

        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.createReviewForActivity(activityId, reviewRequest);
        });

        verify(activityRepository, times(1)).findById(activityId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void testCreateReviewForActivity_AlreadyHasReview() {
        Integer activityId = 1;
        ReviewEntity existingReview = new ReviewEntity();
        ActivityEntity activity = new ActivityEntity();
        activity.setId(activityId);
        activity.setReview(existingReview);

        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));

        assertThrows(IllegalStateException.class, () -> {
            reviewService.createReviewForActivity(activityId, new ReviewEntity());
        });

        verify(activityRepository, times(1)).findById(activityId);
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }
}
