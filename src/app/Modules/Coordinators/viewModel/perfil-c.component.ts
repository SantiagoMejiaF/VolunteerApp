import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-perfil-c',
  templateUrl: '../view/perfil-c.component.html',
  styleUrl: '../styles/perfil-c.component.css'
})
export class PerfilCComponent implements OnInit {
  currentContent: string = 'content1';

  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerId: number = 0;

  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  myForm: FormGroup;
  ngOnInit() {}

  constructor(
    private fb: FormBuilder,
    
  ) {
    this.myForm = this.fb.group({
      cell: [''],
      email: [''],
      
    });

   
  }
  onSubmit(): void {}
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