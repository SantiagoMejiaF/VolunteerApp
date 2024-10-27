package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymodule.repositories.ReviewRepository;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.VOLUNTEER_NOT_FOUND;
import static com.constructiveactivists.configurationmodule.constants.AppConstants.ZONE_PLACE;

@Service
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ActivityRepository activityRepository;

    private VolunteerRepository volunteeringnRepository;

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
        if (reviewRepository.findByActivity(activity).isPresent()) {
            throw new IllegalStateException("Activity already has a review");
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

    public List<ReviewEntity> getReviewsByVolunteerId(Integer volunteerId) {
        VolunteerEntity volunteer = volunteeringnRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException(VOLUNTEER_NOT_FOUND + volunteerId));
        VolunteeringInformationEntity volunteeringInfo = volunteer.getVolunteeringInformation();
        if (volunteeringInfo == null || volunteeringInfo.getActivitiesCompleted().isEmpty()) {
            return List.of();
        }
        Set<Integer> uniqueActivityIds = new HashSet<>(volunteeringInfo.getActivitiesCompleted());
        List<ReviewEntity> reviews = new ArrayList<>();
        for (Integer activityId : uniqueActivityIds) {
            ActivityEntity activity = activityRepository.findById(activityId)
                    .orElse(null);
            if (activity != null) {
                reviewRepository.findByActivity(activity).ifPresent(reviews::add);
            }
        }
        return reviews;
    }
}
