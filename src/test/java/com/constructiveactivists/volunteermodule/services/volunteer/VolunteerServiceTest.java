package com.constructiveactivists.volunteermodule.services.volunteer;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.MissionEntity;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.volunteergroup.VolunteerGroupEntity;
import com.constructiveactivists.missionandactivitymodule.services.activity.ActivityService;
import com.constructiveactivists.missionandactivitymodule.services.mission.MissionService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupMembershipService;
import com.constructiveactivists.missionandactivitymodule.services.volunteergroup.VolunteerGroupService;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteeringInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.*;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private UserService userService;

    @Mock
    private VolunteerGroupService volunteerGroupService;

    @Mock
    private VolunteerGroupMembershipService volunteerGroupMembershipService;

    @Mock
    private ActivityService activityService;

    @Mock
    private MissionService missionService;

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @InjectMocks
    private VolunteerService volunteerService;

    private VolunteerEntity volunteerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        PersonalInformationEntity personalInformation = new PersonalInformationEntity();
        personalInformation.setBornDate(LocalDate.of(1990, 5, 15));

        VolunteeringInformationEntity volunteeringInformation = new VolunteeringInformationEntity();
        volunteeringInformation.setVolunteeredTotalHours(10);

        volunteerEntity = new VolunteerEntity();
        volunteerEntity.setId(1);
        volunteerEntity.setUserId(1);
        volunteerEntity.setPersonalInformation(personalInformation);
        volunteerEntity.setVolunteeringInformation(volunteeringInformation);
    }

    @Test
    void testGetVolunteerById() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));

        Optional<VolunteerEntity> result = volunteerService.getVolunteerById(1);

        assertTrue(result.isPresent());
        assertEquals(34, result.get().getPersonalInformation().getAge()); // Edad calculada
        verify(volunteerRepository, times(1)).findById(1);
    }

    @Test
    void testSaveVolunteer() {
        UserEntity user = new UserEntity();
        user.setId(1);

        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.SIN_ASIGNAR);

        user.setRole(role);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(volunteerRepository.save(any(VolunteerEntity.class))).thenReturn(volunteerEntity);

        VolunteerEntity result = volunteerService.saveVolunteer(volunteerEntity);

        assertNotNull(result);
        assertEquals(volunteerEntity.getUserId(), result.getUserId());
        verify(userService, times(1)).updateUserRoleType(1, RoleType.VOLUNTARIO);
        verify(volunteerRepository, times(1)).save(volunteerEntity);
    }

    @Test
    void testDeleteVolunteer() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));

        volunteerService.deleteVolunteer(1);

        verify(volunteerRepository, times(1)).delete(volunteerEntity);
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void testSignUpForActivity_Success() {
        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(10);
        volunteerGroup.setNumberOfVolunteersRequired(10);
        volunteerGroup.setCurrentVolunteers(5);

        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));
        when(activityService.getById(1)).thenReturn(Optional.of(new ActivityEntity()));

        volunteerService.signUpForActivity(1, 1);

        verify(volunteerGroupMembershipService, times(1)).addVolunteerToGroup(10, 1);  // Usando el ID de grupo correcto
        assertEquals(6, volunteerGroup.getCurrentVolunteers());
        verify(volunteerGroupService, times(1)).save(volunteerGroup);
    }

    @Test
    void testSignUpForActivity_ActivityFull() {
        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setNumberOfVolunteersRequired(5);
        volunteerGroup.setCurrentVolunteers(5);

        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerService.signUpForActivity(1, 1);
        });

        assertEquals("El grupo de voluntarios ya ha alcanzado la cantidad máxima de voluntarios requeridos.", exception.getMessage());
        verify(volunteerGroupMembershipService, never()).addVolunteerToGroup(anyInt(), anyInt());
    }

    @Test
    void testAddVolunteerHours() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));

        volunteerService.addVolunteerHours(1, 5);

        assertEquals(15, volunteerEntity.getVolunteeringInformation().getVolunteeredTotalHours());
        verify(volunteerRepository, times(1)).save(volunteerEntity);
    }

    @Test
    void testPromoteToLeader_Success() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));

        volunteerService.promoteToLeader(1);

        assertEquals(VolunteerType.LIDER, volunteerEntity.getVolunteeringInformation().getVolunteerType());
        verify(volunteerRepository, times(1)).save(volunteerEntity);
    }

    @Test
    void testPromoteToLeader_AlreadyLeader() {
        volunteerEntity.getVolunteeringInformation().setVolunteerType(VolunteerType.LIDER);
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));

        VolunteerEntity result = volunteerService.promoteToLeader(1);

        assertEquals(VolunteerType.LIDER, result.getVolunteeringInformation().getVolunteerType());
        verify(volunteerRepository, never()).save(volunteerEntity);
    }

    @Test
    void testGetAllVolunteers() {
        when(volunteerRepository.findAll()).thenReturn(List.of(volunteerEntity));

        List<VolunteerEntity> result = volunteerService.getAllVolunteers();

        assertEquals(1, result.size());
        assertEquals(volunteerEntity.getId(), result.get(0).getId());
        verify(volunteerRepository, times(1)).findAll();
    }

    @Test
    void testGetAllInterests() {
        List<InterestEnum> result = volunteerService.getAllInterests();

        assertEquals(Arrays.asList(InterestEnum.values()), result);
    }

    @Test
    void testGetAllSkills() {
        List<SkillEnum> result = volunteerService.getAllSkills();

        assertEquals(Arrays.asList(SkillEnum.values()), result);
    }

    @Test
    void testGetAllAvailabilities() {
        List<AvailabilityEnum> result = volunteerService.getAllAvailabilities();

        assertEquals(Arrays.asList(AvailabilityEnum.values()), result);
    }

    @Test
    void testGetAllRelationships() {
        List<RelationshipEnum> result = volunteerService.getAllRelationships();

        assertEquals(Arrays.asList(RelationshipEnum.values()), result);
    }

    @Test
    void testUpdateVolunteer_Success() {
        PersonalInformationEntity newPersonalInfo = new PersonalInformationEntity();
        newPersonalInfo.setPhoneNumber("123456789");
        newPersonalInfo.setAddress("New Address");
        newPersonalInfo.setBornDate(LocalDate.of(1995, 7, 10));

        VolunteeringInformationEntity newVolunteeringInfo = new VolunteeringInformationEntity();
        newVolunteeringInfo.setVolunteerType(VolunteerType.VOLUNTARIO);

        VolunteerEntity updatedEntity = new VolunteerEntity();
        updatedEntity.setPersonalInformation(newPersonalInfo);
        updatedEntity.setVolunteeringInformation(newVolunteeringInfo);

        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));
        when(volunteerRepository.save(any(VolunteerEntity.class))).thenReturn(volunteerEntity);

        VolunteerEntity result = volunteerService.updateVolunteer(1, updatedEntity);

        assertEquals(newPersonalInfo.getPhoneNumber(), result.getPersonalInformation().getPhoneNumber());
        verify(volunteerRepository, times(1)).save(volunteerEntity);
    }

    @Test
    void testUpdateVolunteer_NotFound() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.updateVolunteer(1, volunteerEntity);
        });

        assertEquals("El voluntario con ID 1 no existe en la base de datos.", exception.getMessage());
        verify(volunteerRepository, never()).save(any(VolunteerEntity.class));
    }

    @Test
    void testGetVolunteerByUserId() {
        when(volunteerRepository.findByUserId(1)).thenReturn(Optional.of(volunteerEntity));

        Optional<VolunteerEntity> result = volunteerService.getVolunteerByUserId(1);

        assertTrue(result.isPresent());
        assertEquals(volunteerEntity.getId(), result.get().getId());
        verify(volunteerRepository, times(1)).findByUserId(1);
    }

    @Test
    void testAddVolunteerActivity() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));

        volunteerService.addVolunteerActivity(1);

        assertEquals(1, volunteerEntity.getVolunteeringInformation().getActivitiesCompleted());
        verify(volunteerRepository, times(1)).save(volunteerEntity);
    }

    @Test
    void testSignUpForActivity_PrivateActivity_VolunteerNotInOrganization() {
        ActivityEntity activity = new ActivityEntity();
        activity.setVisibility(VisibilityEnum.PRIVADA);
        activity.setMissionId(100);

        MissionEntity mission = new MissionEntity();
        mission.setOrganizationId(200);

        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setCurrentVolunteers(5);
        volunteerGroup.setNumberOfVolunteersRequired(10);

        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));
        when(activityService.getById(1)).thenReturn(Optional.of(activity));
        when(missionService.getMissionById(100)).thenReturn(Optional.of(mission));
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(1)).thenReturn(List.of());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerService.signUpForActivity(1, 1);
        });

        assertEquals("El voluntario no pertenece a la organización requerida para inscribirse en esta actividad privada.", exception.getMessage());
    }

    @Test
    void testSignUpForActivity_PrivateActivity_VolunteerInOrganization() {
        ActivityEntity activity = new ActivityEntity();
        activity.setVisibility(VisibilityEnum.PRIVADA);
        activity.setMissionId(100);

        MissionEntity mission = new MissionEntity();
        mission.setOrganizationId(200);

        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(10);
        volunteerGroup.setCurrentVolunteers(5);
        volunteerGroup.setNumberOfVolunteersRequired(10);

        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setOrganizationId(200);

        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));
        when(activityService.getById(1)).thenReturn(Optional.of(activity));
        when(missionService.getMissionById(100)).thenReturn(Optional.of(mission));
        when(volunteerOrganizationService.getOrganizationsByVolunteerId(1)).thenReturn(List.of(volunteerOrganization));
        when(volunteerGroupMembershipService.isVolunteerInGroup(10, 1)).thenReturn(false);

        volunteerService.signUpForActivity(1, 1);

        verify(volunteerGroupMembershipService, times(1)).addVolunteerToGroup(10, 1);
        assertEquals(6, volunteerGroup.getCurrentVolunteers());
        verify(volunteerGroupService, times(1)).save(volunteerGroup);
    }

    @Test
    void testSignUpForActivity_AlreadyRegistered() {
        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(10);
        volunteerGroup.setCurrentVolunteers(5);
        volunteerGroup.setNumberOfVolunteersRequired(10);

        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));
        when(activityService.getById(1)).thenReturn(Optional.of(new ActivityEntity()));
        when(volunteerGroupMembershipService.isVolunteerInGroup(10, 1)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerService.signUpForActivity(1, 1);
        });

        assertEquals("El voluntario ya está registrado en el grupo.", exception.getMessage());
    }

    @Test
    void testValidateAge_TooYoung() {
        UserEntity user = new UserEntity();
        user.setId(1);
        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.SIN_ASIGNAR);
        user.setRole(role);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        volunteerEntity.getPersonalInformation().setBornDate(LocalDate.now().minusYears(15));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerService.saveVolunteer(volunteerEntity);
        });

        assertEquals("El voluntario debe tener al menos 16 años.", exception.getMessage());
    }

    @Test
    void testValidateAge_TooOld() {
        UserEntity user = new UserEntity();
        user.setId(1);
        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.SIN_ASIGNAR);
        user.setRole(role);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        volunteerEntity.getPersonalInformation().setBornDate(LocalDate.now().minusYears(141));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerService.saveVolunteer(volunteerEntity);
        });

        assertEquals("La edad del voluntario no puede exceder los 140 años.", exception.getMessage());
    }


    @Test
    void testAddVolunteerHours_VolunteerNotFound() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.addVolunteerHours(1, 5);
        });

        assertEquals("Volunteer not found with ID: 1", exception.getMessage());
    }

    @Test
    void testAddVolunteerActivity_VolunteerNotFound() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.addVolunteerActivity(1);
        });

        assertEquals("Volunteer not found with ID: 1", exception.getMessage());
    }

    @Test
    void testPromoteToLeader_VolunteerNotFound() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.promoteToLeader(1);
        });

        assertEquals("El voluntario con ID 1 no existe en la base de datos.", exception.getMessage());
    }

    @Test
    void testSignUpForActivity_VolunteerGroupNotFound() {
        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.signUpForActivity(1, 1);
        });

        assertEquals("No se encontró un grupo de voluntarios para la actividad con ID: 1", exception.getMessage());
    }

    @Test
    void testSignUpForActivity_ActivityNotFound() {
        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(1);
        volunteerGroup.setNumberOfVolunteersRequired(10);
        volunteerGroup.setCurrentVolunteers(5);
        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));

        when(activityService.getById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.signUpForActivity(1, 1);
        });

        assertEquals("No se encontró la actividad con ID: 1", exception.getMessage());
    }

    @Test
    void testSignUpForActivity_MissionNotFound() {
        VolunteerGroupEntity volunteerGroup = new VolunteerGroupEntity();
        volunteerGroup.setId(1);
        volunteerGroup.setNumberOfVolunteersRequired(10);
        volunteerGroup.setCurrentVolunteers(5);
        when(volunteerGroupService.getVolunteerGroupByActivityId(1)).thenReturn(Optional.of(volunteerGroup));

        ActivityEntity activity = new ActivityEntity();
        activity.setMissionId(1);
        activity.setVisibility(VisibilityEnum.PRIVADA);
        when(activityService.getById(1)).thenReturn(Optional.of(activity));

        when(missionService.getMissionById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.signUpForActivity(1, 1);
        });

        assertEquals("No se encontró la misión con ID: 1", exception.getMessage());
    }

    @Test
    void testValidateUserExists_Success() {
        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.SIN_ASIGNAR);

        UserEntity user = new UserEntity();
        user.setRole(role);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> volunteerService.validateUserExists(1));

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testValidateUserExists_UserNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.validateUserExists(1);
        });

        assertEquals("El usuario con ID 1 no existe en la bd", exception.getMessage());

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testValidateUserExists_UserHasRoleAssigned() {
        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.VOLUNTARIO);

        UserEntity user = new UserEntity();
        user.setRole(role);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerService.validateUserExists(1);
        });

        assertEquals("El usuario ya tiene un rol asignado", exception.getMessage());

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testGetVolunteerById_VolunteerExists() {
        VolunteerEntity volunteer = new VolunteerEntity();
        PersonalInformationEntity personalInfo = new PersonalInformationEntity();
        personalInfo.setBornDate(LocalDate.of(1990, 5, 15));
        volunteer.setPersonalInformation(personalInfo);

        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteer));

        Optional<VolunteerEntity> result = volunteerService.getVolunteerById(1);

        assertTrue(result.isPresent());
        assertEquals(34, result.get().getPersonalInformation().getAge());

        verify(volunteerRepository, times(1)).findById(1);
        verify(volunteerRepository, times(1)).save(volunteer);
    }

    @Test
    void testGetVolunteerById_VolunteerNotFound() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.empty());

        Optional<VolunteerEntity> result = volunteerService.getVolunteerById(1);

        assertFalse(result.isPresent());

        verify(volunteerRepository, times(1)).findById(1);
        verify(volunteerRepository, never()).save(any(VolunteerEntity.class));
    }

    @Test
    void testDeleteVolunteer_VolunteerNotFound() {
        when(volunteerRepository.findById(1)).thenReturn(Optional.empty());


        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerService.deleteVolunteer(1);
        });

        assertEquals("El voluntario con ID: 1 no existe en la base de datos", exception.getMessage());

        verify(volunteerRepository, times(1)).findById(1);
        verify(volunteerRepository, never()).delete(any(VolunteerEntity.class));
        verify(userService, never()).deleteUser(anyInt());
    }

    @Test
    void testDeleteVolunteer_VolunteerExists() {
        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setUserId(1);

        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteer));

        volunteerService.deleteVolunteer(1);

        verify(userService, times(1)).deleteUser(1);
        verify(volunteerRepository, times(1)).delete(volunteer);
    }
}
