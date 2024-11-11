import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivityService } from '../../Coordinators/model/services/activity.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';
import { MissionsService } from '../model/services/mission.service';

@Component({
  selector: 'app-edit-a',
  templateUrl: '../view/edit-a.component.html',
  styleUrls: ['../styles/edit-a.component.css']
})
export class EditAComponent implements OnInit {
  @Input() activity: any; // Recibe la actividad seleccionada desde el componente padre
  currentStep: number = 1;
  @Output() cancel = new EventEmitter<void>();

  // Formulario reactivo
  myForm: FormGroup;

  coordinators: any[] = [];  // Lista de coordinadores disponibles

  constructor(
    private fb: FormBuilder,
    private activityService: ActivityService,
    private missionsService: MissionsService,
    private organizationService: OrganizationService
  ) { }

  ngOnInit(): void {
    // Inicializar el formulario con valores predeterminados
    this.myForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      activityCoordinator: [0, Validators.required],
      startDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      city: ['', Validators.required],
      locality: ['', Validators.required],
      address: ['', Validators.required],
      numberOfVolunteersRequired: [0, Validators.required],
      requiredHours: [0, Validators.required],
      numberOfBeneficiaries: [0, Validators.required],
      observations: [''],
      visibility: [false]
    });

    // Cargar los datos si existen
    if (this.activity) {
      this.loadFormData();
    }
    this.loadCoordinators();
  }

  // Cargar los datos de la actividad seleccionada en el formulario
  loadFormData() {
    this.myForm.patchValue({
      title: this.activity.title,
      description: this.activity.description,
      activityCoordinator: this.activity.activityCoordinator,
      startDate: this.activity.date,
      startTime: this.activity.startTime,
      endTime: this.activity.endTime,
      city: this.activity.city,
      locality: this.activity.locality,
      address: this.activity.address,
      numberOfVolunteersRequired: this.activity.numberOfVolunteersRequired,
      requiredHours: this.activity.requiredHours,
      numberOfBeneficiaries: this.activity.numberOfBeneficiaries,
      observations: this.activity.observations,
      visibility: this.activity.visibility === 'PUBLICA'
    });
  }

  // Cargar la lista de coordinadores
  loadCoordinators(): void {
    const orgId = localStorage.getItem('OrgId'); // Obtener el ID de la organización desde localStorage
    if (orgId) {
      this.activityService.getCoordinatorsByOrganizationId(+orgId).subscribe(
        (coordinators) => {
          console.log('Coordinadores cargados:', coordinators); // Agrega esto para depurar
          this.coordinators = []; // Limpiamos la lista de coordinadores
          coordinators.forEach((coordinator) => {
            this.organizationService
              .getUserDetails(coordinator.userId)
              .subscribe(
                (userDetails) => {
                  this.coordinators.push({
                    id: coordinator.id,
                    name: `${userDetails.firstName} ${userDetails.lastName}`,
                  });
                },
                (error) => {
                  console.error('Error al obtener los detalles del usuario:', error);
                }
              );
          });
        },
        (error) => {
          console.error('Error al cargar los coordinadores:', error);
        }
      );
    } else {
      console.error('OrgId no encontrado en el localStorage');
    }
  }


  // Actualizar la actividad
  updateActivity() {
    if (this.myForm.valid) {
      const updatedActivity = this.myForm.value;
      this.missionsService.updateActivity(this.activity.id, updatedActivity).subscribe(
        (response) => {
          console.log('Actividad actualizada correctamente', response);
          this.currentStep = 1;  // Volver al paso 1
        },
        (error) => {
          console.error('Error al actualizar la actividad:', error);
        }
      );
    }
  }

  // Emitir el evento de cancelación
  onCancel() {
    this.cancel.emit();
  }

  // Navegar al siguiente paso
  nextStep() {
    this.currentStep++;
  }

  // Navegar al paso anterior
  previousStep() {
    this.currentStep--;
  }
}
