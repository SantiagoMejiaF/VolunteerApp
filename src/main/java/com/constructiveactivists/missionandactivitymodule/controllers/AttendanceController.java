package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.services.activity.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.attendace}")
public class AttendanceController {

    private final AttendanceService attendanceService;


    // Endpoint para registrar check-in
    @PostMapping("/{activityId}/checkin")
    public ResponseEntity<String> checkIn(@PathVariable Integer activityId, HttpServletRequest request) {
        try {
            String response = attendanceService.handleCheckIn(activityId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para registrar check-out
    @PostMapping("/{activityId}/checkout")
    public ResponseEntity<String> checkOut(@PathVariable Integer activityId, HttpServletRequest request) {
        try {
            String response = attendanceService.handleCheckOut(activityId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
