import { Component, AfterViewInit } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';

@Component({
  selector: 'app-solicitudes-v',
  templateUrl: '../view/solicitudes-v.component.html',
  styleUrl: '../styles/solicitudes-v.component.css'
})
export class SolicitudesVComponent {
  public data: any[] = []
  constructor(private router: Router,){

  }
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableSolicitudesVoluntarios')) {
      $('#datatableSolicitudesVoluntarios').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableSolicitudesVoluntarios').DataTable({
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
    ngAfterViewInit(): void {
      this.initializeDataTable();
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
  verDetalles(){
    this.router.navigate(['/verPerfilV'], { queryParams: { from: 'verVoluntarios' } });
  }
}
