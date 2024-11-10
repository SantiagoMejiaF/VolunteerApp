import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';

@Component({
  selector: 'app-ver-perfil-o',
  templateUrl: '../view/ver-perfil-o.component.html',
  styleUrls: ['../styles/ver-perfil-o.component.css'],
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
  showAlert = false;
  showAlert2 = false;
  showAlert3 = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private organizationService: OrganizationService,
    private volunteerService: VolunteerService // Agregar el servicio de voluntarios
  ) { }

  ngOnInit(): void {
    // Obtener la organización desde localStorage
    const savedOrganization = localStorage.getItem('SelectedOrganization');
    console.log('Datos de la organización guardados:', savedOrganization);

    this.route.queryParams.subscribe((params) => {
      this.fromPage = params['from'] || 'misF'; // Establece 'misF' como valor por defecto
    });

    if (savedOrganization) {
      this.organizationData = JSON.parse(savedOrganization); // Parsear el JSON para obtener el objeto

      // Extraer el id de la organización
      const organizationId = this.organizationData.id;

      if (organizationId) {
        // Obtener los detalles de la organización usando el id
        this.organizationService.getOrganizationDetailsById(organizationId).subscribe(
          (organizationData) => {
            this.organizationData = organizationData; // Actualizar con los datos obtenidos

            const userId = organizationData.userId; // Obtener el userId
            console.log('User ID:', userId); // Para depuración

            // Obtener detalles del usuario
            this.organizationService.getUserDetails(userId).subscribe(
              (userData) => {
                this.email = userData.email;
                this.firstName = userData.firstName;
                this.lastName = userData.lastName;
                this.imageUrl = userData.image || 'assets/img/default-user.png'; // Imagen por defecto
              },
              (error) => {
                console.error('Error al cargar los detalles del usuario', error);
              }
            );

            // Obtener el historial (timeline) de la organización
            this.organizationService.getOrganizationHistory(organizationId).subscribe(
              (historyData) => {
                this.timelineData = historyData.map((item, index) => ({
                  id: index + 1,
                  title: item.activity.title,
                  description: item.description || 'No hay descripción disponible.',
                  rating: item.rating,
                  creationDate: item.creationDate,
                }));
              },
              (error) => {
                console.error('Error al cargar el historial de la organización', error);
              }
            );

            // Obtener las actividades de la organización
            this.organizationService.getOrganizationActivities(organizationId).subscribe(
              (activitiesData) => {
                // Filtrar las actividades para excluir las completadas
                console.log('Todas las actividades:', activitiesData);
                this.actividades = activitiesData
                  .filter(activity => {
                    if (this.fromPage === 'homeV') {
                      // En homeV, excluir completadas y canceladas, y solo mostrar públicas
                      return activity.activityStatus !== 'COMPLETADA' &&
                        activity.activityStatus !== 'CANCELADA' &&
                        activity.visibility === 'PUBLICA';
                    } else {
                      // En misF, mostrar tanto privadas como públicas, excluyendo solo completadas y canceladas
                      return activity.activityStatus !== 'COMPLETADA' &&
                        activity.activityStatus !== 'CANCELADA';
                    }
                  }) // Filtrar por estado
                  .map((activity) => ({
                    id: activity.id,
                    nombre: activity.title,
                    descripcion: activity.description,
                    fecha: activity.date,
                    cuposRestantes: activity.numberOfVolunteersRequired,
                    cuposTotales: activity.numberOfVolunteersRequired,
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
      } else {
        console.error('No se encontró el id de la organización en los datos guardados');
      }
    } else {
      console.error('No se encontró información de la organización en localStorage');
      // Opcionalmente, puedes redirigir al usuario a otra página o mostrar un mensaje
    }
  }


  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  getStars(rating: number): string[] {
    const totalStars = 5;
    return Array(totalStars)
      .fill('gray')
      .map((_, index) => (index < rating ? 'gold' : 'gray'));
  }

  goBack() {
    this.router.navigate([`/${this.fromPage}`]);
  }

  // Método para enviar la solicitud para ser parte de la organización
  unirseF(event: Event) {
    event.preventDefault();
    const volunteerId = Number(localStorage.getItem('volunteerId'));
    // Enviar la solicitud de unión
    this.volunteerService
      .joinOrganization(volunteerId, this.organizationData.id)
      .subscribe(
        (response) => {
          this.showAlert = true;
          setTimeout(() => (this.showAlert = false), 3000);
          console.log('Solicitud enviada con éxito', response);
        },
        (error) => {
          if (error.status === 400) {
            this.showAlert3 = true; // Mostrar alerta de éxito
            setTimeout(() => (this.showAlert3 = false), 3000);
          } else {
            this.showAlert2 = true; // Mostrar alerta de error
            setTimeout(() => (this.showAlert2 = false), 3000);
          }
        }
      );
  }

  verDetalles(actividadId: number) {
    const actividad = this.actividades.find(act => act.id === actividadId);
    console.log('Detalles de la actividad seleccionada:', actividad);

    if (actividad) {
      // Guardar la actividad seleccionada en localStorage
      localStorage.setItem('SelectedActivity1', JSON.stringify(actividad));
      console.log('Actividad guardada en localStorage:', JSON.parse(localStorage.getItem('SelectedActivity1') || 'null'));

      const imagenId = (actividadId % 6) + 1;
      const from = this.fromPage === 'homeV' ? 'homeV' : 'misF';

      this.router.navigate(
        [
          '/actividad',
          actividadId,
          `card${imagenId}.svg`,
          { fromMisActividades: true },
        ],
        { queryParams: { from: from } }
      );
    } else {
      console.error('Actividad no encontrada con ID:', actividadId);
    }
  }


}
