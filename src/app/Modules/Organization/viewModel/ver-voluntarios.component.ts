import { Component, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-ver-voluntarios',
  templateUrl: '../view/ver-voluntarios.component.html',
  styleUrls: ['../styles/ver-voluntarios.component.css']
})
export class VerVoluntariosComponent implements AfterViewInit {
  public data: any[] = [
    {
      firstName: 'Juan',
      lastName: 'Pérez',
      email: 'juan.perez@example.com',
      cedula: '12345678',
      image: 'assets/img/user1.png',
      Status: 'Activo'
    },
    {
      firstName: 'María',
      lastName: 'Gómez',
      email: 'maria.gomez@example.com',
      cedula: '87654321',
      image: 'assets/img/user2.png',
      Status: 'Pendiente'
    },
    {
      firstName: 'Carlos',
      lastName: 'Rodríguez',
      email: 'carlos.rodriguez@example.com',
      cedula: '12349876',
      image: 'assets/img/user3.png',
      Status: 'Activo'
    },
    {
      firstName: 'Lucía',
      lastName: 'Martínez',
      email: 'lucia.martinez@example.com',
      cedula: '98761234',
      image: '',
      Status: 'Rechazado'
    },
    {
      firstName: 'Pedro',
      lastName: 'Sánchez',
      email: 'pedro.sanchez@example.com',
      cedula: '13579246',
      image: 'assets/img/user5.png',
      Status: 'Activo'
    }
  ];

  ngAfterViewInit(): void {
    this.initializeDataTable();
  }

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
        }
      });
    }, 1);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Activo':
        return 'status-activo';
      case 'Pendiente':
        return 'status-pendiente';
      case 'Rechazado':
        return 'status-rechazado';
      default:
        return '';
    }
  }
}
