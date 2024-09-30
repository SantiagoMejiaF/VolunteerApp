import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../model/services/admin.service';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';
import {
  ApexAxisChartSeries,
  ApexChart,
  ChartComponent,
  ApexDataLabels,
  ApexXAxis,
  ApexPlotOptions,
  ApexStroke,
  ApexTitleSubtitle,
  ApexYAxis,
  ApexTooltip,
  ApexFill,
  ApexLegend,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  tooltip: ApexTooltip;
  fill: ApexFill;
  legend: ApexLegend;
};

@Component({
  selector: 'app-dash-borrar',
  templateUrl: '../view/dashboardAdmin.component.html',
  styleUrls: ['../styles/dashboardAdmin.component.css'],
})
export class DashboardAdminComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  public userName: string = '';
  public totalUsers: number = 0;
  public activeVolunteers: number = 0;
  public activeOrganizations: number = 0;
  public data: any[] = [];

  constructor(
    private adminService: AdminService,
    private volunteerService: VolunteerService,
    private organizationService: OrganizationService
  ) {
    this.chartOptions = {
      series: [
        {
          name: 'Voluntarios',
          data: [44, 55, 41, 37, 22, 43, 21],
        },
        {
          name: 'Organizaciones',
          data: [53, 32, 33, 52, 13, 43, 32],
        },
      ],
      chart: {
        type: 'bar',
        height: 350,
        stacked: true,
      },
      plotOptions: {
        bar: {
          horizontal: true,
        },
      },
      stroke: {
        width: 1,
        colors: ['#fff'],
      },
      title: {
        text: '',
      },
      xaxis: {
        categories: ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dic'],
        labels: {
          formatter: function (val) {
            return val;
          },
        },
      },
      yaxis: {
        title: {
          text: undefined,
        },
      },
      tooltip: {
        y: {
          formatter: function (val) {
            return val + 'K';
          },
        },
      },
      fill: {
        opacity: 1,
      },
      legend: {
        position: 'top',
        horizontalAlign: 'left',
        offsetX: 40,
      },
      dataLabels: {
        enabled: false,
      },
    };
  }

  ngOnInit(): void {
    // Obtener los datos del usuario desde localStorage
    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    if (userInfo) {
      this.userName = userInfo.firstName;
    }

    // Consumir los servicios para obtener los conteos
    this.adminService.getTotalUsers().subscribe((data) => {
      this.totalUsers = data;
    });

    this.volunteerService.getActiveVolunteers().subscribe((data) => {
      this.activeVolunteers = data;
    });

    this.organizationService.getActiveOrganizations().subscribe((data) => {
      this.activeOrganizations = data;
    });

    // Obtener los datos de los usuarios autorizados
    this.adminService.getAuthorizedUsers().subscribe((users) => {
      this.data = users;
      console.log('Usuarios autorizados obtenidos:', this.data);

      // Llenar los roles de los usuarios
      this.populateUserRoles().then(() => {
        // Inicializar DataTable después de cargar los datos y roles
        this.initializeDataTable();
      });
    });
  }

  // Método para llenar los roles y detalles adicionales de los usuarios
  populateUserRoles(): Promise<void> {
    return new Promise<void>((resolve) => {
      let completedRequests = 0;

      this.data.forEach((user) => {
        user.rol = user.role.roleType; // Usar directamente el rol desde el objeto usuario

        if (user.rol === 'VOLUNTARIO') {
          this.volunteerService.getVolunteerDetails(user.id).subscribe((volunteerDetails) => {
            user.cedula = volunteerDetails.personalInformation.identificationCard;
            completedRequests++;
            if (completedRequests === this.data.length) resolve();
          });
        } else if (user.rol === 'ORGANIZACION') {
          this.organizationService.getOrganizationDetails(user.id).subscribe((organizationDetails) => {
            user.cedula = organizationDetails.responsiblePersonId;
            completedRequests++;
            if (completedRequests === this.data.length) resolve();
          });
        } else {
          completedRequests++;
          if (completedRequests === this.data.length) resolve();
        }
      });

      if (this.data.length === 0) resolve(); // Resuelve la promesa si no hay usuarios
    });
  }

  initializeDataTable(): void {
    setTimeout(() => {
      const tableId = '#datatableGestionUser';  // Cambia por el ID correspondiente

      if ($.fn.dataTable.isDataTable(tableId)) {
        $(tableId).DataTable().destroy();
      }

      $(tableId).DataTable({
        data: this.data, // Asignamos los datos al DataTable
        columns: [
          { title: "#", data: "id" },  // Columna del índice
          {
            title: "Perfil",
            data: null,
            render: (data, type, row) => {
              return `<img src="${data.profileImage}" alt="${data.name}" class="profile-img"/>`;
            }
          },
          { title: "Nombre", data: "name" },  // Nombre con rol
          { title: "Email", data: "email" },
          { title: "Cédula", data: "cedula" }
        ],
        pagingType: 'full_numbers',
        pageLength: 7,
        processing: true,
        lengthMenu: [5, 10, 25, 50],
        scrollX: true,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrando página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty: '<span style="font-size: 0.875rem;">No hay registros disponibles</span>',
          infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros totales)</span>',
          lengthMenu: '<span style="font-size: 0.875rem;">Mostrar _MENU_ registros por página</span>',
          zeroRecords: '<span style="font-size: 0.875rem;">No se encontraron resultados</span>',
          paginate: {
            first: 'Primero',
            last: 'Último',
            next: 'Siguiente',
            previous: 'Anterior'
          }
        },
      });
    }, 1000);
  }
}
