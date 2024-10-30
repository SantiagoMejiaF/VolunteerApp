package com.constructiveactivists.missionandactivitymodule.services.activity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.PersonalDataCommunityLeaderEntity;
import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ActivityStatusServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ReviewEmailService reviewEmailService;

    @InjectMocks
    private ActivityStatusService activityStatusService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateActivityStatus_ActivitiesInProgress() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        ActivityEntity activity1 = new ActivityEntity();
        activity1.setDate(today);
        activity1.setStartTime(now.minusMinutes(10).toLocalTime());
        activity1.setEndTime(now.plusMinutes(10).toLocalTime());
        activity1.setActivityStatus(ActivityStatusEnum.DISPONIBLE);
        PersonalDataCommunityLeaderEntity leader1 = new PersonalDataCommunityLeaderEntity();
        leader1.setEmailCommunityLeader("leader1@example.com");
        activity1.setPersonalDataCommunityLeaderEntity(leader1);
        ActivityEntity activity2 = new ActivityEntity();
        activity2.setDate(today);
        activity2.setStartTime(now.minusMinutes(20).toLocalTime());
        activity2.setEndTime(now.minusMinutes(10).toLocalTime());
        activity2.setActivityStatus(ActivityStatusEnum.DISPONIBLE);
        PersonalDataCommunityLeaderEntity leader2 = new PersonalDataCommunityLeaderEntity();
        leader2.setEmailCommunityLeader("leader2@example.com");
        activity2.setPersonalDataCommunityLeaderEntity(leader2);
        List<ActivityEntity> activities = Arrays.asList(activity1, activity2);
        when(activityRepository.findByActivityStatusNot(ActivityStatusEnum.COMPLETADA)).thenReturn(activities);
        activityStatusService.updateActivityStatus();
        assertEquals(ActivityStatusEnum.EN_CURSO, activity1.getActivityStatus());
        assertEquals(ActivityStatusEnum.COMPLETADA, activity2.getActivityStatus());
        verify(activityRepository).saveAll(activities);
    }

    @Test
    void testUpdateActivityStatus_NoActivitiesToUpdate() {
        when(activityRepository.findByActivityStatusNot(ActivityStatusEnum.COMPLETADA)).thenReturn(Arrays.asList());
        activityStatusService.updateActivityStatus();
        verify(activityRepository, never()).saveAll(any());
    }
}
