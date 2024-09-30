import { Component } from '@angular/core';

@Component({
  selector: 'app-coordinadores',
  templateUrl: '../view/coordinadores.component.html',
  styleUrl: '../styles/coordinadores.component.css'
})
export class CoordinadoresComponent {
  // Variable para controlar si mostrar botones sociales o campos de cédula y celular
  public showSocialButtons: boolean = true;

 
  public data = [
    {
      firstName: 'Juan',
      lastName: 'Pérez',
      email: 'juan.perez@example.com',
      cedula: '1234567890',
      image: 'assets/img/user1.png'
    },
    {
      firstName: 'Ana',
      lastName: 'García',
      email: 'ana.garcia@example.com',
      cedula: '0987654321',
      image: 'assets/img/ana.png'
    },
    {
      firstName: 'Carlos',
      lastName: 'Martínez',
      email: 'carlos.martinez@example.com',
      cedula: '1122334455',
      image: ''
    }
  ];
  ngAfterViewInit(): void {
    this.initializeDataTable();
  }

   // Método para cambiar a la vista de los campos de cédula y celular
   public onGoogleSignIn(): void {
    this.showSocialButtons = false;
  }

  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableCoordinadores')) {
      $('#datatableCoordinadores').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableCoordinadores').DataTable({
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
}
