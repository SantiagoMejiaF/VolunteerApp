import { Component, AfterViewInit, OnInit } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import interactionPlugin from '@fullcalendar/interaction';
import { ActivityService } from '../../Coordinators/model/services/activity.service';
import { ActivatedRoute } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';
import { MissionsService } from '../../Misiones/model/services/mission.service';
import { Modal } from 'bootstrap';

@Component({
  selector: 'app-actividades-o',
  templateUrl: '../view/actividades-o.component.html',
  styleUrl: '../styles/actividades-o.component.css',
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
  showAlert = false;
  showAlert2 = false;
  startTimes: string[] = [];
  endTimes: string[] = [];

  constructor(
    private fb: FormBuilder,
    private activityService: ActivityService,
    private route: ActivatedRoute,
    private organizationService: OrganizationService,
    private missionsService: MissionsService
  ) {
    this.generateTimeOptions();
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
      visibility: [true, Validators.required], // Aquí agregas el control de visibilidad
    });
  }
  generateTimeOptions() {
    const times = [];
    for (let hour = 0; hour < 24; hour++) {
      times.push(this.formatTime12Hour(hour, 0)); // Ejemplo: "2:00 AM"
      times.push(this.formatTime12Hour(hour, 30)); // Ejemplo: "2:30 AM"
    }
    this.startTimes = times;
    this.endTimes = times;
  }

  formatTime12Hour(hour: number, minutes: number): string {
    const suffix = hour >= 12 ? 'PM' : 'AM';
    const hour12 = hour % 12 === 0 ? 12 : hour % 12;
    const minuteString = minutes === 0 ? '00' : `${minutes}`;
    return `${hour12}:${minuteString} ${suffix}`;
  }
  ngOnInit(): void {
    // Obtener missionId desde query params
    this.route.queryParams.subscribe((params) => {
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

        this.initializeDataTable();
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
            this.organizationService
              .getUserDetails(coordinator.userId)
              .subscribe(
                (userDetails) => {
                  this.coordinators.push({
                    id: coordinator.id,
                    name: `${userDetails.firstName} ${userDetails.lastName}`, // Combina el nombre y apellido del usuario
                  });
                },
                (error) => {
                  console.error(
                    'Error al obtener los detalles del usuario:',
                    error
                  );
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

  convertTo24HourFormat(time: string): string {
    const [timePart, modifier] = time.split(' '); // Separar la parte de tiempo y el modificador AM/PM
    let [hours, minutes] = timePart.split(':').map(Number); // Dividir horas y minutos

    if (modifier === 'PM' && hours < 12) {
      hours += 12; // Convertir a 24 horas
    } else if (modifier === 'AM' && hours === 12) {
      hours = 0; // Convertir 12 AM a 0
    }

    return `${hours.toString().padStart(2, '0')}:${minutes
      .toString()
      .padStart(2, '0')}`; // Retornar en formato HH:mm
  }

  refreshActivities(): void {
    if (this.missionId) {
      this.loadMissionActivities(this.missionId); // Cargar actividades de la misión
    } else {
      console.error('MissionId no disponible para refrescar las actividades.');
    }
  }

  submitForm() {
    if (this.missionForm.valid) {
      const formData = this.missionForm.value;

      const startTime24 = this.convertTo24HourFormat(formData.startTime);
      const endTime24 = this.convertTo24HourFormat(formData.endTime);
      const newActivity = {
        missionId: this.missionId,
        personalDataCommunityLeaderEntity: {
          nameCommunityLeader: formData.nameCommunityLeader,
          emailCommunityLeader: formData.emailCommunityLeader,
          phoneCommunityLeader: formData.phoneCommunityLeader,
        },
        activityCoordinator: formData.activityCoordinator,
        title: formData.title,
        description: formData.description,
        date: formData.startDate,
        startTime: startTime24,
        endTime: endTime24,
        city: formData.city,
        locality: formData.locality,
        address: formData.address,
        numberOfVolunteersRequired: formData.numberOfVolunteersRequired,
        requiredHours: formData.requiredHours,
        visibility: formData.visibility ? 'PUBLICA' : 'PRIVADA',
        numberOfBeneficiaries: formData.numberOfBeneficiaries,
        observations: formData.observations,
      };

      this.activityService.createActivity(newActivity).subscribe(
        (response) => {
          console.log('Actividad creada:', response);

          // Crear el nuevo evento
          const newEvent = {
            title: `${formData.title}`,
            start: `${formData.startDate}T${formData.startTime}`,
            end: `${formData.startDate}T${formData.endTime}`,
            description: `${formData.description}`,
            extendedProps: {
              city: formData.city,
              address: formData.address,
            },
          };
          // Cerrar el modal programáticamente usando la clase `Modal`
          // Cerrar el modal programáticamente usando la clase `Modal`
          this.closeModal();
          this.showAlert = true;
          setTimeout(() => (this.showAlert = false), 3000);
          this.refreshActivities();
          // Añadir el nuevo evento
          // Asegúrate de que `events` esté definido y sea un array
          if (!this.calendarOptions.events) {
            this.calendarOptions.events = [];
          }

          const currentEvents = this.calendarOptions.events as any[];
          this.calendarOptions.events = [...currentEvents, newEvent];
        },
        (error) => {
          console.error('Error al crear la actividad:', error);
          this.closeModal();
          this.showAlert2 = true;
          setTimeout(() => (this.showAlert2 = false), 3000);
        }
      );
    } else {
      alert('Por favor complete todos los campos requeridos.');
    }
  }

  openModal(event: Event): void {
    event.preventDefault();
    const modalElement = document.getElementById('VolunteerModal');

    if (modalElement) {
      modalElement.style.display = 'block'; // Mostrar el modal
      modalElement.classList.add('show'); // Agregar la clase que lo hace visible
      document.body.classList.add('modal-open'); // Asegurarse de que el body esté en modo modal

      // Monitorea la adición de backdrops
      const observer = new MutationObserver((mutationsList) => {
        for (let mutation of mutationsList) {
          mutation.addedNodes.forEach((node) => {
            if (
              node instanceof HTMLElement &&
              node.classList.contains('modal-backdrop')
            ) {
              console.log('Se ha añadido un modal-backdrop');
            }
          });
        }
      });

      observer.observe(document.body, { childList: true, subtree: true });
    }
  }

  closeModal(): void {
    const modalElement = document.getElementById('VolunteerModal');
    if (modalElement) {
      const modalInstance = (window as any).bootstrap.Modal.getInstance(
        modalElement
      );
      if (modalInstance) {
        modalInstance.hide();
      }

      // Limpiar el formulario después de cerrar el modal
      this.missionForm.reset(); // Reiniciar el formulario
      this.currentStep = 1;
      // Limpiar cualquier estado adicional relacionado con la actividad
      this.selectedActivity = null; // Resetear la actividad seleccionada

      // Remover backdrops
      const backdrops = document.querySelectorAll('.modal-backdrop');
      backdrops.forEach((backdrop) => backdrop.remove());
    }
  }

  mostrar(activityId: number): void {
    // Obtener detalles de la actividad seleccionada
    this.activityService.getActivityById(activityId).subscribe(
      (activityDetails) => {
        this.selectedActivity = activityDetails; // Asignar los detalles de la actividad
        this.showCalendar = false; // Ocultar la lista y mostrar los detalles

        this.loadCoordinatorDetails(this.selectedActivity);
      },
      (error) => {
        console.error('Error al obtener los detalles de la actividad', error);
      }
    );
  }

  loadCoordinatorDetails(activity: any) {
    this.missionsService
      .getActivityCoordinator(activity.activityCoordinator)
      .subscribe(
        (coordinatorDetails) => {
          activity.coordinatorPhone =
            coordinatorDetails.phoneActivityCoordinator;
          if (coordinatorDetails.userId) {
            this.organizationService
              .getUserDetails(coordinatorDetails.userId)
              .subscribe(
                (userDetails) => {
                  // Asignar los datos del coordinador al objeto de actividad
                  activity.coordinatorName = `${userDetails.firstName} ${userDetails.lastName}`;
                  activity.coordinatorEmail = userDetails.email;
                },
                (error) => {
                  console.error(
                    'Error al obtener detalles del usuario coordinador',
                    error
                  );
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

  onComeBack() {
    this.showCalendar = true;
    this.initializeDataTable();
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
    // Destruir la tabla si ya está inicializada
    if ($.fn.dataTable.isDataTable('#datatableActividadesO')) {
      $('#datatableActividadesO').DataTable().destroy();
    }

    // Inicializa la tabla con los nuevos datos
    setTimeout(() => {
      const table = $('#datatableActividadesO').DataTable({
        data: this.activities, // Asegúrate de que esta línea esté configurando los datos de la tabla
        columns: [
          { data: 'id', title: '#' },
          { data: 'title', title: 'Nombre Actividad' },
          { data: 'date', title: 'Fecha inicio' },
          { data: 'address', title: 'Dirección' },
          { data: 'numberOfVolunteersRequired', title: '# voluntarios' },
          {
            data: 'activityStatus',
            title: 'Status',
            render: (data) => {
              let bgColor = 'rgba(220, 234, 255, 1)';
              let textColor = '#03A3AE';

              if (data === 'COMPLETADA') {
                bgColor = 'rgba(220, 255, 229, 1)';
                textColor = '#3FC28A';
              } else if (data === 'CANCELADA') {
                bgColor = 'rgba(255,229,220,255)';
                textColor = '#F36060';
              }

              return `<span style="background-color:${bgColor}; color:${textColor}; padding: 4px 8px; border-radius: 12px; display: inline-block;font-weight: bold;">${data}</span>`;
            }
          },
          {
            data: null,
            title: 'Acción',
            render: (data, type, row) => `
                    <a  class="show-details" data-id="${row.id}" style="border: none; background: none;">
                        <i class="bi bi-eye" style="font-size: 1.3rem; color: #000000;"></i>
                    </a>
                    <a   data-id="${row.id}" style="border: none; background: none;">
                        <i class="bi bi-trash" style="font-size: 1.3rem; color: #000000;"></i>
                    </a>
                `,
          },
        ],
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        scrollX: true,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrar página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty:
            '<span style="font-size: 0.875rem;">No hay registros</span>',
          infoFiltered:
            '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
          lengthMenu:
            '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
          zeroRecords:
            '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
        },
      });

      // Delegación de eventos para el botón de mostrar
      $('#datatableActividadesO').on('click', '.show-details', (event) => {
        event.preventDefault();
        const activityId = $(event.currentTarget).data('id'); // Obtener el ID de la actividad
        this.mostrar(activityId); // Llamar al método mostrar con el ID
      });
    }, 1);
  }
}
