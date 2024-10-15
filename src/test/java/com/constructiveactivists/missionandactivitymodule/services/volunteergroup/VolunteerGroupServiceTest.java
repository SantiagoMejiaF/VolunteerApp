package com.constructiveactivists.missionandactivitymodule.services.volunteergroup;

import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.VolunteerGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VolunteerGroupServiceTest {

    @Mock
    private VolunteerGroupRepository volunteerGroupRepository;

    @InjectMocks
    private VolunteerGroupService volunteerGroupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetVolunteerGroupById_Found() {
        Integer groupId = 1;
        VolunteerGroupEntity group = new VolunteerGroupEntity();
        group.setId(groupId);

        when(volunteerGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Optional<VolunteerGroupEntity> result = volunteerGroupService.getVolunteerGroupById(groupId);

        assertTrue(result.isPresent());
        assertEquals(groupId, result.get().getId());
        verify(volunteerGroupRepository, times(1)).findById(groupId);
    }

    @Test
    void testGetVolunteerGroupById_NotFound() {
        Integer groupId = 1;

        when(volunteerGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        Optional<VolunteerGroupEntity> result = volunteerGroupService.getVolunteerGroupById(groupId);

        assertFalse(result.isPresent());
        verify(volunteerGroupRepository, times(1)).findById(groupId);
    }

    @Test
    void testGetAllVolunteerGroups() {
        List<VolunteerGroupEntity> groups = List.of(new VolunteerGroupEntity(), new VolunteerGroupEntity());

        when(volunteerGroupRepository.findAll()).thenReturn(groups);

        List<VolunteerGroupEntity> result = volunteerGroupService.getAllVolunteerGroups();

        assertEquals(2, result.size());
        verify(volunteerGroupRepository, times(1)).findAll();
    }

    @Test
    void testSaveVolunteerGroup() {
        VolunteerGroupEntity group = new VolunteerGroupEntity();
        group.setId(1);

        when(volunteerGroupRepository.save(group)).thenReturn(group);

        VolunteerGroupEntity savedGroup = volunteerGroupService.save(group);

        assertEquals(1, savedGroup.getId());
        verify(volunteerGroupRepository, times(1)).save(group);
    }

    @Test
    void testDeleteVolunteerGroupById() {
        Integer groupId = 1;

        doNothing().when(volunteerGroupRepository).deleteById(groupId);

        volunteerGroupService.deleteVolunteerGroupById(groupId);

        verify(volunteerGroupRepository, times(1)).deleteById(groupId);
    }

    @Test
    void testGetVolunteerGroupByOrganizationId() {
        Integer organizationId = 1;
        List<VolunteerGroupEntity> groups = List.of(new VolunteerGroupEntity(), new VolunteerGroupEntity());

        when(volunteerGroupRepository.findByOrganizationId(organizationId)).thenReturn(groups);

        List<VolunteerGroupEntity> result = volunteerGroupService.getVolunteerGroupByOrganizationId(organizationId);

        assertEquals(2, result.size());
        verify(volunteerGroupRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    void testGetVolunteerGroupByActivityId_Found() {
        Integer activityId = 1;
        VolunteerGroupEntity group = new VolunteerGroupEntity();
        group.setId(1);
        group.setActivity(activityId);

        when(volunteerGroupRepository.findByActivity(activityId)).thenReturn(Optional.of(group));

        Optional<VolunteerGroupEntity> result = volunteerGroupService.getVolunteerGroupByActivityId(activityId);

        assertTrue(result.isPresent());
        assertEquals(activityId, result.get().getActivity());
        verify(volunteerGroupRepository, times(1)).findByActivity(activityId);
    }

    @Test
    void testGetVolunteerGroupByActivityId_NotFound() {
        Integer activityId = 1;

        when(volunteerGroupRepository.findByActivity(activityId)).thenReturn(Optional.empty());

        Optional<VolunteerGroupEntity> result = volunteerGroupService.getVolunteerGroupByActivityId(activityId);

        assertFalse(result.isPresent());
        verify(volunteerGroupRepository, times(1)).findByActivity(activityId);
    }
}
