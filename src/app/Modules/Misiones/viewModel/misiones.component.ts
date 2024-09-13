import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-misiones',
  templateUrl: '../view/misiones.component.html',
  styleUrls: ['../../../styles/misione.component.css']
})
export class MisionesComponent implements OnInit {
  selectedMission: any = {};
  data: any[] = [];

  constructor(private router: Router) {}
  
  ngOnInit(): void {
    // Crear una lista de misiones directamente en el archivo para probar
    this.data = [
      { id: 1, name: 'Misión A', startDate: '2023-09-01', endDate: '2023-09-30', department: 'Cundinamarca', visibility: 'Pública', status: 'Activa' },
      { id: 2, name: 'Misión B', startDate: '2023-10-01', endDate: '2023-10-15', department: 'Antioquia', visibility: 'Privada', status: 'Cancelada' },
      { id: 3, name: 'Misión C', startDate: '2023-11-01', endDate: '2023-11-20', department: 'Valle del Cauca', visibility: 'Pública', status: 'Completada' }
    ];
    console.log('Misiones generadas:', this.data);
    this.initializeDataTable();
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
          { data: 'name' },
          { data: 'startDate' },
          { data: 'endDate' },
          { data: 'department' },
          { data: 'visibility' },
          {
            data: 'status',
            render: function (data, type, row) {
              if (type === 'display') {
                let bgColor = '';
                let textColor = '';

                // Modificar los colores dependiendo del estado
                if (data === 'Completada') {
                  bgColor = 'rgba(82, 243, 101, 0.1)';
                  textColor = '#1CC52A';
                } else if (data === 'Activa') {
                  bgColor = 'lightyellow';
                  textColor = '#ADBF38';
                } else if (data === 'Cancelada') {
                  bgColor = '#FEEEEE';
                  textColor = '#F35252';
                }

                // Retornar el elemento con estilo inline
                return `<span style="background-color:${bgColor}; color:${textColor}; padding: 4px 8px; border-radius: 12px; display: inline-block;">${data}</span>`;              }
              return data;
            }
          },
          {data: 'accion'}
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
    this.selectedMission = mission; // Cargar los datos de la misión seleccionada en el modal
    console.log('Misión seleccionada:', this.selectedMission);
  }
  details(mission: any) {
    this.selectedMission = mission;
    console.log('Misión seleccionada:', mission);
    this.router.navigate(['/detallesM']); // Navega a la ruta de detalles
  }
  
}
