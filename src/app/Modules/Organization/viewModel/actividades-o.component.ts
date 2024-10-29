import { Component, AfterViewInit, OnInit } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import interactionPlugin from '@fullcalendar/interaction';
import { ActivityService } from '../../Coordinators/model/services/activity.service';
import { ActivatedRoute } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';
import { MissionsService } from '../../Misiones/model/services/mission.service';

@Component({
  selector: 'app-actividades-o',
  templateUrl: '../view/actividades-o.component.html',
  styleUrl: '../styles/actividades-o.component.css'
})
export class ActividadesOComponent implements AfterViewInit, OnInit {
  currentStep: number = 1;
  showCalendar: boolean = true;
  missionForm: FormGroup;
  calendarOptions: CalendarOptions;
  isEditing = false;
  selectedActivity: any = null;
  missionId: number | null = null; // Id de la misión obtenido
  coordinators: any[] = []; // Lista de coordinadores
  public activities: any[] = [];

  constructor(
    private fb: FormBuilder,
    private activityService: ActivityService,
    private route: ActivatedRoute,
    private organizationService: OrganizationService,
    private missionsService: MissionsService
  ) {


    // Inicializamos el formulario sin valores predefinidos
    this.missionForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      activityCoordinator: ['', Validators.required],
      startDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      city: ['', Validators.required],
      locality: [''],
      address: ['', Validators.required],
      numberOfVolunteersRequired: [null, Validators.required],
      requiredHours: [null, Validators.required],
      numberOfBeneficiaries: [null, Validators.required],
      observations: [''],
      nameCommunityLeader: ['', Validators.required],
      emailCommunityLeader: ['', [Validators.required, Validators.email]],
      phoneCommunityLeader: ['', Validators.required],
      visibility: [true, Validators.required] // Aquí agregas el control de visibilidad
    });
  }

  ngOnInit(): void {

    // Obtener missionId desde query params
    this.route.queryParams.subscribe(params => {
      this.missionId = +params['id']; // Convertir el parámetro en número
      if (this.missionId) {
        console.log('MissionId obtenido de queryParams:', this.missionId);
        this.loadMissionActivities(this.missionId);
      } else {
        console.error('MissionId no encontrado en los queryParams.');
      }
    });
    this.loadCoordinators();
  }


  loadMissionActivities(missionId: number): void {
    this.activityService.getActivitiesByMissionId(missionId).subscribe(
      (activities: any[]) => {
        this.activities = activities;

        // Debugging para verificar la carga de actividades
        console.log('Actividades cargadas:', this.activities);

        // Solo inicializar la tabla una vez que las actividades han sido cargadas
        setTimeout(() => {
          this.initializeDataTable();  // Inicializar la tabla con las actividades cargadas
        }, 0);  // Dar un pequeño retraso para asegurar que Angular renderice las actividades
      },
      (error) => {
        console.error('Error al cargar actividades por misión', error);
      }
    );
  }



  ngAfterViewInit(): void {
    // Ya no inicializamos la tabla aquí, lo hacemos cuando las actividades están cargadas
  }

  loadCoordinators(): void {
    const orgId = localStorage.getItem('OrgId'); // Obtener el ID de la organización desde localStorage
    if (orgId) {
      this.activityService.getCoordinatorsByOrganizationId(+orgId).subscribe(
        (coordinators) => {
          this.coordinators = []; // Limpiamos la lista de coordinadores
          coordinators.forEach((coordinator) => {
            // Por cada coordinador, obtenemos los detalles del usuario
            this.organizationService.getUserDetails(coordinator.userId).subscribe(
              (userDetails) => {
                this.coordinators.push({
                  id: coordinator.id,
                  name: `${userDetails.firstName} ${userDetails.lastName}`, // Combina el nombre y apellido del usuario
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

  submitForm() {
    if (this.missionForm.valid) {
      const formData = this.missionForm.value;
      const newActivity = {
        missionId: this.missionId, // Asignar el id de la misión
        personalDataCommunityLeaderEntity: {
          nameCommunityLeader: formData.nameCommunityLeader,
          emailCommunityLeader: formData.emailCommunityLeader,
          phoneCommunityLeader: formData.phoneCommunityLeader
        },
        activityCoordinator: formData.activityCoordinator,
        title: formData.title,
        description: formData.description,
        date: formData.startDate,
        startTime: formData.startTime,
        endTime: formData.endTime,
        city: formData.city,
        locality: formData.locality,
        address: formData.address,
        numberOfVolunteersRequired: formData.numberOfVolunteersRequired,
        requiredHours: formData.requiredHours,
        visibility: 'PUBLICA',
        numberOfBeneficiaries: formData.numberOfBeneficiaries,
        observations: formData.observations
      };

      this.activityService.createActivity(newActivity).subscribe(
        response => {
          console.log('Actividad creada:', response);

          // Agregar el evento al calendario
          const newEvent = {
            title: `${formData.title}`,
            start: `${formData.startDate}T${formData.startTime}`,
            end: `${formData.startDate}T${formData.endTime}`,
            description: `${formData.description}`,
            extendedProps: {
              city: formData.city,
              address: formData.address
            }
          };

          const currentEvents = this.calendarOptions.events as any[];
          this.calendarOptions.events = [...currentEvents, newEvent];

          this.closeModal();
        },
        error => {
          console.error('Error al crear la actividad:', error);
        }
      );
    } else {
      alert('Por favor complete todos los campos requeridos.');
    }
  }

  closeModal() {
    const modal = document.getElementById('VolunteerModal');
    if (modal) {
      const modalInstance = (window as any).bootstrap.Modal.getInstance(modal);
      modalInstance.hide();
    }
  }

  mostrar(activityId: number): void {
    // Obtener detalles de la actividad seleccionada
    this.activityService.getActivityById(activityId).subscribe(
      (activityDetails) => {
        this.selectedActivity = activityDetails; // Asignar los detalles de la actividad
        this.showCalendar = false;  // Ocultar la lista y mostrar los detalles

        this.loadCoordinatorDetails(this.selectedActivity);
      },
      (error) => {
        console.error('Error al obtener los detalles de la actividad', error);
      }
    );
  }

  loadCoordinatorDetails(activity: any) {
    this.missionsService.getActivityCoordinator(activity.activityCoordinator).subscribe(
      (coordinatorDetails) => {
        activity.coordinatorPhone = coordinatorDetails.phoneActivityCoordinator;
        if (coordinatorDetails.userId) {
          this.organizationService.getUserDetails(coordinatorDetails.userId).subscribe(
            (userDetails) => {
              // Asignar los datos del coordinador al objeto de actividad
              activity.coordinatorName = `${userDetails.firstName} ${userDetails.lastName}`;
              activity.coordinatorEmail = userDetails.email;
            },
            (error) => {
              console.error('Error al obtener detalles del usuario coordinador', error);
              activity.coordinatorName = 'No asignado';
              activity.coordinatorEmail = '';
              activity.coordinatorPhone = '';
            }
          );
        }
      },
      (error) => {
        console.error('Error al obtener detalles del coordinador', error);
        activity.coordinatorName = 'No asignado';
        activity.coordinatorEmail = '';
        activity.coordinatorPhone = '';
      }
    );
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  handleBack2() {
    this.showCalendar = true;
    this.selectedActivity = null;
  }

  // Métodos para avanzar y retroceder entre los pasos del formulario
  nextStep() {
    this.currentStep++;
  }

  previousStep() {
    this.currentStep--;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'DISPONIBLE':
        return 'status-activo';
      case 'PENDIENTE':
        return 'status-pendiente';
      case 'COMPLETADA':
        return 'status-completado';
      case 'APLAZADO':
        return 'status-aplazado';
      default:
        return '';
    }
  }
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableActividadesO')) {
      $('#datatableActividadesO').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableActividadesO').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        scrollX: true,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrar página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty: '<span style="font-size: 0.875rem;">No hay registros</span>',
          infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
          lengthMenu: '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
          zeroRecords: '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
        }
      });
    }, 1);
  }

}

