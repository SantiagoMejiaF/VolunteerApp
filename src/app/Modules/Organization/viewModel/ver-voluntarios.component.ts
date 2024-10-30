import { Component, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service'; // Importar el servicio necesario

@Component({
  selector: 'app-ver-voluntarios',
  templateUrl: '../view/ver-voluntarios.component.html',
  styleUrls: ['../styles/ver-voluntarios.component.css']
})
export class VerVoluntariosComponent implements AfterViewInit {
  public data: any[] = []; // Inicializamos el arreglo de voluntarios
  public dataLoaded: boolean = false;

  constructor(
    private router: Router,
    private organizationService: OrganizationService // Inyectamos el servicio de organización
  ) { }

  ngAfterViewInit(): void {
    this.loadActiveVolunteers(); // Llamamos a la función para cargar voluntarios
  }

  // Cargar los voluntarios activos (aceptados)
  loadActiveVolunteers(): void {
    this.dataLoaded = false; // Desactivar la acción de ver detalles mientras se cargan los datos
    const orgId = localStorage.getItem('OrgId'); // Obtener OrgId del localStorage
    if (orgId) {
      this.organizationService.getAcceptedVolunteers(+orgId).subscribe((volunteers) => {
        this.data = volunteers; // Asignamos los voluntarios a la variable 'data' para el *ngFor

        // Por cada voluntario, obtenemos detalles adicionales
        const volunteerPromises = this.data.map(volunteer => {
          return new Promise<void>((resolve) => {
            this.organizationService.getVolunteerDetails(volunteer.id).subscribe(volunteerDetails => {
              volunteer.personalInformation = volunteerDetails.personalInformation;
              volunteer.emergencyInformation = volunteerDetails.emergencyInformation;

              // Obtener la imagen del voluntario usando el userId
              this.organizationService.getUserDetails(volunteerDetails.userId).subscribe(userDetails => {
                volunteer.image = userDetails.image;
                volunteer.firstName = userDetails.firstName;
                volunteer.lastName = userDetails.lastName;
                volunteer.email = userDetails.email;
                volunteer.cedula = volunteerDetails.personalInformation.identificationCard;
                volunteer.Status = volunteer.status;

                // Una vez que los detalles del voluntario estén listos, resolvemos la promesa
                resolve();
              });
            });
          });
        });

        // Esperar a que todos los voluntarios se hayan cargado antes de habilitar el botón
        Promise.all(volunteerPromises).then(() => {
          this.dataLoaded = true; // Todos los datos están completamente cargados
          this.initializeDataTable(); // Inicializar la tabla de datos
        });
      });
    }
  }

  // Inicializa la tabla de datos
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableVerVoluntarios')) {
      $('#datatableVerVoluntarios').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableVerVoluntarios').DataTable({
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
        }
      });
    }, 1);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACEPTADO':
        return 'status-activo';
      case 'Pendiente':
        return 'status-pendiente';
      case 'Rechazado':
        return 'status-rechazado';
      default:
        return '';
    }
  }

  verDetalles(volunteer: any): void {
    if (!this.dataLoaded) {
      return;
    }
    localStorage.setItem('selectedVolunteer', JSON.stringify(volunteer));
    this.router.navigate(['/verPerfilV'], { queryParams: { from: 'verVoluntarios' } });
  }
}
