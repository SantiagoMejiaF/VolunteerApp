import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivityService } from '../model/services/activity.service';
import { Activity } from '../model/activity.model';
import { Router } from '@angular/router';
import { MissionsService } from '../../Misiones/model/services/mission.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-actividades-c',
  templateUrl: '../view/actividades-c.component.html',
  styleUrls: ['../styles/actividades-c.component.css'],
})
export class ActividadesCComponent implements OnInit, AfterViewInit {
  selectedActividad: any = {};
  data: any[] = [];
  coordinatorId: number | null = null;
  loadingComplete: boolean = false; // Estado para indicar si la carga ha finalizado

  constructor(private router: Router, private activityService: ActivityService, private missionsService: MissionsService, private organizationService: OrganizationService) { }

  ngOnInit(): void {
    this.coordinatorId = Number(localStorage.getItem('coordinatorId'));
    console.log('Coordinator ID:', this.coordinatorId); // Verifica que el ID sea correcto
    if (this.coordinatorId) {
      this.loadActivities();
    } else {
      console.error('Coordinador ID no encontrado en localStorage');
    }
  }

  ngAfterViewInit(): void {
  }

  loadActivities(): void {
    if (this.coordinatorId) {
      this.activityService.getActivitiesByCoordinator(this.coordinatorId).subscribe(
        (response: any[]) => {
          console.log('Response from API:', response);
          if (response && response.length > 0) {
            this.data = response.map(activity => ({
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
              coordinatorId: activity.activityCoordinator // Guardar el ID del coordinador
            }));
            let completedRequests = 0;
            // Obtener los detalles del coordinador para cada actividad
            this.data.forEach(activity => {
              this.missionsService.getActivityCoordinator(activity.coordinatorId).subscribe(coordinatorDetails => {
                activity.coordinator = {
                  id: coordinatorDetails.identificationCard,
                  phone: coordinatorDetails.phoneActivityCoordinator,
                  userId: coordinatorDetails.userId
                };

                // Obtener el nombre del usuario
                this.organizationService.getUserDetails(coordinatorDetails.userId).subscribe(userDetails => {
                  activity.coordinator.name = userDetails.firstName + ' ' + userDetails.lastName; // Asumiendo que el campo se llama 'name'

                  completedRequests++;
                  if (completedRequests === this.data.length) {
                    this.loadingComplete = true; // Marca la carga como completa cuando todas las solicitudes hayan terminado
                    this.initializeDataTable();
                  }
                });
              }, error => {
                console.error('Error fetching coordinator details', error);
              });
            });

            console.log('Mapped data:', this.data);
            this.initializeDataTable(); // Inicializa la tabla después de cargar los datos
          } else {
            console.log('No activities found for this coordinator.');
          }
        },
        error => {
          console.error('Error al cargar las actividades del coordinador', error);
        }
      );
    }
  }


  // Inicializar DataTable
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableActividadesC')) {
      $('#datatableActividadesC').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableActividadesC').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        scrollX: true,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrar página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty: '<span style="font-size: 0.875rem;">No hay registros</span>',
          infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
          lengthMenu: '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
          zeroRecords: '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
        },
      });
    }, 1);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'DISPONIBLE':
        return 'status-activo';
      case 'COMPLETADA':
        return 'status-completado';
      case 'CANCELADA':
        return 'status-aplazado';
      default:
        return '';
    }
  }

  verDetalles(activity: any): void {
    if (!this.loadingComplete) {
      alert('Los datos aún están cargando. Por favor, espera hasta que se completen.');
      return;
    } else

      // Guardar la información de la actividad seleccionada en localStorage
      localStorage.setItem('selectedActivity', JSON.stringify(activity));

    // Redirigir a la vista de detalles
    this.router.navigate(['/verDetallesAxC'], { queryParams: { from: 'misAC' } });
  }

}
