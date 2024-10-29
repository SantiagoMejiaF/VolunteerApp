import { Component, OnInit } from '@angular/core';
import { CalendarOptions, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { VolunteerService } from '../model/services/volunteer.service'; // Asegúrate de importar el servicio correcto
import { differenceInCalendarDays } from 'date-fns';

@Component({
  selector: 'app-actividades-v',
  templateUrl: '../view/actividades-v.component.html',
  styleUrls: ['../styles/actividades-v.component.css']
})
export class ActividadesVComponent implements OnInit {
  currentStep: number = 1;
  showCalendar: boolean = true;
  isEditing = false;
  selectedEvent: any = null;
  isModalOpen: boolean = false;

  calendarEvents: EventInput[] = [];
  calendarOptions: CalendarOptions;
  volunteerId: number; // ID del voluntario, asumimos que lo obtendrás de alguna parte

  constructor(private volunteerService: VolunteerService) { }

  ngOnInit() {
    this.volunteerId = Number(localStorage.getItem('volunteerId')); // Obtener el volunteerId desde localStorage
    this.loadActividades();
    this.setCalendarOptions();
    window.addEventListener('resize', this.setCalendarOptions.bind(this));
  }

  loadActividades() {
    this.volunteerService.getActivitiesByVolunteerId(this.volunteerId).subscribe((data: any[]) => {
      // Mapear las actividades obtenidas a un formato compatible con FullCalendar
      this.calendarEvents = data.map(actividad => ({
        id: actividad.id,
        title: actividad.title,
        start: actividad.date + 'T' + actividad.startTime, // Combinar la fecha y la hora de inicio
        end: actividad.date + 'T' + actividad.endTime,     // Combinar la fecha y la hora de fin
        description: actividad.description,
        extendedProps: {
          city: actividad.city,
          address: actividad.address
        }
      }));

      // Asignar los eventos al calendario
      this.calendarOptions.events = this.calendarEvents;
    });
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

  eliminarEvento(event: any) {
    // Aquí implementa la lógica para eliminar el evento
  }
}
