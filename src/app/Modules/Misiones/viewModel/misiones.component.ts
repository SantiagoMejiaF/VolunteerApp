import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MissionsService } from '../model/services/mission.service';
import { Mission } from '../model/mission.model';

@Component({
  selector: 'app-misiones',
  templateUrl: '../view/misiones.component.html',
  styleUrls: ['../styles/misione.component.css']
})
export class MisionesComponent implements OnInit {
  selectedMission: any = {};
  data = [
    {
      id: 1,
      title: 'Reforestación en la Amazonía',
      startDate: '2024-10-01',
      endDate: '2024-10-15',
      department: 'Medio Ambiente',
      visibility: 'Pública',
      status: 'Activo'
    },
    {
      id: 2,
      title: 'Campaña de educación rural',
      startDate: '2024-11-05',
      endDate: '2024-11-20',
      department: 'Educación',
      visibility: 'Pública',
      status: 'Completado'
    },
    {
      id: 3,
      title: 'Banco de alimentos comunitario',
      startDate: '2024-09-20',
      endDate: '2024-09-30',
      department: 'Social',
      visibility: 'Privada',
      status: 'Completado'
    },
    {
      id: 4,
      title: 'Limpieza de playas en la costa',
      startDate: '2024-12-01',
      endDate: '2024-12-10',
      department: 'Medio Ambiente',
      visibility: 'Pública',
      status: 'Cancelado'
    },
    {
      id: 5,
      title: 'Taller de liderazgo juvenil',
      startDate: '2024-11-25',
      endDate: '2024-12-05',
      department: 'Educación',
      visibility: 'Pública',
      status: 'Activo'
    }
  ];

  constructor(private router: Router, private missionsService: MissionsService) { }
  ngAfterViewInit(): void {
    this.initializeDataTable();
  }
  ngOnInit(): void {
    const orgId = localStorage.getItem('OrgId');
    if (orgId) {
      this.loadMissions(parseInt(orgId));
    } else {
      console.error('OrgId no encontrado en el localStorage');
    }
  }

  // Método para cargar las misiones de la organización
  loadMissions(orgId: number): void {
    this.missionsService.getMissionsByOrganization(orgId).subscribe(
      (missions) => {
        // Asignar el status por defecto a cada misión
        this.data = missions.map(mission => ({ ...mission, status: 'Activa' }));
        console.log('Misiones cargadas:', this.data);
        this.initializeDataTable();
      },
      (error) => {
        console.error('Error al cargar misiones:', error);
      }
    );
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
        }
      });
    }, 1);
  }

  // Método para abrir el modal de detalles de la misión
  openModal(mission: any) {
    this.selectedMission = mission;
    console.log('Misión seleccionada:', this.selectedMission);
  }

  // Método para manejar los detalles de la misión
  details(mission: Mission): void {
    console.log('Detalles de la misión seleccionada:', mission);
    this.router.navigate(['/detallesM'], { queryParams: { id: mission.id } });
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'Activo':
        return 'status-activo';
      case 'Completado':
        return 'status-completado';
      case 'Cancelado':
        return 'status-cancelado';
      default:
        return '';
    }
  }
}
