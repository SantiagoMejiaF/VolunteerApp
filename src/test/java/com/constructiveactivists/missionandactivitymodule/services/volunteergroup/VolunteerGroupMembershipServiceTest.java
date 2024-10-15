package com.constructiveactivists.missionandactivitymodule.services.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupMembershipEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupMembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class VolunteerGroupMembershipServiceTest {

    @Mock
    private VolunteerGroupMembershipRepository volunteerGroupMembershipRepository;

    @InjectMocks
    private VolunteerGroupMembershipService volunteerGroupMembershipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsVolunteerInGroup_ReturnsTrue() {
        Integer groupId = 1;
        Integer volunteerId = 100;

        when(volunteerGroupMembershipRepository.existsByGroupIdAndVolunteerId(groupId, volunteerId)).thenReturn(true);

        boolean result = volunteerGroupMembershipService.isVolunteerInGroup(groupId, volunteerId);

        assertTrue(result);
        verify(volunteerGroupMembershipRepository, times(1)).existsByGroupIdAndVolunteerId(groupId, volunteerId);
    }

    @Test
    void testIsVolunteerInGroup_ReturnsFalse() {
        Integer groupId = 1;
        Integer volunteerId = 100;

        when(volunteerGroupMembershipRepository.existsByGroupIdAndVolunteerId(groupId, volunteerId)).thenReturn(false);

        boolean result = volunteerGroupMembershipService.isVolunteerInGroup(groupId, volunteerId);

        assertFalse(result);
        verify(volunteerGroupMembershipRepository, times(1)).existsByGroupIdAndVolunteerId(groupId, volunteerId);
    }

    @Test
    void testAddVolunteerToGroup() {
        Integer groupId = 1;
        Integer volunteerId = 100;

        volunteerGroupMembershipService.addVolunteerToGroup(groupId, volunteerId);

        verify(volunteerGroupMembershipRepository, times(1)).save(any(VolunteerGroupMembershipEntity.class));
    }
}
