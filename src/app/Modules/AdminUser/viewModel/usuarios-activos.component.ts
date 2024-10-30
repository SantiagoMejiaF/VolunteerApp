import { Component, AfterViewInit } from '@angular/core';
import { AdminService } from '../model/services/admin.service';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-usuarios-activos',
  templateUrl: '../view/usuarios-activos.component.html',
  styleUrls: ['../styles/usuarios-activos.component.css']
})
export class UsuariosActivosComponent implements AfterViewInit {
  selectedUser: any = {};
  data: any[] = [];
  dataLoaded: boolean = false;

  constructor(
    private adminService: AdminService,
    private volunteerService: VolunteerService,
    private organizationService: OrganizationService
  ) { }

  ngAfterViewInit(): void {
    this.fetchAndPopulateUsers();
  }

  fetchAndPopulateUsers(): void {
    this.adminService.getAuthorizedUsers().subscribe((users) => {
      this.data = users; // Asignar los datos
      console.log('Usuarios autorizados obtenidos:', this.data);

      this.populateUserRoles(); // Llenar los roles

      setTimeout(() => {
        this.dataLoaded = true;
        this.refreshDataTable(); // Refrescar DataTable después de que los datos estén listos
      }, 500);
    });
  }

  populateUserRoles(): void {
    this.data.forEach((user) => {
      user.rol = user.role.roleType;

      if (user.rol === 'VOLUNTARIO') {
        this.volunteerService.getVolunteerDetails(user.id).subscribe((volunteerDetails) => {
          user.personalInformation = volunteerDetails.personalInformation;
          user.emergencyInformation = volunteerDetails.emergencyInformation;
          user.volunteeringInformation = volunteerDetails.volunteeringInformation;
          user.Cedula = volunteerDetails.personalInformation.identificationCard;
        });
      } else if (user.rol === 'ORGANIZACION') {
        this.organizationService.getOrganizationDetails(user.id).subscribe((organizationDetails) => {
          user.responsiblePersonId = organizationDetails.responsiblePersonId;
          user.responsiblePersonPhoneNumber = organizationDetails.responsiblePersonPhoneNumber;
          user.organizationName = organizationDetails.organizationName;
          user.organizationTypeEnum = organizationDetails.organizationTypeEnum;
          user.sectorTypeEnum = organizationDetails.sectorTypeEnum;
          user.volunteeringTypeEnum = organizationDetails.volunteeringTypeEnum;
          user.address = organizationDetails.address;
          user.nit = organizationDetails.nit;
          user.Cedula = organizationDetails.responsiblePersonId;
        });
      } else if (user.rol === 'COORDINADOR_ACTIVIDAD') {
        // Obtener detalles del coordinador de actividad
        this.adminService.getCoordinatorDetails(user.id).subscribe((coordinatorDetails) => {
          user.Cedula = coordinatorDetails.identificationCard;
          user.phone = coordinatorDetails.phoneActivityCoordinator;

          // Luego obtenemos los detalles de la organización del coordinador
          this.organizationService.getOrganizationDetailsById(coordinatorDetails.organizationId).subscribe((organizationDetails) => {
            user.organizationName = organizationDetails.organizationName;
            user.nit = organizationDetails.nit;
            user.organizationTypeEnum = organizationDetails.organizationTypeEnum;
            user.sectorTypeEnum = organizationDetails.sectorTypeEnum;
            user.volunteeringTypeEnum = organizationDetails.volunteeringTypeEnum;
            user.address = organizationDetails.address;
          });
        });
      }
    });
  }


  refreshDataTable(): void {
    const tableId = '#datatableUsuariosActivos';

    // Destruye el DataTable si ya existe
    if ($.fn.dataTable.isDataTable(tableId)) {
      $(tableId).DataTable().clear().destroy();
    }

    // Vuelve a crear el DataTable después de un pequeño retardo para asegurar que los datos estén listos
    setTimeout(() => {
      $(tableId).DataTable({
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
          zeroRecords: '<span style="font-size: 0.875rem;">No se encontró ningún registro</span>',
        },
      });
    }, 100); // Ajusta el tiempo de retraso si es necesario
  }



  openModal(user: any) {
    this.selectedUser = user; // Cargar los datos del usuario seleccionado en el modal
    console.log('Usuario seleccionado:', this.selectedUser);
  }
}
