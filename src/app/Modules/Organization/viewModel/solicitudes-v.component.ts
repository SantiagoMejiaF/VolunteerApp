import { Component, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrganizationService } from '../model/services/organization.service';

@Component({
  selector: 'app-solicitudes-v',
  templateUrl: '../view/solicitudes-v.component.html',
  styleUrl: '../styles/solicitudes-v.component.css'
})
export class SolicitudesVComponent implements AfterViewInit {
  public data: any[] = []; // Arreglo de voluntarios
  public dataLoaded: boolean = false;

  constructor(
    private router: Router,
    private organizationService: OrganizationService
  ) { }

  // Inicializa la tabla de datos
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableSolicitudesVoluntarios')) {
      $('#datatableSolicitudesVoluntarios').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableSolicitudesVoluntarios').DataTable({
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

  // Cargar los voluntarios pendientes y sus detalles adicionales
  loadPendingVolunteers(): void {
    this.dataLoaded = false; // Datos no están completamente cargados
    const orgId = localStorage.getItem('OrgId'); // Obtén el OrgId del localStorage
    if (orgId) {
      this.organizationService.getPendingVolunteers(+orgId).subscribe(pendingVolunteers => {
        this.data = pendingVolunteers; // Asignamos los voluntarios para renderizarlos con *ngFor
        const volunteerPromises = this.data.map(volunteer => {
          return new Promise<void>((resolve) => {
            this.organizationService.getVolunteerDetails(volunteer.id).subscribe(volunteerDetails => {
              volunteer.personalInformation = volunteerDetails.personalInformation;
              volunteer.emergencyInformation = volunteerDetails.emergencyInformation;

              // Obtener detalles adicionales del usuario, como la imagen
              this.organizationService.getUserDetails(volunteerDetails.userId).subscribe(userDetails => {
                volunteer.image = userDetails.image;
                volunteer.firstName = userDetails.firstName;
                volunteer.lastName = userDetails.lastName;
                volunteer.email = userDetails.email;
                volunteer.cedula = volunteerDetails.personalInformation.identificationCard;

                // Datos de este voluntario están completamente cargados
                resolve();
              });
            });
          });
        });

        // Esperar a que todos los voluntarios estén completamente cargados
        Promise.all(volunteerPromises).then(() => {
          this.dataLoaded = true; // Datos completamente cargados
          this.initializeDataTable(); // Inicializamos la tabla
        });
      });
    }
  }

  // Se ejecuta después de cargar la vista
  ngAfterViewInit(): void {
    this.loadPendingVolunteers(); // Cargar voluntarios después de inicializar la vista
  }

  // Devuelve la clase CSS correspondiente al estado
  getStatusClass(status: string): string {
    switch (status) {
      case 'Activo':
        return 'status-activo';
      case 'PENDIENTE':
        return 'status-pendiente';
      case 'Rechazado':
        return 'status-rechazado';
      default:
        return '';
    }
  }

  // Navega a la vista de detalles del voluntario
  verDetalles(volunteer: any): void {
    if (!this.dataLoaded) {
      return;
    }
    // Pasar el id del voluntario y el parámetro 'from' usando queryParams
    localStorage.setItem('selectedVolunteer', JSON.stringify(volunteer));
    this.router.navigate(['/verPerfilV'], { queryParams: { from: 'verVoluntarios' } });
  }
}
