import { Component, AfterViewInit } from '@angular/core';
import { CalendarOptions, EventInput  } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-calendar-a',
  templateUrl: '../view/calendar-a.component.html',
  styleUrls: ['../styles/calendar-a.component.css']
})
export class CalendarAComponent implements AfterViewInit {
  currentStep: number = 1;  
  showCalendar: boolean = true;
  missionForm: FormGroup;  
  calendarOptions: CalendarOptions;
  isEditing=false;
  // Lista de eventos
  calendarEvents: EventInput[] = [
    {
      id: '1', 
      title: 'BCH237',
      start: '2024-09-30T09:00:00',
      end: '2024-09-30T11:30:00',
      extendedProps: {
        city: 'Ciudad X',
        address: 'Dirección Y'
      },
      description: 'Lecture'
    }
  ];
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
          dayHeaderFormat: isMobile
            ? { weekday: 'narrow' }  
            : { weekday: 'short' }  
        }
      },
      height: 'auto', 
      
      
    };
  }
  

  constructor(private fb: FormBuilder) {
    this.setCalendarOptions();
    window.addEventListener('resize', this.setCalendarOptions.bind(this));
    // Inicializamos el formulario con los campos requeridos
    this.missionForm = this.fb.group({
      title: ['Plantación de árboles', Validators.required],
      description: ['Plantación de 100 árboles en el parque central.', Validators.required],
      activityCoordinator: ['0', Validators.required],
      startDate: ['2024-09-03', Validators.required],
      startTime: ['08:00', Validators.required],
      endTime: ['12:00', Validators.required],
      city: ['Bogotá', Validators.required],
      locality: ['Usaquén'],
      address: ['Parque Central, Calle 123', Validators.required],
      numberOfVolunteersRequired: [20, Validators.required],
      requiredHours: [4, Validators.required],
      numberOfBeneficiaries: [100, Validators.required],
      observations: ['Llevar agua y protector solar.'],
      nameCommunityLeader: ['Juan Pérez', Validators.required],
      emailCommunityLeader: ['juan.perez@example.com', [Validators.required, Validators.email]],
      phoneCommunityLeader: ['3216549870', Validators.required]
    });
  }

  ngAfterViewInit() {
  }

 

  
 
  

  
  nextStep() {
    this.currentStep++;
  }
  
 
  previousStep() {
    this.currentStep--;
  }

  submitForm() {
    if (this.missionForm.valid) {
      const formData = this.missionForm.value;

      
      const newEvent = {
        title: formData.title,
        start: `${formData.startDate}T${formData.startTime}`,
        end: `${formData.startDate}T${formData.endTime}`,
        description: formData.description,
        extendedProps: {
          activityCoordinator: formData.activityCoordinator,
          city: formData.city,
          address: formData.address,
          volunteersRequired: formData.numberOfVolunteersRequired,
          hoursRequired: formData.requiredHours,
          beneficiaries: formData.numberOfBeneficiaries,
          observations: formData.observations,
          communityLeader: {
            name: formData.nameCommunityLeader,
            email: formData.emailCommunityLeader,
            phone: formData.phoneCommunityLeader
          }
        }
      };

      
      const currentEvents = this.calendarOptions.events as any[];
      this.calendarOptions.events = [...currentEvents, newEvent];

      
      this.closeModal();

      console.log('Evento creado:', newEvent);  
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
   // Manejador cuando se hace clic en un evento
   handleEventClick(info: any) {
    console.log('Evento seleccionado:', info.event);
    
    // Aquí podrías pasar los datos del evento al componente de detalles si lo deseas
    // Cambia el valor de `showCalendar` para mostrar el componente de detalles
    this.showCalendar = false;
  }
  toggleEdit() {
    this.isEditing = !this.isEditing;
  }
   // Manejador para el evento "Back" emitido desde el componente `app-detalles-a`
   handleBack() {
    this.showCalendar = true;  // Volver a mostrar el calendario
  }
}
