package com.constructiveactivists.missionandactivitymodule.services.activity;

import static org.mockito.Mockito.*;
import java.util.List;

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

    @InjectMocks
    private ActivityStatusService activityStatusService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateActivityStatus_NoActivitiesToUpdate() {
        when(activityRepository.findByActivityStatusNot(ActivityStatusEnum.COMPLETADA)).thenReturn(List.of());
        activityStatusService.updateActivityStatus();
        verify(activityRepository, never()).saveAll(any());
    }
}
