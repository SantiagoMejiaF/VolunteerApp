package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity.ReviewAPI;
import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ReviewRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.mappers.activity.ReviewMapper;
import com.constructiveactivists.missionandactivitymodule.services.activity.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.review}")
public class ReviewController implements ReviewAPI {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Override
    public ResponseEntity<ReviewEntity> createReviewForActivity(
            @RequestParam("activityId") Integer activityId,
            @Valid @RequestBody ReviewRequest reviewRequest) {

        try {
            ReviewEntity createdReview = reviewService.createReviewForActivity(activityId, reviewMapper.toDomain(reviewRequest));
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
