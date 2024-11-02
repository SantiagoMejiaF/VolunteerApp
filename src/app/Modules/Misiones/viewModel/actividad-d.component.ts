import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MissionsService } from '../model/services/mission.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-actividad-d',
  templateUrl: '../view/actividad-d.component.html',
  styleUrls: ['../styles/actividad-d.component.css'],
})
export class ActividadDComponent implements OnInit {
  imagen: string = '';
  btnClass: string = '';
  showAlert = false;
  showAlert2 = false;
  showAlert3 = false;
  origen: string = '';
  actividadId: number = 0;
  public mostrarBotonUnirse: boolean = true;
  actividad: any = {}; // Datos de la actividad
  coordinator: any = {}; // Datos del coordinador
  userCoordinator: any = {}; // Datos del usuario relacionado con el coordinador

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private missionsService: MissionsService, // Servicio de misiones
    private organizationService: OrganizationService // Servicio de organizaciones para obtener detalles del usuario
  ) {
    // Obtener el parámetro 'origen' de la ruta
    this.route.queryParams.subscribe((params) => {
      this.origen = params['from'];
    });
  }

  ngOnInit() {
    // Leer los parámetros de la URL
    this.route.params.subscribe((params) => {
      this.actividadId = +params['id']; // Obtener el ID de la actividad
      this.imagen = 'assets/img/' + params['image']; // Cargar la imagen de la actividad
      this.mostrarBotonUnirse = params['fromMisActividades'] !== 'true'; // Mostrar o no el botón de unirse
      this.loadActivityDetails(); // Cargar los detalles de la actividad
    });
  }

  // Método para cargar los detalles de la actividad
  loadActivityDetails(): void {
    this.missionsService.getActivityById(this.actividadId).subscribe(
      (actividad) => {
        this.actividad = actividad; // Guardar los detalles de la actividad obtenida
        this.loadCoordinatorDetails(this.actividad.activityCoordinator); // Cargar los detalles del coordinador
      },
      (error) => {
        console.error('Error al cargar detalles de la actividad:', error); // Manejar errores
      }
    );
  }

  // Método para cargar los detalles del coordinador
  loadCoordinatorDetails(coordinatorId: number): void {
    this.missionsService.getActivityCoordinator(coordinatorId).subscribe(
      (coordinator) => {
        this.coordinator = coordinator; // Guardar los detalles del coordinador
        this.loadUserCoordinatorDetails(this.coordinator.userId); // Obtener los detalles del usuario relacionado
      },
      (error) => {
        console.error('Error al cargar detalles del coordinador:', error); // Manejar errores
      }
    );
  }

  // Método para cargar los detalles del usuario relacionado con el coordinador desde OrganizationService
  loadUserCoordinatorDetails(userId: number): void {
    this.organizationService.getUserDetails(userId).subscribe(
      (userCoordinator) => {
        this.userCoordinator = userCoordinator; // Guardar los detalles del usuario del coordinador
      },
      (error) => {
        console.error(
          'Error al cargar detalles del usuario del coordinador:',
          error
        ); // Manejar errores
      }
    );
  }

  unirse(event: Event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del enlace

    const volunteerId = Number(localStorage.getItem('volunteerId')); // Obtener el volunteerId del localStorage

    if (volunteerId) {
      this.missionsService.joinActivity(volunteerId, this.actividadId).subscribe(
        (response) => {
          // Imprimir la respuesta del servidor
          console.log(response);
          this.showAlert = true; // Mostrar alerta de éxito
          setTimeout(() => (this.showAlert = false), 3000);
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
    } else {
      console.error('No se encontró el ID del voluntario en localStorage');
      this.showAlert2 = true; // Mostrar alerta de error si no hay volunteerId
      setTimeout(() => (this.showAlert2 = false), 3000);
    }
  }



  // Método para volver a la pantalla anterior
  volver() {
    if (this.origen === 'misF' || this.origen === 'homeV') {
      this.router.navigate(['/verPerfilO'], {
        queryParams: { from: this.origen },
      });
    } else {
      this.router.navigate(['/misA']); // Navegar de vuelta a Mis Actividades
    }
  }
}
