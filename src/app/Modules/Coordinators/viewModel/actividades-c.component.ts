import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivityService } from '../model/services/activity.service';
import { Activity } from '../model/activity.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-actividades-c',
  templateUrl: '../view/actividades-c.component.html',
  styleUrl: '../styles/actividades-c.component.css',
})
export class ActividadesCComponent implements OnInit, AfterViewInit {
  selectedActividad: any = {};
  data: Activity[] = [];

  constructor(private router: Router, private activityService: ActivityService) { }

  ngOnInit(): void {
    this.loadActivities();
  }

  ngAfterViewInit(): void {
    this.initializeDataTable();
  }

  // Método para cargar actividades desde el servicio
  // Método para cargar actividades desde el servicio
  loadActivities(): void {
    this.activityService.getActivities().subscribe(
      (response: Activity[]) => {
        this.data = response.map(activity => ({
          id: activity.id,
          title: activity.title,
          startDate: activity.startDate,
          address: activity.address,
          noVolunteers: activity.noVolunteers, // Usando el campo `noVolunteers`
          status: activity.status // Usando el campo `status`
        }));
      },
      error => {
        console.error('Error al cargar las actividades', error);
      }
    );
  }


  // Inicializar DataTable
  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableActividadesC')) {
      $('#datatableActividadesC').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableActividadesC').DataTable({
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
  verDetalles() {
    this.router.navigate(['/verDetallesAxC'], { queryParams: { from: 'misAC' } });

  }
}
