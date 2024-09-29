import { Component } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { differenceInCalendarDays } from 'date-fns'; // Para calcular la diferencia de días

@Component({
  selector: 'app-actividades-v',
  templateUrl: '../view/actividades-v.component.html',
  styleUrls: ['../styles/actividades-v.component.css']
})
export class ActividadesVComponent {
  currentStep: number = 1;
  showCalendar: boolean = true;
  isEditing = false;
  selectedEvent: any = null;
  isModalOpen: boolean = false;

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

  calendarOptions: CalendarOptions;

  constructor() {
    this.setCalendarOptions();
    window.addEventListener('resize', this.setCalendarOptions.bind(this));
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
          dayHeaderFormat: isMobile
            ? { weekday: 'narrow' }  
            : { weekday: 'short' }  
        }
      },
      height: 'auto'
    };
  }

  handleEventClick(info: any) {
    console.log('Evento seleccionado:', info.event);

    this.selectedEvent = info.event;
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  
  cancelarSuscripcion() {
    const today = new Date(); 
    const eventDate = new Date(this.selectedEvent.start); 

   
    const difference = differenceInCalendarDays(eventDate, today);

    if (difference < 2) {
      alert('No puedes cancelar la suscripción con menos de 2 días de antelación al evento.');
    } else {
      const confirmacion = confirm('¿Estás seguro de que quieres cancelar la suscripción?');
      if (confirmacion) {
        this.eliminarEvento(this.selectedEvent);
        alert('Evento cancelado exitosamente.');
        this.closeModal();
      }
    }
  }

  // Método para eliminar el evento del calendario
  eliminarEvento(event: any) {
    
  }

  nextStep() {
    this.currentStep++;
  }

  previousStep() {
    this.currentStep--;
  }
}
