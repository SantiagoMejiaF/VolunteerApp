import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';

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
  actividades: any[] = []; // Para almacenar las actividades de la organización

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private organizationService: OrganizationService
  ) { }

  ngOnInit(): void {
    const organizationId = this.route.snapshot.queryParams['id'];
    console.log('Organization ID recibido:', organizationId);

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
              this.timelineData = historyData.map(item => ({
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

  unirseF(event: Event) {
    event.preventDefault();
    alert('Se ha enviado tu solicitud de forma exitosa');
  }

  verDetalles(index: number) {
    const actividad = this.actividades[index];
    console.log('Detalles de la actividad:', actividad);
    // Aquí puedes navegar a una nueva página de detalles o mostrar más información
    // Ejemplo: this.router.navigate(['/actividad-detalle', actividad.id]);
  }

}
