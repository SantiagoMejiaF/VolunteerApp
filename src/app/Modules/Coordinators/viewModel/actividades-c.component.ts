import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-actividades-c',
  templateUrl: '../view/actividades-c.component.html',
  styleUrl: '../styles/actividades-c.component.css',
})
export class ActividadesCComponent {
  selectedActividad: any = {};
  data = [
    {
      id: 1,
      title: 'Reforestación en el parque central',
      startDate: '2024-10-01',
      address: 'Av. Central 123, Ciudad Verde',
      noVolunteers: 15,
      status: 'Activo'
    },
    {
      id: 2,
      title: 'Limpieza de playas',
      startDate: '2024-11-05',
      address: 'Playa del Sol, Costa Azul',
      noVolunteers: 25,
      status: 'Pendiente'
    },
    {
      id: 3,
      title: 'Recogida de alimentos',
      startDate: '2024-09-15',
      address: 'Calle Esperanza 456, Barrio Centro',
      noVolunteers: 10,
      status: 'Completado'
    },
    {
      id: 4,
      title: 'Campaña de donación de sangre',
      startDate: '2024-12-02',
      address: 'Hospital General, Av. Salud 789',
      noVolunteers: 40,
      status: 'Aplazado'
    },
    {
      id: 5,
      title: 'Construcción de viviendas',
      startDate: '2024-10-20',
      address: 'Comunidad Los Pinos, Sector 7',
      noVolunteers: 30,
      status: 'Pendiente'
    }
  ];

  constructor(private router: Router) {}
  ngAfterViewInit(): void {
    this.initializeDataTable();
  }
  ngOnInit(): void {}

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
          infoEmpty:
            '<span style="font-size: 0.875rem;">No hay registros</span>',
          infoFiltered:
            '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
          lengthMenu:
            '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
          zeroRecords:
            '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
        },
      });
    }, 1);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Activo':
        return 'status-activo';
      case 'Pendiente':
        return 'status-pendiente';
      case 'Completado':
        return 'status-completado';
      case 'Aplazado':
        return 'status-aplazado';
      default:
        return '';
    }
  }
}
