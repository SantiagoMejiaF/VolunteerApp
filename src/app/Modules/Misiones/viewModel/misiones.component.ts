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
  data: Mission[] = [];
  dataTable: any;  // Variable para manejar la tabla
  tableInitialized = false; // Bandera para saber si la tabla ya ha sido inicializada
  missionTypes: string[] = [];
  volunteerRequirements: string[] = [];
  requiredSkills: string[] = [];
  newMission: any = {
    missionType: '',
    title: '',
    description: '',
    startDate: '',
    endDate: '',
    department: '',
    visibility: true, // Visible por defecto
    volunteerMissionRequirementsEnumList: [],
    requiredSkillsList: []
  };

  constructor(private router: Router, private missionsService: MissionsService) { }

  ngAfterViewInit(): void {
    this.initializeDataTable();
  }

  ngOnInit(): void {
    const orgId = localStorage.getItem('OrgId');
    if (orgId) {
      this.loadMissions(parseInt(orgId));
      this.loadMissionTypes();
      this.loadVolunteerRequirements();
      this.loadRequiredSkills();
    } else {
      console.error('OrgId no encontrado en el localStorage');
    }

    // Agrega aquí el MutationObserver para monitorear cuando se agregue un backdrop al DOM
    const observer = new MutationObserver(function (mutationsList) {
      for (let mutation of mutationsList) {
        // Asegúrate de que el nodo sea un HTMLElement
        mutation.addedNodes.forEach((node) => {
          if (node instanceof HTMLElement && node.classList.contains('modal-backdrop')) {
            console.log('Se ha añadido un modal-backdrop');
          }
        });
      }
    });

    // Observar el body por cambios en el DOM
    observer.observe(document.body, { childList: true, subtree: true });
  }

  // Método para cargar las misiones de la organización
  loadMissions(orgId: number): void {
    this.missionsService.getMissionsByOrganization(orgId).subscribe(
      (missions) => {
        this.data = missions;
        this.initializeDataTable();
      },
      (error) => {
        console.error('Error al cargar misiones:', error);
      }
    );
  }

  initializeDataTable(): void {
    if (this.dataTable) {
      this.dataTable.destroy();  // Destruir la tabla antes de reinicializarla
    }

    setTimeout(() => {

      this.dataTable = $('#datatableMisiones').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,  // Ajustar a tus necesidades
        processing: true,
        lengthMenu: [5, 10, 25, 50],  // Menú de paginado
        scrollX: true,  // Mantener el scroll horizontal
        scrollY: '400px',  // Mantener el scroll vertical (ajusta según tus necesidades)
        scrollCollapse: true,
        data: this.data,
        columns: [
          { data: 'id' },
          { data: 'title' },
          { data: 'startDate' },
          { data: 'endDate' },
          { data: 'department' },
          { data: 'visibility' },
          {
            data: 'missionStatus',
            render: function (data, type, row) {
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
          },
          {
            data: null,  // Columna para acciones (ver detalles y eliminar)
            render: function (data, type, row) {
              return `
                <a href="/detallesM?id=${row.id}" style="border: none; background: none;">
                  <i class="bi bi-eye" style="font-size: 1.3rem; color: #000000;"
                      onmouseover="this.style.color='#186dde';"
                      onmouseout="this.style.color='#80777c';"></i>
                </a>
                <button style="border: none; background: none;">
                  <i class="bi bi-trash3" style="font-size: 1.1rem; color: #000000;"
                      onmouseover="this.style.color='#186dde';"
                      onmouseout="this.style.color='#80777c';"></i>
                </button>
              `;
            }
          }
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

  // Cargar tipos de misión
  loadMissionTypes(): void {
    this.missionsService.getMissionTypes().subscribe(
      (types) => {
        this.missionTypes = types;
      },
      (error) => {
        console.error('Error al cargar los tipos de misiones:', error);
      }
    );
  }

  // Cargar requisitos para voluntarios
  loadVolunteerRequirements(): void {
    this.missionsService.getVolunteerRequirements().subscribe(
      (requirements) => {
        this.volunteerRequirements = requirements;
      },
      (error) => {
        console.error('Error al cargar los requisitos para voluntarios:', error);
      }
    );
  }

  // Cargar habilidades requeridas
  loadRequiredSkills(): void {
    this.missionsService.getRequiredSkills().subscribe(
      (skills) => {
        this.requiredSkills = skills;
      },
      (error) => {
        console.error('Error al cargar las habilidades requeridas:', error);
      }
    );
  }

  createMission(): void {
    const mission = {
      organizationId: parseInt(localStorage.getItem('OrgId') || '0'),
      missionType: this.newMission.missionType,
      title: this.newMission.title,
      description: this.newMission.description,
      startDate: this.newMission.startDate,
      endDate: this.newMission.endDate,
      department: this.newMission.department,
      visibility: this.newMission.visibility ? 'PUBLICA' : 'PRIVADA',
      volunteerMissionRequirementsEnumList: this.newMission.volunteerMissionRequirementsEnumList,
      requiredSkillsList: this.newMission.requiredSkillsList
    };

    this.missionsService.createMission(mission).subscribe(
      (response) => {
        this.showToast();
        this.closeModal();
        this.loadMissions(mission.organizationId);
      },
      (error) => {
        console.error('Error al crear la misión:', error);
      }
    );
  }

  showToast(): void {
    const toastElement = document.getElementById('missionCreatedToast');
    if (toastElement) {
      toastElement.classList.add('show');
      setTimeout(() => {
        toastElement.classList.remove('show');
      }, 3000);
    }
  }

  openModal(event: Event): void {
    event.preventDefault();

    // Abrir el modal de manera personalizada
    const modalElement = document.getElementById('MissionModal');
    if (modalElement) {
      modalElement.style.display = 'block';  // Mostrar el modal
      modalElement.classList.add('show');  // Agregar la clase que lo hace visible
      document.body.classList.add('modal-open');  // Asegurarse de que el body esté en modo modal

      // Esperar un breve momento para asegurar que el backdrop se haya generado
      setTimeout(() => {
        // Eliminar cualquier backdrop adicional que haya sido generado
        const backdrops = document.querySelectorAll('.modal-backdrop');
        if (backdrops.length > 1) {
          backdrops.forEach((backdrop, index) => {
            if (index > 0) backdrop.remove();  // Eliminar los backdrops adicionales, dejando solo el primero
          });
        }
      }, 50);  // 50ms debería ser suficiente para esperar a que el backdrop aparezca
    }
  }

  closeModal(): void {
    const modalElement = document.getElementById('MissionModal');
    if (modalElement) {
      modalElement.style.display = 'none';  // Ocultar el modal
      modalElement.classList.remove('show');  // Remover la clase que lo muestra
      document.body.classList.remove('modal-open');  // Restaurar el estado del body

      // Eliminar cualquier backdrop adicional
      const backdrops = document.querySelectorAll('.modal-backdrop');
      backdrops.forEach((backdrop) => {
        backdrop.remove();
      });
    }
  }

  // Restaurar la función 'details'
  details(missionId: number) {
    this.router.navigate(['/detallesM'], { queryParams: { id: missionId } });
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
