import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Mission } from '../model/mission.model';
import { MissionsService } from '../model/services/mission.service';

@Component({
  selector: 'app-edit-m',
  templateUrl: '../view/edit-m.component.html',
  styleUrls: ['../styles/edit-m.component.css']
})
export class EditMComponent implements OnInit {
  @Input() mission: Mission | null = null; // Recibimos la misión desde el padre
  @Output() cancel = new EventEmitter<void>();
  @Output() missionUpdated = new EventEmitter<void>();

  missionType = '';
  title = '';
  description = '';
  startDate = '';
  endDate = '';
  department = '';
  volunteerRequirements: string[] = [];
  requiredSkills: string[] = [];
  visibility = false;
  showAlert = false;
  showAlert2 = false;

  constructor(private missionsService: MissionsService) { }

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
    if (this.mission && this.mission.id !== undefined) {
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

      // Llamamos al servicio para actualizar la misión
      this.missionsService.updateMission(this.mission.id, updatedMission).subscribe(
        (response) => {
          console.log('Misión actualizada con éxito:', response);
          this.showAlert=true;
          setTimeout(() => (this.showAlert = false), 3000);
          this.missionUpdated.emit();
          // Aquí puedes manejar la respuesta si es necesario, por ejemplo, redirigir a otra página
        },
        (error) => {
          console.error('Error al actualizar la misión:', error);
          this.showAlert2=true;
          setTimeout(() => (this.showAlert2 = false), 3000);
        }
      );
    } else {
      console.error('La misión no tiene un ID válido');
      this.showAlert2=true;
          setTimeout(() => (this.showAlert2 = false), 3000);
    }
  }
}