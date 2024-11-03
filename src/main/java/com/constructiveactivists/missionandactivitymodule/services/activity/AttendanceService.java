package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.AttendanceException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.AttendanceEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.AttendanceRepository;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupMembershipService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.constructiveactivists.configurationmodule.constants.AppConstants.*;

@Service
@AllArgsConstructor
public class AttendanceService {

    private final UserService userService;
    private final VolunteerService volunteerService;
    private final VolunteerGroupMembershipService volunteerGroupMembershipService;
    private final AttendanceRepository attendanceRepository;
    private final ActivityService activityService;
    private final VolunteerOrganizationService volunteerOrganizationService;
    private final PostulationService postulationService;

    private final VolunteerRepository voluneerRepository;

    private static final long CHECK_IN_WINDOW_BEFORE = 30;
    private static final long CHECK_IN_WINDOW_AFTER = 60;
    private static final long CHECK_OUT_WINDOW_BEFORE = 30;
    private static final long CHECK_OUT_WINDOW_AFTER = 60;
    private final MissionService missionService;

    public void handleCheckIn(String email, Integer activityId) {

        validateCheckIn(email, activityId);
        registerCheckIn(email, activityId);
    }

    public void handleCheckOut(String email, Integer activityId) {
        validateCheckOut(email, activityId);
        registerCheckOut(email, activityId);
    }

    void validateCheckIn(String email, Integer activityId) {
        verifyUserAuthorization(email, activityId);
        checkInTimeValidity(activityId);
    }

    void validateCheckOut(String email, Integer activityId) {
        verifyUserAuthorization(email, activityId);
        checkOutTimeValidity(activityId);
    }

    void registerCheckIn(String email, Integer activityId) {
        UserEntity userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new AttendanceException(USER_NOT_FOUND));

        VolunteerEntity volunteerEntity = volunteerService.getVolunteerByUserId(userEntity.getId())
                .orElseThrow(() -> new AttendanceException(VOLUNTEER_NOT_FOUND));

        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));

        MissionEntity missionEntity = missionService.getMissionById(activityEntity.getMissionId())
                .orElseThrow(() -> new AttendanceException(MISSION_NOT_FOUND));

        VolunteerOrganizationEntity volunteerOrganizationEntity = volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerEntity.getId(), missionEntity.getOrganizationId());

        PostulationEntity postulationEntity = postulationService.findById(volunteerOrganizationEntity.getId());

        attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId())
                .ifPresent(attendance -> {
                    throw new AttendanceException("Check-in esta registrado");
                });

        postulationService.addTimeToPostulation(postulationEntity.getVolunteerOrganizationId(), activityEntity.getRequiredHours());
        ZoneId colombiaZoneId = ZoneId.of(ZONE_PLACE);
        ZonedDateTime nowInColombia = ZonedDateTime.now(colombiaZoneId);
        LocalTime checkInTime = nowInColombia.toLocalTime();

        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setActivity(activityEntity);
        attendance.setVolunteerId(volunteerEntity.getId());
        attendance.setCheckInTime(checkInTime);
        attendanceRepository.save(attendance);

        volunteerService.addVolunteerHours(volunteerEntity.getId(), activityEntity.getRequiredHours());
        volunteerService.addVolunteerActivity(volunteerEntity.getId(), activityEntity.getId());
        volunteerService.addCompletedActivity(volunteerEntity.getId(), activityId);
    }

    void registerCheckOut(String email, Integer activityId) {
        UserEntity userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new AttendanceException("User not found"));

        VolunteerEntity volunteerEntity = volunteerService.getVolunteerByUserId(userEntity.getId())
                .orElseThrow(() -> new AttendanceException("Volunteer not found"));

        AttendanceEntity attendance = attendanceRepository.findByActivityIdAndVolunteerId(activityId, volunteerEntity.getId())
                .orElseThrow(() -> new AttendanceException("Attendance record not found"));

        if (attendance.getCheckInTime() == null) {
            throw new AttendanceException("Check-in not registered, cannot perform check-out");
        }

        if (attendance.getCheckOutTime() != null) {
            throw new AttendanceException("Check-out esta registrado");
        }

        checkOutTimeValidity(activityId);

        ZoneId colombiaZoneId = ZoneId.of(ZONE_PLACE);
        ZonedDateTime nowInColombia = ZonedDateTime.now(colombiaZoneId);
        LocalTime checkOutTime = nowInColombia.toLocalTime();

        attendance.setCheckOutTime(checkOutTime);

        attendanceRepository.save(attendance);
    }

    void verifyUserAuthorization(String email, Integer activityId) {
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
        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));
        return volunteerGroupMembershipService.isVolunteerInGroup(activityEntity.getVolunteerGroup(), volunteerId);
    }

    void checkInTimeValidity(Integer activityId) {
        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));

        ZoneId colombiaZoneId = ZoneId.of(ZONE_PLACE);
        LocalTime activityStartTime = activityEntity.getStartTime();
        ZonedDateTime currentTimeInColombia = ZonedDateTime.now(colombiaZoneId);

        LocalTime checkInStart = activityStartTime.minusMinutes(CHECK_IN_WINDOW_BEFORE);
        LocalTime checkInEnd = activityStartTime.plusMinutes(CHECK_IN_WINDOW_AFTER);

        if (currentTimeInColombia.toLocalTime().isBefore(checkInStart) || currentTimeInColombia.toLocalTime().isAfter(checkInEnd)) {
            throw new AttendanceException("Check-in time not within allowed range.");
        }
    }
    public void validateCheckInTime(Integer activityId) {
        checkInTimeValidity(activityId);
    }

    void checkOutTimeValidity(Integer activityId) {
        ActivityEntity activityEntity = activityService.getById(activityId)
                .orElseThrow(() -> new AttendanceException(ACTIVITY_NOT_FOUND));

        LocalTime endTime = activityEntity.getEndTime();
        ZoneId colombiaZoneId = ZoneId.of(ZONE_PLACE);
        ZonedDateTime currentTimeInColombia = ZonedDateTime.now(colombiaZoneId);
        LocalTime checkOutStart = endTime.minusMinutes(CHECK_OUT_WINDOW_BEFORE);
        LocalTime checkOutEnd = endTime.plusMinutes(CHECK_OUT_WINDOW_AFTER);
        if (currentTimeInColombia.toLocalTime().isBefore(checkOutStart) || currentTimeInColombia.toLocalTime().isAfter(checkOutEnd)) {
            throw new AttendanceException("Check-out time not within allowed range.");
        }
    }
}
