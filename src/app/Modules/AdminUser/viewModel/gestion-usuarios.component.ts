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

  ngOnInit(): void {
    // Obtener los usuarios pendientes
    this.adminService.getPendingUsers().subscribe((users) => {
      this.data = users;
      console.log('Usuarios pendientes obtenidos:', this.data);
      this.populateUserRoles(); // Asignar los roles adicionales (voluntario/organización)
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
      }
    });
  }

  openModal(user: any) {
    this.selectedUser = user; // Cargar los datos del usuario seleccionado en el modal
    console.log('Usuario seleccionado:', this.selectedUser);
  }

  acceptUser(): void {
    if (this.selectedUser && this.selectedUser.id) {
      // Llamada al servicio para aceptar al usuario
      this.adminService.sendApprovalEmail(this.selectedUser.id, true).subscribe(
        (response) => {
          console.log('Usuario aceptado:', response);
          this.removeUserFromList(this.selectedUser.id); // Eliminar el usuario aceptado de la lista
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
      // Llamada al servicio para rechazar al usuario
      this.adminService.sendApprovalEmail(this.selectedUser.id, false).subscribe(
        (response) => {
          console.log('Usuario rechazado:', response);
          this.removeUserFromList(this.selectedUser.id); // Eliminar el usuario rechazado de la lista
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
    // Filtrar el usuario eliminado de la lista
    this.data = this.data.filter(user => user.id !== userId);
  }
}
