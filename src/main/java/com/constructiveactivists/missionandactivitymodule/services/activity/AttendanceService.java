package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.externalservicesmodule.services.GoogleService;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.AttendanceEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.AttendanceRepository;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupMembershipService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AttendanceService {

    private final GoogleService googleService;
    private final UserService userService;
    private final VolunteerService volunteerService;
    private final VolunteerGroupMembershipService volunteerGroupMembershipService;
    private final AttendanceRepository attendanceRepository;
    private final ActivityService activityService;

    private static final long CHECK_IN_WINDOW_BEFORE = 30;
    private static final long CHECK_IN_WINDOW_AFTER = 60;
    private static final long CHECK_OUT_WINDOW_BEFORE = 30;
    private static final long CHECK_OUT_WINDOW_AFTER = 60;

    public void handleCheckIn(String email, Integer activityId) {

        validateCheckIn(email, activityId);
        registerCheckIn(email, activityId);
    }

    public void handleCheckOut(String email, Integer activityId) {
        validateCheckOut(email, activityId);
        registerCheckOut(email, activityId);
    }

    private void validateCheckIn(String email, Integer activityId) {
        verifyUserAuthorization(email, activityId);
        checkInTimeValidity(activityId, LocalTime.now());
    }

    private void validateCheckOut(String email, Integer activityId) {
        verifyUserAuthorization(email, activityId);
        checkOutTimeValidity(activityId, LocalTime.now());
    }

    private void registerCheckIn(String email, Integer activityId) {
        UserEntity userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new AttendanceException("User not found"));

        VolunteerEntity volunteerEntity = volunteerService.getVolunteerByUserId(userEntity.getId())
                .orElseThrow(() -> new AttendanceException("Volunteer not found"));

        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException("Activity not found"));

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setActivity(activityEntity);
        attendance.setVolunteerId(volunteerEntity.getId());
        attendance.setCheckInTime(LocalTime.now());

        attendanceRepository.save(attendance);
    }

    private void registerCheckOut(String email, Integer activityId) {
        UserEntity userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new AttendanceException("User not found"));

        VolunteerEntity volunteerEntity = volunteerService.getVolunteerByUserId(userEntity.getId())
                .orElseThrow(() -> new AttendanceException("Volunteer not found"));

        AttendanceEntity attendance = attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId())
                .orElseThrow(() -> new AttendanceException("Attendance record not found"));

        checkOutTimeValidity(activityId, LocalTime.now());
        attendance.setCheckOutTime(LocalTime.now());

        attendanceRepository.save(attendance);
    }

    private void verifyUserAuthorization(String email, Integer activityId) {
        boolean isMember = isVolunteerInGroup(email, activityId);
        if (!isMember) {
            throw new AttendanceException("User not authorized");
        }
    }

    private boolean isVolunteerInGroup(String email, Integer activityId) {
        Optional<UserEntity> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        Integer userId = userOpt.get().getId();
        Optional<VolunteerEntity> volunteerOpt = volunteerService.getVolunteerByUserId(userId);
        if (volunteerOpt.isEmpty()) {
            return false;
        }
        Integer volunteerId = volunteerOpt.get().getId();
        return volunteerGroupMembershipService.isVolunteerInGroup(activityId, volunteerId);
    }

    private void checkInTimeValidity(Integer activityId, LocalTime currentTime) {
        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException("Activity not found"));

        LocalTime startTime = activityEntity.getStartTime();
        LocalTime checkInStart = startTime.minusMinutes(CHECK_IN_WINDOW_BEFORE);
        LocalTime checkInEnd = startTime.plusMinutes(CHECK_IN_WINDOW_AFTER);

        System.out.println("Check-in start: " + checkInStart);
        System.out.println("Check-in end: " + checkInEnd);
        System.out.println("Current time: " + currentTime);
        System.out.println("Activity start time: " + startTime);

        if (currentTime.isBefore(checkInStart) || currentTime.isAfter(checkInEnd)) {
            throw new AttendanceException("Check-in time not within allowed range.");
        }
    }

    private void checkOutTimeValidity(Integer activityId, LocalTime currentTime) {
        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException("Activity not found"));
        LocalTime endTime = activityEntity.getEndTime();

        LocalTime checkOutStart = endTime.minusMinutes(CHECK_OUT_WINDOW_BEFORE);
        LocalTime checkOutEnd = endTime.plusMinutes(CHECK_OUT_WINDOW_AFTER);

        if (currentTime.isBefore(checkOutStart) || currentTime.isAfter(checkOutEnd)) {
            throw new AttendanceException("Check-out time not within allowed range.");
        }
    }
}

