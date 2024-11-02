import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';
import { Router } from '@angular/router';
import { ActivityService } from '../model/services/activity.service';

@Component({
  selector: 'app-ver-perfil-c',
  templateUrl: '../view/ver-perfil-c.component.html',
  styleUrl: '../styles/ver-perfil-c.component.css'
})
export class VerPerfilCComponent {
  currentContent: string = 'content1';
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerId: number = 0;
  phoneNumber: string = '';
  identificationCard: string = '';
  image: string = '';
  activities: any[] = [];
  timelineData: any[] = [];
  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  myForm: FormGroup;
  volunteerData: any;
  disabled = false;
  ShowFilter = false;
  limitSelection = false;


  constructor(
    private activityService: ActivityService,
    private router: Router

  ) {
  }

  loadCoordinatorData(): void {
    const coordinator = JSON.parse(localStorage.getItem('SelectedCoordinator')!);
    if (coordinator) {
      this.firstName = coordinator.firstName;
      this.lastName = coordinator.lastName;
      this.email = coordinator.email;
      this.phoneNumber = coordinator.phoneActivityCoordinator;
      this.identificationCard = coordinator.identificationCard;
      this.image = coordinator.image || 'assets/img/ejemplo.jpg';
    } else {
      console.error('No se encontró el coordinador seleccionado en localStorage.');
    }
  }

  loadCoordinatorActivities(): void {
    const coordinator = JSON.parse(localStorage.getItem('SelectedCoordinator')!);
    if (coordinator && coordinator.id) {
      this.activityService.getActivitiesByCoordinator(coordinator.id).subscribe(
        (activities) => {
          this.activities = activities; // Almacena cada actividad completa
        },
        (error) => {
          console.error('Error al cargar las actividades del coordinador:', error);
        }
      );
    }
  }


  loadCoordinatorHistory(): void {
    const coordinator = JSON.parse(localStorage.getItem('SelectedCoordinator')!);
    if (coordinator && coordinator.userId) {
      this.activityService.getCoordinatorReviewHistory(coordinator.userId).subscribe(
        (history) => {
          this.timelineData = history.map(item => ({
            title: item.activity.title,
            rating: item.rating,
            description: item.description,
            creationDate: item.creationDate
          }));
        },
        (error) => {
          console.error('Error al cargar el historial del coordinador:', error);
        }
      );
    }
  }



  ngOnInit() {
    this.loadCoordinatorData();
    this.loadCoordinatorActivities();
    this.loadCoordinatorHistory();
  }


  getStars(rating: number): string[] {
    const totalStars = 5;
    return Array(totalStars).fill('gray').map((_, index) => index < rating ? 'gold' : 'gray');
  }

  verDetalles(index: number | undefined) {
    // Asignar 1 por defecto si el index es undefined o null
    const validIndex = index ?? 1;

    // Asegurarse de que la imagenId esté en el rango adecuado (1-3)
    const imagenId = (validIndex % 3) + 1;
    const btnClass = 'btn-outline-primary' + imagenId;

    // Navegar a la ruta con los parámetros calculados
    this.router.navigate(['/actividad', validIndex, `card${imagenId}.jpg`, btnClass]);
  }
  getStatusClass(status: string): string {
    switch (status) {
      case 'COMPLETADA':
        return 'status-completada';
      case 'DISPONIBLE':
        return 'status-activo';
      case 'PENDIENTE':
        return 'status-pendiente';
      case 'CANCELADA':
        return 'status-cancelada';
      default:
        return '';
    }
  }

  verDetallesA(activity: any): void {
    // Crea el objeto en el formato adecuado, similar al de ActividadesCComponent
    const formattedActivity = {
      id: activity.id,
      title: activity.title,
      startDate: activity.date,
      address: activity.address,
      noVolunteers: activity.numberOfVolunteersRequired,
      status: activity.activityStatus,
      startTime: activity.startTime,
      description: activity.description,
      city: activity.city,
      requiredHours: activity.requiredHours,
      coordinatorId: activity.activityCoordinator,
      // Incluye el detalle del coordinador si está disponible
      coordinator: {
        id: activity.personalDataCommunityLeaderEntity?.id || null,
        phone: activity.personalDataCommunityLeaderEntity?.phoneCommunityLeader || '',
        userId: activity.personalDataCommunityLeaderEntity?.id || null,
        name: `${activity.personalDataCommunityLeaderEntity?.nameCommunityLeader || 'N/A'}`
      }
    };

    // Imprime el objeto para verificar
    console.log('Actividad seleccionada (formateada):', formattedActivity);

    // Guarda en localStorage
    localStorage.setItem('selectedActivity', JSON.stringify(formattedActivity));

    // Navega a la página de detalles
    this.router.navigate(['/verDetallesAxC'], { queryParams: { from: 'verPerfilC' } });
  }


}


