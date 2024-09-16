import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MissionsService } from '../model/services/mission.service'; // Servicio para obtener los detalles
import { Mission } from '../model/mission.model'; // Modelo de Misión

@Component({
  selector: 'app-detalles-m',
  templateUrl: '../view/detalles-m.component.html',
  styleUrls: ['../../../styles/detalles-m.component.css']
})
export class DetallesMComponent implements OnInit {
  selectedSection: string = 'descripcion'; // Por defecto, la sección de Descripción está activa
  isEditing = false;
  missionId: number | null = null; // Almacenar el id de la misión
  missionDetails: Mission | null = null; // Almacenar los detalles de la misión

  constructor(private route: ActivatedRoute, private missionsService: MissionsService) { }

  ngOnInit(): void {
    // Obtener el ID de la misión de los parámetros de la URL
    this.route.queryParams.subscribe(params => {
      this.missionId = +params['id']; // Obtener el 'id' como número
      if (this.missionId) {
        this.getMissionDetails(this.missionId); // Llamar al servicio para obtener detalles
      }
    });
  }

  // Llamar al servicio para obtener los detalles de la misión
  getMissionDetails(id: number): void {
    this.missionsService.getMissionById(id).subscribe(
      (mission: Mission) => {
        this.missionDetails = mission;
        console.log('Detalles de la misión cargada:', this.missionDetails);
      },
      (error) => {
        console.error('Error al cargar los detalles de la misión:', error);
      }
    );
  }

  // Función para cambiar la sección activa
  selectSection(section: string): void {
    this.selectedSection = section;
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
  }

  save(): void {
    // Aquí puedes manejar la lógica de guardar los cambios
    this.isEditing = false;
  }
}
