import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';

@Component({
  selector: 'app-ver-perfil-o',
  templateUrl: '../view/ver-perfil-o.component.html',
  styleUrls: ['../styles/ver-perfil-o.component.css']
})
export class VerPerfilOComponent implements OnInit {
  organizationData: any = {};
  email: string = '';
  firstName: string = '';
  lastName: string = '';
  imageUrl: string = '';
  currentContent: string = 'content1';
  fromPage: string = 'misF';
  timelineData: any[] = [];
  actividades: any[] = [];
  volunteerId: number = 0; // ID del voluntario que se obtiene desde localStorage

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private organizationService: OrganizationService,
    private volunteerService: VolunteerService // Agregar el servicio de voluntarios
  ) { }

  ngOnInit(): void {
    const organizationId = this.route.snapshot.queryParams['id'];
    this.fromPage = this.route.snapshot.queryParams['from'] || 'misF'; // Capturar correctamente `from` o establecer `misF`
    console.log('Organization ID recibido:', organizationId);
    console.log('Página de origen:', this.fromPage);

    // Obtener el volunteerId desde localStorage
    const volunteerId = Number(localStorage.getItem('volunteerId'));
    this.volunteerId = volunteerId;

    if (organizationId) {
      // Obtener los detalles de la organización
      this.organizationService.getOrganizationDetailsById(organizationId).subscribe(
        (organizationData) => {
          this.organizationData = organizationData;

          const userId = organizationData.userId;
          this.organizationService.getUserDetails(userId).subscribe(
            (userData) => {
              this.email = userData.email;
              this.firstName = userData.firstName;
              this.lastName = userData.lastName;
              this.imageUrl = userData.image;
            },
            (error) => {
              console.error('Error al cargar los detalles del usuario', error);
            }
          );

          // Obtener el historial (timeline) de la organización
          this.organizationService.getOrganizationHistory(organizationId).subscribe(
            (historyData) => {
              this.timelineData = historyData.map((item, index) => ({
                id: index + 1, // Contador incremental
                title: item.activity.title,
                description: item.description || 'No hay descripción disponible.',
                rating: item.rating,
                creationDate: item.creationDate
              }));
            },
            (error) => {
              console.error('Error al cargar el historial de la organización', error);
            }
          );

          // Obtener las actividades de la organización
          this.organizationService.getOrganizationActivities(organizationId).subscribe(
            (activitiesData) => {
              this.actividades = activitiesData.map(activity => ({
                nombre: activity.title,
                descripcion: activity.description,
                fecha: activity.date,
                cuposRestantes: activity.numberOfVolunteersRequired,
                cuposTotales: activity.numberOfVolunteersRequired
              }));
              console.log('Datos de actividades:', this.actividades);
            },
            (error) => {
              console.error('Error al cargar las actividades de la organización', error);
            }
          );
        },
        (error) => {
          console.error('Error al cargar los detalles de la organización', error);
        }
      );
    }
  }



  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  getStars(rating: number): string[] {
    const totalStars = 5;
    return Array(totalStars).fill('gray').map((_, index) => index < rating ? 'gold' : 'gray');
  }

  goBack() {
    this.router.navigate([`/${this.fromPage}`]);
  }

  // Método para enviar la solicitud para ser parte de la organización
  unirseF(event: Event) {
    event.preventDefault();

    // Enviar la solicitud de unión
    this.volunteerService.joinOrganization(this.volunteerId, this.organizationData.id).subscribe(
      (response) => {
        alert('Se ha enviado tu solicitud de forma exitosa');
        console.log('Solicitud enviada con éxito', response);
      },
      (error) => {
        console.error('Error al enviar la solicitud', error);
        alert('Ocurrió un error al enviar la solicitud.');
      }
    );
  }

  verDetalles(id: number) {
    const actividad = this.actividades[id];
    console.log('Detalles de la actividad:', actividad);
    const imagenId = (id % 6) + 1;
    const from = this.fromPage === 'homeV' ? 'homeV' : 'misF';

    this.router.navigate(
      ['/actividad', id, `card${imagenId}.svg`, { fromMisActividades: true }], 
      { queryParams: { from: from } }
    );
  
  }
}
