export interface Mission {
  id: number;
  organizationId: number;
  missionType: string;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  department: string;
  visibility: string;
  volunteerMissionRequirementsEnumList: string[];
  requiredSkillsList: string[];
  status: string;
}
