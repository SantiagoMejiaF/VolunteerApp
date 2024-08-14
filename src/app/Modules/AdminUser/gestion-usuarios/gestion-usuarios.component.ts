import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin.service';
import { OauthService } from '../../../services/oauth.service';

@Component({
  selector: 'app-gestion-usuarios',
  templateUrl: './gestion-usuarios.component.html',
  styleUrls: ['./gestion-usuarios.component.css'],
})
export class GestionUsuariosComponent implements OnInit {
  selectedUser: any = {};
  data: any[] = [];

  constructor(private adminService: AdminService, private oauthService: OauthService) { }

  ngOnInit(): void {
    // Obtener los usuarios pendientes
    this.adminService.getPendingUsers().subscribe((users) => {
      this.data = users;
      this.populateUserRoles();
      this.initializeDataTable();
    });
  }

  // Obtener los roles de los usuarios
  populateUserRoles(): void {
    this.data.forEach((user) => {
      this.oauthService.getUserRole(user.roleId).subscribe((role) => {
        user.rol = role.roleType;
      });
    });
  }

  // Inicializar DataTable
  initializeDataTable(): void {
    setTimeout(() => {
      $('#datatableexample').DataTable({
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

  openModal(user: any) {
    this.selectedUser = user; // Cargar los datos del usuario seleccionado en el modal
  }

  acceptUser(): void {
    this.adminService.sendApprovalEmail(this.selectedUser.id, true).subscribe(
      (response) => {
        console.log('Usuario aceptado:', response);
        this.removeUserFromList(this.selectedUser.id);
      },
      (error) => {
        console.error('Error al aceptar el usuario:', error);
      }
    );
  }

  rejectUser(): void {
    this.adminService.sendApprovalEmail(this.selectedUser.id, false).subscribe(
      (response) => {
        console.log('Usuario rechazado:', response);
        this.removeUserFromList(this.selectedUser.id);
      },
      (error) => {
        console.error('Error al rechazar el usuario:', error);
      }
    );
  }

  removeUserFromList(userId: number): void {
    this.data = this.data.filter(user => user.id !== userId);
  }
}
