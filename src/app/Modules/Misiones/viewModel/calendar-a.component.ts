import { Component, AfterViewInit, OnInit } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import interactionPlugin from '@fullcalendar/interaction';
import { ActivityService } from '../../Coordinators/model/services/activity.service';
import { ActivatedRoute } from '@angular/router';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-calendar-a',
  templateUrl: '../view/calendar-a.component.html',
  styleUrls: ['../styles/calendar-a.component.css']
})
export class CalendarAComponent implements AfterViewInit, OnInit {
  currentStep: number = 1;
  showCalendar: boolean = true;
  missionForm: FormGroup;
  calendarOptions: CalendarOptions;
  isEditing = false;
  missionId: number | null = null; // Id de la misión obtenido
  coordinators: any[] = []; // Lista de coordinadores

  constructor(
    private fb: FormBuilder,
    private activityService: ActivityService,
    private route: ActivatedRoute,
    private organizationService: OrganizationService
  ) {
    this.setCalendarOptions();
    window.addEventListener('resize', this.setCalendarOptions.bind(this));

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
        this.loadActivities(); // Cargar actividades relacionadas a la misión
        this.loadCoordinators(); // Cargar coordinadores relacionados a la misión
      } else {
        console.error('MissionId no encontrado en los queryParams.');
      }
    });
  }

  ngAfterViewInit() {
    this.loadActivities(); // Cargar actividades al iniciar el componente
  }

  setCalendarOptions() {
    const isMobile = window.innerWidth <= 768;
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      eventClick: this.handleEventClick.bind(this),
      plugins: [dayGridPlugin, interactionPlugin],
      editable: true,
      selectable: true,
      locale: 'es',
      events: this.calendarEvents,
      headerToolbar: {
        left: 'prev,next',
        center: 'title',
        right: ''
      },
      views: {
        dayGridMonth: {
          buttonText: 'Mes',
          dayHeaderFormat: isMobile ? { weekday: 'narrow' } : { weekday: 'short' }
        }
      },
      height: 'auto'
    };
  }

  // Lista de eventos del calendario
  calendarEvents: EventInput[] = [];

  // Cargar las actividades desde el servicio y agregarlas al calendario
  loadActivities() {
    if (this.missionId) {
      this.activityService.getActivitiesByMissionId(this.missionId).subscribe(
        activities => {
          const events = activities.map(activity => ({
            title: activity.title,
            start: `${activity.date}T${activity.startTime}`,
            end: `${activity.date}T${activity.endTime}`,
            description: activity.description,
            extendedProps: {
              city: activity.city,
              address: activity.address
            }
          }));
          this.calendarEvents = events; // Asignar los eventos al calendario
          this.calendarOptions.events = this.calendarEvents; // Actualizar las opciones del calendario
        },
        error => {
          console.error('Error al cargar actividades:', error);
        }
      );
    }
  }

  // Cargar los coordinadores de la organización
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

  handleEventClick(info: any) {
    console.log('Evento seleccionado:', info.event);
    this.showCalendar = false;
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  handleBack() {
    this.showCalendar = true;
  }

  // Métodos para avanzar y retroceder entre los pasos del formulario
  nextStep() {
    this.currentStep++;
  }

  previousStep() {
    this.currentStep--;
  }
}
