import { Component, AfterViewInit, OnInit } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import interactionPlugin from '@fullcalendar/interaction';
import { ActivityService } from '../../Coordinators/model/services/activity.service';
import { ActivatedRoute } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';

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
  missionId: number | null = null; // Id de la misión obtenido
  coordinators: any[] = []; // Lista de coordinadores
  public data = [
    {
      id: 1,
      title: 'Recogida de Basura',
      startDate: '2024-10-25',
      address: 'Calle 123, Ciudad Verde',
      noVolunteers: 10,
      status: 'DISPONIBLE',
    },
    {
      id: 2,
      title: 'Donación de Ropa',
      startDate: '2024-11-01',
      address: 'Av. Libertador, Plaza Mayor',
      noVolunteers: 5,
      status: 'COMPLETADO',
    },
    {
      id: 3,
      title: 'Convivencia en el Parque',
      startDate: '2024-11-10',
      address: 'Parque Central, Sector 4',
      noVolunteers: 20,
      status: 'APLAZADO',
    },
    {
      id: 4,
      title: 'Taller de Reciclaje',
      startDate: '2024-11-15',
      address: 'Centro Comunitario, Calle 45',
      noVolunteers: 8,
      status: 'PENDIENTE',
    },
    {
      id: 5,
      title: 'Plantación de Árboles',
      startDate: '2024-11-20',
      address: 'Calle Ecológica, Sector 3',
      noVolunteers: 15,
      status: 'COMPLETADO',
    },
    {
      id: 6,
      title: 'Campaña de Vacunación',
      startDate: '2024-11-25',
      address: 'Hospital Local, Av. San Martín',
      noVolunteers: 12,
      status: 'COMPLETADO',
    },
  ];
  
  

  constructor(
    private fb: FormBuilder,
    private activityService: ActivityService,
    private route: ActivatedRoute,
    private organizationService: OrganizationService
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
    
  
    
  }
  

  
  ngAfterViewInit(): void {
    this.initializeDataTable();
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

  mostrar() {
    this.showCalendar = false;
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  handleBack2() {
    this.showCalendar = true;
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
      case 'COMPLETADO':
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

