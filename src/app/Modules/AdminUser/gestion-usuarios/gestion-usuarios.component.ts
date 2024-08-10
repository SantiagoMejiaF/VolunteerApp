import { Component } from '@angular/core';

@Component({
  selector: 'app-gestion-usuarios',
  templateUrl: './gestion-usuarios.component.html',
  styleUrls: ['./gestion-usuarios.component.css'],
})
export class GestionUsuariosComponent {
  selectedUser: any = {}; 
  data = [
    { id: 1, name: 'Juan Pérez', rol: 'voluntario', email: 'juan@example.com', Cedula: '123456789' },
    { id: 2, name: 'María García', rol: 'organización', email: 'maria@example.com', Cedula: '987654321' },
    { id: 3, name: 'Carlos Ruiz', rol: 'voluntario', email: 'carlos@example.com', Cedula: '456789123' },
    { id: 4, name: 'Ana López', rol: 'organización', email: 'ana@example.com', Cedula: '654321987' }
  ];

  constructor() {
    setTimeout(() => {
      $('#datatableexample').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        //dom: 'ftip',
        scrollX:true,
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
    this.selectedUser = user; // coloca los datos del user que se selecciona aquí Martin
  }
}
