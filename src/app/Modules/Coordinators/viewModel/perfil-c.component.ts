import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { AdminService } from '../../AdminUser/model/services/admin.service';
import { ActivityService } from '../model/services/activity.service';

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
  image: any;
  phoneNumber: string = '';

  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  myForm: FormGroup;
  ngOnInit() {
    this.loadUserInfo();
    this.loadCoordinatorDetails();

    this.myForm.setValue({
      firstName: this.firstName,
      lastName: this.lastName,
      cell: this.phoneNumber,
      email: this.email
    });
  }

  constructor(
    private fb: FormBuilder, private adminService: AdminService, private activityService: ActivityService

  ) {
    this.myForm = this.fb.group({
      firstName: [''], // Agrega este control
      lastName: [''],  // Agrega este control
      cell: [''],
      email: [''],
    });


  }

  loadUserInfo() {
    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    if (userInfo) {
      this.firstName = userInfo.firstName;
      this.lastName = userInfo.lastName;
      this.email = userInfo.email;
      this.image = userInfo.image; // Cargar la URL de la imagen
    }
  }

  loadCoordinatorDetails() {
    const userId = JSON.parse(localStorage.getItem('userInfo')!).id; // Obtener el userId del localStorage
    this.adminService.getCoordinatorDetails(userId).subscribe(
      (coordinatorDetails) => {
        this.phoneNumber = coordinatorDetails.phoneActivityCoordinator; // Asigna el número telefónico
      },
      (error) => {
        console.error('Error loading coordinator details:', error);
      }
    );
  }

  onSubmit(): void {
    if (this.myForm.valid) {
      // Crear un objeto con los valores actuales o del formulario, si se han modificado
      const updatedData = {
        firstName: this.myForm.value.firstName || this.firstName,
        lastName: this.myForm.value.lastName || this.lastName,
        phone: this.myForm.value.cell || this.phoneNumber
      };

      // Obtener el coordinatorId del localStorage
      const coordinatorId = localStorage.getItem('coordinatorId');
      if (coordinatorId) {
        this.activityService.updateCoordinatorDetails(parseInt(coordinatorId), updatedData).subscribe(
          (response) => {
            console.log('Datos actualizados:', response);
            alert('Datos actualizados correctamente');
            // Actualizar la información en el componente después de la actualización
            this.firstName = updatedData.firstName;
            this.lastName = updatedData.lastName;
            this.phoneNumber = updatedData.phone;
          },
          (error) => {
            console.error('Error al actualizar los datos:', error);
            alert('Hubo un error al actualizar los datos');
          }
        );
      } else {
        console.error('No se encontró el coordinatorId en localStorage');
        alert('Hubo un error al obtener el ID del coordinador');
      }
    } else {
      alert('Por favor, completa los campos requeridos');
    }
  }

  currentStep: number = 1;
  showCalendar: boolean = true;
  missionForm: FormGroup;
  isEditing = false;
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