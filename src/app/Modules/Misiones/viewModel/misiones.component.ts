import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MissionsService } from '../model/services/mission.service';
import { Mission } from '../model/mission.model';

@Component({
  selector: 'app-misiones',
  templateUrl: '../view/misiones.component.html',
  styleUrls: ['../../../styles/misione.component.css']
})
export class MisionesComponent implements OnInit {
  selectedMission: any = {};
  data: Mission[] = [];

  constructor(private router: Router, private missionsService: MissionsService) { }

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
        columns: [
          { data: 'id' },
          { data: 'title' },
          { data: 'startDate' },
          { data: 'endDate' },
          { data: 'department' },
          { data: 'visibility' },
          {
            data: 'status',
            render: function (data, type, row) {
              if (type === 'display') {
                let bgColor = 'lightyellow';
                let textColor = '#ADBF38';

                if (data === 'Completada') {
                  bgColor = 'rgba(82, 243, 101, 0.1)';
                  textColor = '#1CC52A';
                } else if (data === 'Cancelada') {
                  bgColor = '#FEEEEE';
                  textColor = '#F35252';
                }

                return `<span style="background-color:${bgColor}; color:${textColor}; padding: 4px 8px; border-radius: 12px; display: inline-block;">${data}</span>`;
              }
              return data;
            }
          },
          { data: 'accion' }
        ],
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

  openModal(mission: any) {
    this.selectedMission = mission;
    console.log('Misión seleccionada:', this.selectedMission);
  }

  details(mission: Mission) {
    console.log('Detalles de la misión seleccionada:', mission);  // Verifica si se imprime en la consola
    this.router.navigate(['/detallesM'], { queryParams: { id: mission.id } });
  }


}
