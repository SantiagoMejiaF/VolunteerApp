import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Mission } from '../model/mission.model';

@Component({
  selector: 'app-edit-m',
  templateUrl: '../view/edit-m.component.html',
  styleUrls: ['../styles/edit-m.component.css']
})
export class EditMComponent implements OnInit {
  @Input() mission: Mission | null = null; // Recibimos la misión desde el padre
  @Output() cancel = new EventEmitter<void>();

  missionType = '';
  title = '';
  description = '';
  startDate = '';
  endDate = '';
  department = '';
  volunteerRequirements: string[] = [];
  requiredSkills: string[] = [];
  visibility = false;

  ngOnInit(): void {
    if (this.mission) {
      this.missionType = this.mission.missionType;
      this.title = this.mission.title;
      this.description = this.mission.description;
      this.startDate = this.mission.startDate;
      this.endDate = this.mission.endDate;
      this.department = this.mission.department;
      this.volunteerRequirements = this.mission.volunteerMissionRequirementsEnumList;
      this.requiredSkills = this.mission.requiredSkillsList;
      this.visibility = this.mission.visibility === 'PUBLICA';
    }
  }

  onCancel() {
    this.cancel.emit();
  }

  // Aquí iría la lógica para actualizar la misión en el backend
  onSubmit() {
    const updatedMission = {
      missionType: this.missionType,
      title: this.title,
      description: this.description,
      startDate: this.startDate,
      endDate: this.endDate,
      department: this.department,
      volunteerMissionRequirementsEnumList: this.volunteerRequirements,
      requiredSkillsList: this.requiredSkills,
      visibility: this.visibility ? 'PUBLICA' : 'PRIVADA'
    };

    // Aquí puedes emitir un evento o llamar un servicio para actualizar la misión en el servidor.
    console.log('Misión actualizada:', updatedMission);
  }
}
