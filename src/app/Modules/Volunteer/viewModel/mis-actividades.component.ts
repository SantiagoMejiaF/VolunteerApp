import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mis-actividades',
  templateUrl: '../view/mis-actividades.component.html',
  styleUrl: '../styles/mis-actividades.component.css'
})
export class MisActividadesComponent {
  public data = [
    {
      id: 1,
      title: 'Recogida de Basura',
      startDate: '2024-10-25',
      address: 'Calle 123, Ciudad Verde',
      noVolunteers: 10,
      status: 'DISPONIBLE',
    },
    {
      id: 2,
      title: 'Donación de Ropa',
      startDate: '2024-11-01',
      address: 'Av. Libertador, Plaza Mayor',
      noVolunteers: 5,
      status: 'COMPLETADO',
    },
    {
      id: 3,
      title: 'Convivencia en el Parque',
      startDate: '2024-11-10',
      address: 'Parque Central, Sector 4',
      noVolunteers: 20,
      status: 'APLAZADO',
    },
    {
      id: 4,
      title: 'Taller de Reciclaje',
      startDate: '2024-11-15',
      address: 'Centro Comunitario, Calle 45',
      noVolunteers: 8,
      status: 'PENDIENTE',
    },
    {
      id: 5,
      title: 'Plantación de Árboles',
      startDate: '2024-11-20',
      address: 'Calle Ecológica, Sector 3',
      noVolunteers: 15,
      status: 'COMPLETADO',
    },
    {
      id: 6,
      title: 'Campaña de Vacunación',
      startDate: '2024-11-25',
      address: 'Hospital Local, Av. San Martín',
      noVolunteers: 12,
      status: 'COMPLETADO',
    },
  ];
  constructor(private router: Router ){}
  getStatusClass(status: string): string {
    switch (status) {
      case 'DISPONIBLE':
        return 'status-activo';
      case 'PENDIENTE':
        return 'status-pendiente';
      case 'COMPLETADO':
        return 'status-completado';
      case 'APLAZADO':
        return 'status-aplazado';
      default:
        return '';
    }
  }
  ngAfterViewInit(): void {
    this.initializeDataTable();
  }
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableActividadesO')) {
      $('#datatableActividadesO').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableActividadesO').DataTable({
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

  verDetalles(index: number | undefined) {
    const validIndex = index ?? 1;
    const imagenId = (validIndex % 6) + 1;
    
    // Navegar a la ruta con el parámetro que indica el origen
    this.router.navigate(['/actividad', validIndex, `card${imagenId}.svg`, { fromMisActividades: true }], { queryParams: { from: 'misA' } });
  }

   
}
