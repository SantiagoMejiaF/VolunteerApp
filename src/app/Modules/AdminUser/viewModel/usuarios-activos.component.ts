import { Component } from '@angular/core';

@Component({
  selector: 'app-usuarios-activos',
  templateUrl: '../view/usuarios-activos.component.html',
  styleUrl: '../styles/usuarios-activos.component.css'
})
export class UsuariosActivosComponent {
  selectedUser: any = {};
  data = [
    {
      id: 1,
      image: '',
      firstName: 'Juan',
      lastName: 'Pérez',
      rol: 'VOLUNTARIO',
      email: 'juan.perez@example.com',
      Cedula: '123456789',
      personalInformation: {
        identificationCard: '123456789',
        phoneNumber: '3001234567'
      }
    },
    {
      id: 2,
      image: '',
      firstName: 'Fundación',
      lastName: 'ABC',
      rol: 'ORGANIZACION',
      email: 'fundacion.abc@example.com',
      Cedula: '12098765',
      responsiblePersonPhoneNumber: '1209876543',
      organizationName: 'Fundación ABC',
      nit: '12098765',
      organizationTypeEnum: 'ONG',
      sectorTypeEnum: 'Educación',
      volunteeringTypeEnum: 'Presencial',
      address: 'Calle 123 #45-67'
    }
  ];
  ngAfterViewInit(): void {
    this.initializeDataTable();
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
}
