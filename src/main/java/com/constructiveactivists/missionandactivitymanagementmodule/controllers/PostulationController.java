package com.constructiveactivists.missionandactivitymanagementmodule.controllers;

import com.constructiveactivists.missionandactivitymanagementmodule.controllers.configuration.PostulationAPI;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation.PostulationEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.services.PostulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.postulation}")
public class PostulationController implements PostulationAPI {

    private final PostulationService postulationService;

    @Override
    public ResponseEntity<List<PostulationEntity>> getAllPostulations() {
        return ResponseEntity.ok(postulationService.getAllPostulations());
    }

    @Override
    public ResponseEntity<PostulationEntity> getPostulationById(@PathVariable Integer id) {
        Optional<PostulationEntity> postulation = postulationService.getPostulationById(id);
        return postulation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

//    @Override
//    public ResponseEntity<PostulationEntity> createPostulation(@RequestBody PostulationEntity postulation) {
//        PostulationEntity createdPostulation = postulationService.createPostulation(postulation);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostulation);
//    }

    @Override
    public ResponseEntity<Void> deletePostulation(@PathVariable Integer id) {
        postulationService.deletePostulation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<PostulationEntity> approvePostulation(@PathVariable Integer id) {
        PostulationEntity approvedPostulation = postulationService.approvePostulation(id);
        return ResponseEntity.ok(approvedPostulation);
    }
}
