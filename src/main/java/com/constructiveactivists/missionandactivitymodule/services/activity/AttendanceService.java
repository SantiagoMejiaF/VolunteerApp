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
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.*;

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

    public String handleCheckIn(Integer activityId, HttpServletRequest request) throws IOException {
        String authToken = extractAuthToken(request);
        validateAuthToken(authToken);
        String email = extractEmailFromToken(authToken);
        verifyUserAuthorization(email, activityId);
        checkInTimeValidity(activityId, LocalTime.now());
        registerAttendance(email, activityId, true);
        return CHECK_IN_SUCCESS;
    }

    public String handleCheckOut(Integer activityId, HttpServletRequest request) throws IOException {
        String authToken = extractAuthToken(request);
        validateAuthToken(authToken);
        String email = extractEmailFromToken(authToken);
        verifyUserAuthorization(email, activityId);
        checkOutTimeValidity(activityId, LocalTime.now());
        registerAttendance(email, activityId, false);
        return CHECK_OUT_SUCCESS;
    }

    private String extractAuthToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        throw new AttendanceException(GOOGLE_TOKEN_NOT_PROVIDED);
    }

    private void validateAuthToken(String authToken) {
        if (authToken == null) {
            throw new AttendanceException(GOOGLE_TOKEN_NOT_PROVIDED);
        }
    }

    private String extractEmailFromToken(String authToken) throws IOException {
        Map<String, Object> userInfo = googleService.fetchUserInfo(authToken);
        if (userInfo == null || userInfo.get("email") == null) {
            throw new AttendanceException(INVALID_GOOGLE_TOKEN);
        }
        return (String) userInfo.get("email");
    }

    private void verifyUserAuthorization(String email, Integer activityId) {
        boolean isMember = isVolunteerInGroup(email, activityId);
        if (!isMember) {
            throw new AttendanceException(USER_NOT_AUTHORIZED);
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
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));

        LocalTime startTime = activityEntity.getStartTime();

        LocalTime checkInStart = startTime.minusMinutes(CHECK_IN_WINDOW_BEFORE);
        LocalTime checkInEnd = startTime.plusMinutes(CHECK_IN_WINDOW_AFTER);

        if (currentTime.isBefore(checkInStart) || currentTime.isAfter(checkInEnd)) {
            throw new AttendanceException("El tiempo de check-in no está dentro del rango permitido.");
        }
    }

    private void checkOutTimeValidity(Integer activityId, LocalTime currentTime) {
        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));
        LocalTime endTime = activityEntity.getEndTime();

        LocalTime checkOutStart = endTime.minusMinutes(CHECK_OUT_WINDOW_BEFORE);
        LocalTime checkOutEnd = endTime.plusMinutes(CHECK_OUT_WINDOW_AFTER);

        if (currentTime.isBefore(checkOutStart) || currentTime.isAfter(checkOutEnd)) {
            throw new AttendanceException("El tiempo de check-out no está dentro del rango permitido.");
        }
    }

    private void registerAttendance(String email, Integer activityId, boolean isCheckIn) {
        UserEntity userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new AttendanceException(USER_NOT_FOUND));

        VolunteerEntity volunteerEntity = volunteerService.getVolunteerByUserId(userEntity.getId())
                .orElseThrow(() -> new AttendanceException(VOLUNTEER_NOT_FOUND));

        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));

        AttendanceEntity attendance = isCheckIn ?
                new AttendanceEntity() :
                attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId())
                        .orElseThrow(() -> new AttendanceException(ATTENDANCE_RECORD_NOT_FOUND));

        if (isCheckIn) {
            attendance.setActivity(activityEntity);
            attendance.setVolunteerId(volunteerEntity.getId());
            attendance.setCheckInTime(LocalTime.now());
        } else {
            attendance.setCheckOutTime(LocalTime.now());
        }

        attendanceRepository.save(attendance);
    }
}

