import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerService } from '../model/services/volunteer.service';

@Component({
  selector: 'app-mis-actividades',
  templateUrl: '../view/mis-actividades.component.html',
  styleUrls: ['../styles/mis-actividades.component.css'],
})
export class MisActividadesComponent implements OnInit {
  public activities: any[] = []; // Variable para almacenar las actividades obtenidas
  volunteerId!: number; // Variable para almacenar el ID del voluntario

  constructor(
    private router: Router,
    private volunteerService: VolunteerService // Inyectar el servicio de voluntarios
  ) {}

  ngOnInit(): void {
    this.volunteerId = Number(localStorage.getItem('volunteerId')); // Obtener el volunteerId del localStorage
    if (this.volunteerId) {
      this.loadActivities(this.volunteerId); // Cargar actividades del voluntario
    }
  }

  // Método para cargar actividades del voluntario
  loadActivities(volunteerId: number): void {
    this.volunteerService.getActivitiesByVolunteerId(volunteerId).subscribe(
      (activities) => {
        this.activities = activities; // Guardar las actividades obtenidas
        setTimeout(() => {
          this.initializeDataTable(); // Inicializar DataTable después de que las actividades se carguen
        }, 100); // Un pequeño retraso para asegurar que la tabla esté renderizada
      },
      (error) => {
        console.error('Error al obtener actividades:', error); // Manejar error
      }
    );
  }

  getStatusClass(status: string): string {
    // Método para obtener la clase CSS según el estado de la actividad
    switch (status) {
      case 'DISPONIBLE':
        return 'status-activo';
      case 'PENDIENTE':
        return 'status-pendiente';
      case 'COMPLETADA':
        return 'status-completado';
      case 'CANCELADA':
        return 'status-aplazado';
      default:
        return '';
    }
  }

  initializeDataTable(): void {
    // Método para inicializar DataTable
    if ($.fn.dataTable.isDataTable('#datatableActividadesO')) {
      $('#datatableActividadesO').DataTable().destroy();
    }
    const table = $('#datatableActividadesO').DataTable({
      pagingType: 'full_numbers',
      pageLength: 5,
      processing: true,
      lengthMenu: [5, 10, 25],
      scrollX: true,
      autoWidth: true, 
      language: {
        info: '<span style="font-size: 0.875rem;">Mostrar página _PAGE_ de _PAGES_</span>',
        search: '<span style="font-size: 0.875rem;">Buscar</span>',
        infoEmpty: '<span style="font-size: 0.875rem;">No hay registros</span>',
        infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
        lengthMenu: '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
        zeroRecords: '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
      },
    });
  
  
    table.columns.adjust();
  }
  

  verDetalles(id: number): void {
    // Método para ver los detalles de una actividad
    const imagenId = (id % 6) + 1;
    this.router.navigate(
      ['/actividad', id, `card${imagenId}.svg`, { fromMisActividades: true }],
      { queryParams: { from: 'misA' } }
    );
  }
}
