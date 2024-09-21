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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.review}")
public class ReviewController implements ReviewAPI {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Override
    public ResponseEntity<ReviewEntity> createReviewForActivity(
            @RequestParam("activityId") Integer activityId,
            @RequestBody @Valid ReviewRequest reviewRequest){

        try {
            ReviewEntity createdReview = reviewService.createReviewForActivity(activityId, reviewMapper.toDomain(reviewRequest));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
