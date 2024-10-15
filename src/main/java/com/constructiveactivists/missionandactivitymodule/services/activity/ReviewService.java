package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.ZONE_PLACE;

@Service
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ActivityRepository activityRepository;

    public ReviewEntity addLike(Integer reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setLikes(review.getLikes() + 1);
        return reviewRepository.save(review);
    }

    public ReviewEntity removeLike(Integer reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (review.getLikes() > 0) {
            review.setLikes(review.getLikes() - 1);
        }
        return reviewRepository.save(review);
    }


    public ReviewEntity createReviewForActivity(Integer activityId, ReviewEntity reviewRequest) {
        ActivityEntity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));

        if (activity.getReview() != null) {
            throw new IllegalStateException("This activity already has a review");
        }

        ReviewEntity review = new ReviewEntity();
        review.setActivity(activity);
        review.setDescription(reviewRequest.getDescription());
        review.setImageUrls(reviewRequest.getImageUrls());
        review.setRating(reviewRequest.getRating());
        ZoneId colombiaZoneId = ZoneId.of(ZONE_PLACE);
        ZonedDateTime nowInColombia = ZonedDateTime.now(colombiaZoneId);
        LocalDate currentDateInColombia = nowInColombia.toLocalDate();
        review.setCreationDate(currentDateInColombia);
        review.setLikes(0);
        return reviewRepository.save(review);
    }
}
