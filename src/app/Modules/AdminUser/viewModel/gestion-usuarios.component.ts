import { Component, OnInit } from '@angular/core';
import { AdminService } from '../model/services/admin.service';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-gestion-usuarios',
  templateUrl: '../view/gestion-usuarios.component.html',
  styleUrls: ['../styles/gestion-usuarios.component.css'],
})
export class GestionUsuariosComponent implements OnInit {
  selectedUser: any = {};
  data: any[] = [];

  constructor(
    private adminService: AdminService,
    private volunteerService: VolunteerService,
    private organizationService: OrganizationService
  ) { }
  ngAfterViewInit(): void {
    this.initializeDataTable();
  }
  ngOnInit(): void {
    // Obtener los usuarios pendientes
    this.adminService.getPendingUsers().subscribe((users) => {
      this.data = users;
      console.log('Usuarios pendientes obtenidos:', this.data);
      this.populateUserRoles();
      setTimeout(() => {
        this.initializeDataTable(); // Inicializar el DataTable después de cargar los datos
      }, 0); // Usar setTimeout para asegurarse de que los datos se hayan cargado completamente
    });
  }

  populateUserRoles(): void {
    this.data.forEach((user) => {
      user.rol = user.role.roleType; // Utilizar directamente el rol desde el objeto usuario

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
      }
    });
  }

  initializeDataTable(): void {
    // Verificar si el DataTable ya está inicializado y destruirlo si es necesario
    if ($.fn.dataTable.isDataTable('#datatableGestionUser')) {
      $('#datatableGestionUser').DataTable().destroy();
    }

    // Inicializar el DataTable con un ligero retraso para asegurarse de que los datos se hayan renderizado
    setTimeout(() => {
      $('#datatableGestionUser').DataTable({
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
    }, 100); // Ajustar el tiempo de retraso si es necesario
  }


  openModal(user: any) {
    this.selectedUser = user; // Cargar los datos del usuario seleccionado en el modal
    console.log('Usuario seleccionado:', this.selectedUser);
  }

  acceptUser(): void {
    if (this.selectedUser && this.selectedUser.id) {
      this.adminService.sendApprovalEmail(this.selectedUser.id, true).subscribe(
        (response) => {
          console.log('Usuario aceptado:', response);
          this.removeUserFromList(this.selectedUser.id);
        },
        (error) => {
          console.error('Error al aceptar el usuario:', error);
        }
      );
    } else {
      console.error('No se ha seleccionado un usuario válido.');
    }
  }

  rejectUser(): void {
    if (this.selectedUser && this.selectedUser.id) {
      this.adminService.sendApprovalEmail(this.selectedUser.id, false).subscribe(
        (response) => {
          console.log('Usuario rechazado:', response);
          this.removeUserFromList(this.selectedUser.id);
        },
        (error) => {
          console.error('Error al rechazar el usuario:', error);
        }
      );
    } else {
      console.error('No se ha seleccionado un usuario válido.');
    }
  }

  removeUserFromList(userId: number): void {
    this.data = this.data.filter(user => user.id !== userId);
  }
}
