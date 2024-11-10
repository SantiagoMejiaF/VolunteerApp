import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VolunteerService } from '../model/services/volunteer.service'; // Importamos el servicio

@Component({
  selector: 'app-ver-perfil-v',
  templateUrl: '../view/ver-perfil-v.component.html',
  styleUrls: ['../styles/ver-perfil-v.component.css']
})
export class VerPerfilVComponent implements OnInit {
  currentContent: string = 'content1';
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerData: any; // Información del voluntario seleccionada
  origen: string;
  timelineData: any[] = []; // Historial de actividades del voluntario
  organizationId: number = 0; // Guardamos el organizationId
  showAlert=false;
  showAlert2=false;
  showAlert3=false;
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private volunteerService: VolunteerService // Inyectamos el servicio de voluntarios
  ) {
    this.route.queryParams.subscribe(params => {
      this.origen = params['from']; // Captura el parámetro 'from'
    });
  }

  ngOnInit() {
    // Recuperar la información del voluntario desde localStorage
    const selectedVolunteer = localStorage.getItem('selectedVolunteer');
    this.organizationId = Number(localStorage.getItem('OrgId'));

    if (selectedVolunteer) {
      console.log('Datos del voluntario recuperados de localStorage:', selectedVolunteer);
      this.volunteerData = JSON.parse(selectedVolunteer); // Asignamos los datos del localStorage
      this.firstName = this.volunteerData.firstName;
      this.lastName = this.volunteerData.lastName;
      this.email = this.volunteerData.email;

      // Cargar el historial del voluntario desde el servicio
      this.loadVolunteerTimeline(this.volunteerData.id); // Pasamos el ID del voluntario
    } else {
      console.error('No se encontró la información del voluntario en localStorage');
    }
  }

  // Función para cargar el historial del voluntario desde el servicio
  loadVolunteerTimeline(volunteerId: number) {
    this.volunteerService.getVolunteerHistory(volunteerId).subscribe(
      (response) => {
        this.timelineData = response.map(item => ({
          title: item.activity.title,
          description: item.description,
          rating: item.rating,
          creationDate: item.creationDate
        }));
      },
      (error) => {
        console.error('Error al cargar el historial del voluntario:', error);
      }
    );
  }

  approveVolunteer(approved: boolean) {
    const volunteerId = this.volunteerData.id;

    this.volunteerService.approveVolunteer(volunteerId, this.organizationId, approved).subscribe(
      () => {
        if (approved) {
          this.showAlert=true;
        } else {
          this.showAlert2=true;
        }
        this.volver(); // Regresar después de la acción
      },
      (error) => {
        console.error('Error en la aprobación/rechazo del voluntario:', error);
        this.showAlert3=true;
      }
    );
  }

  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  volver() {
    // Volver a la página anterior según el parámetro 'from'
    this.router.navigate([`/${this.origen}`], { queryParams: { from: 'verPerfilV' } });
  }

  confirmReject() {
    const isConfirmed = window.confirm('¿Está seguro de que quiere rechazar a este voluntario?');
    if (isConfirmed) {
      // Aquí iría la lógica para rechazar al voluntario
      console.log('Voluntario rechazado');
    }
  }

  // Generar las estrellas para la calificación
  getStars(rating: number): string[] {
    const totalStars = 5;
    return Array(totalStars).fill('gray').map((_, index) => index < rating ? 'gold' : 'gray');
  }
}
