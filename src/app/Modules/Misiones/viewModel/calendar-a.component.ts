import { Component, AfterViewInit } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-calendar-a',
  templateUrl: '../view/calendar-a.component.html',
  styleUrls: ['../../../styles/calendar-a.component.css']
})
export class CalendarAComponent implements AfterViewInit {
  currentStep: number = 1;  
  showCalendar: boolean = true;
  missionForm: FormGroup;  
  isEditing=false;
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth', 
    eventClick: this.handleEventClick.bind(this),
    plugins: [dayGridPlugin, interactionPlugin],
    editable: true,
    selectable: true,
    locale: 'es', 
    events: [
      {
        title: 'BCH237',
        start: '2024-09-17',
        end: '2024-09-17T11:30:00',
        extendedProps: {
          department: 'BioChemistry'
        },
        description: 'Lecture'
      }
    ],
    headerToolbar: {
      left: 'prev,next', 
      center: 'title',  
      right: ''          
    },
    views: {
      dayGridMonth: {
        buttonText: 'Mes',
        dayHeaderFormat: { weekday: 'short' }
      }
    },
    height: 'auto', 
    
    windowResize: () => {
      const width = window.innerWidth;
      if (width <= 768) {
        this.adjustButtonSizes('small');
        this.adjustTitleSize('small');
        this.calendarOptions.headerToolbar = {
          left: 'prev,next', 
          center: 'title',   
          right: ''          
        };
        this.calendarOptions.views = {
          dayGridMonth: {
            buttonText: 'Mes',
            dayHeaderFormat: { weekday: 'narrow' } 
          }
        };
        this.calendarOptions.contentHeight = 'auto'; 
      } else {
        this.adjustButtonSizes('normal');
        this.adjustTitleSize('normal');
        this.calendarOptions.headerToolbar = {
          left: 'prev,next',
          center: 'title',
          right: '' 
        };
        this.calendarOptions.views = {
          dayGridMonth: {
            buttonText: 'Mes',
            dayHeaderFormat: { weekday: 'short' } 
          }
        };
        this.calendarOptions.contentHeight = 600; 
      }
    }
  };

  constructor(private fb: FormBuilder) {
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
    this.adjustButtonSizes('normal'); 
    this.applyWeekdayHeaderStyles(); 
  }

  adjustButtonSizes(size: 'small' | 'normal') {
    const buttons = document.querySelectorAll('.fc-prev-button, .fc-next-button');
    buttons.forEach((button) => {
      const htmlButton = button as HTMLElement; 
      if (size === 'small') {
        htmlButton.style.fontSize = '0.4rem'; 
        htmlButton.style.padding = '5px 10px'; 
        htmlButton.style.background = '#ED4B4B'; 
        htmlButton.style.border = 'none'; 
      } else {
        htmlButton.style.fontSize = ''; 
        htmlButton.style.padding = '';  
        htmlButton.style.background = '#ED4B4B'; 
        htmlButton.style.border = 'none'; 
      }
    });
  }

  applyWeekdayHeaderStyles() {
    const dayHeaders = document.querySelectorAll('.fc-col-header-cell');
    dayHeaders.forEach((header) => {
      const htmlHeader = header as HTMLElement;
      htmlHeader.style.backgroundColor = '#ED4B4B'; 
      htmlHeader.style.color = 'white';           
      htmlHeader.style.fontWeight = 'bold';         
      htmlHeader.style.textAlign = 'center';        
    });
  }

 
  adjustTitleSize(size: 'small' | 'normal') {
    const title = document.querySelector('.fc-toolbar-title') as HTMLElement;
    if (title) {
      if (size === 'small') {
        title.style.fontSize = '1.2rem'; 
        title.style.marginLeft = '10px'; 
      } else {
        title.style.fontSize = '';
      }
    }
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
    setTimeout(() => {
      this.adjustButtonSizes('normal');
      this.applyWeekdayHeaderStyles();
    }, 0);  // Asegurarse de que las personalizaciones se apliquen después de que el calendario se renderice
  }
}
