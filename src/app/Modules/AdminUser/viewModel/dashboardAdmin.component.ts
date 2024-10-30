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
  selector: 'app-dashboard-admin',
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

  // Nuevo para el dropdown de años
  public selectedYear: number;
  public yearRange: number[] = [];

  constructor(
    private adminService: AdminService,
    private volunteerService: VolunteerService,
    private organizationService: OrganizationService
  ) {
    this.chartOptions = {
      series: [],
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
      xaxis: {
        title: {
          text: 'Cantidad',
        },
        categories: ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic'],
      },
      yaxis: {
      },
      title: {
        text: 'Voluntarios y Organizaciones por Mes',
        align: 'center',
      },
      tooltip: {
        y: {
          formatter: function (val) {
            return val.toString();
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

    // Definir el año actual como predeterminado
    this.selectedYear = new Date().getFullYear();
  }

  ngOnInit(): void {
    // Calcular el rango de años: 3 años antes y 3 años después
    this.calculateYearRange();

    // Cargar datos iniciales para el año predeterminado
    this.loadChartData(this.selectedYear);

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

    // Obtener los datos de los usuarios autorizados y limitar a las últimas 5 entradas
    this.adminService.getAuthorizedUsers().subscribe((users) => {
      this.data = users.slice(-5); // Mostrar solo las últimas 5 entradas
      console.log('Últimos usuarios obtenidos:', this.data);

      // Llenar los roles de los usuarios
      this.populateUserRoles().then(() => {
        // Inicializar DataTable después de cargar los datos y roles
        this.initializeDataTable();
      });
    });
  }

  // Calcular el rango de años
  calculateYearRange() {
    const currentYear = new Date().getFullYear();
    const startYear = currentYear - 3;
    const endYear = currentYear + 3;
    this.yearRange = Array.from({ length: endYear - startYear + 1 }, (_, i) => startYear + i);
  }

  // Cargar datos para el gráfico según el año seleccionado
  loadChartData(year: number): void {
    this.adminService.getVolunteersCountByMonth(year).subscribe((volunteerData) => {
      this.adminService.getOrganizationsCountByMonth(year).subscribe((organizationData) => {
        // Asegúrate de que los datos sean solo números
        const volunteerSeries = Object.values(volunteerData).map(val => Number(val));
        const organizationSeries = Object.values(organizationData).map(val => Number(val));

        // Actualizar el gráfico con los datos dinámicos
        this.chartOptions.series = [
          {
            name: 'Voluntarios',
            data: volunteerSeries,
            color: "#fb9778"
          },
          {
            name: 'Organizaciones',
            data: organizationSeries,
            color: "#06C9D7"
          }
        ];
      });
    });
  }

  // Llamado cuando el usuario selecciona un año
  onYearChange(event: any): void {
    this.selectedYear = event.target.value;  // Actualizar el año seleccionado
    this.loadChartData(this.selectedYear);   // Cargar los datos correspondientes al nuevo año
  }

  populateUserRoles(): Promise<void> {
    return new Promise<void>((resolve) => {
      let completedRequests = 0;
      console.log('Datos completos de usuarios autorizados en this.data:', this.data);

      this.data.forEach((user) => {
        user.rol = user.role.roleType;
        if (!user.firstName || !user.lastName || !user.cedula) {
          if (user.rol === 'VOLUNTARIO') {
            // No deberías sobrescribir los valores de firstName y lastName si ya existen

            this.volunteerService.getVolunteerDetails(user.id).subscribe((volunteerDetails) => {
              if (volunteerDetails && volunteerDetails.personalInformation) {
                user.firstName = volunteerDetails.personalInformation.firstName || user.firstName || "Sin nombre";
                user.lastName = volunteerDetails.personalInformation.lastName || user.lastName || "";
                user.cedula = volunteerDetails.personalInformation.identificationCard || "Sin cédula";
              }
              completedRequests++;
              if (completedRequests === this.data.length) resolve();
            }, error => {
              console.log('Error al obtener detalles del voluntario:', error);
              completedRequests++;
              if (completedRequests === this.data.length) resolve();
            });

          } else if (user.rol === 'ORGANIZACION') {
            this.organizationService.getOrganizationDetails(user.id).subscribe((organizationDetails) => {
              user.firstName = organizationDetails.organizationName; // Usamos organizationName si no hay firstName
              user.lastName = '';  // Dejar vacío o asignar algún valor si no hay lastName
              user.cedula = organizationDetails.responsiblePersonId || "Sin cédula";

              completedRequests++;
              if (completedRequests === this.data.length) resolve();
            });
          } else if (user.rol === 'COORDINADOR_ACTIVIDAD') {
            this.adminService.getCoordinatorDetails(user.id).subscribe((coordinatorDetails) => {
              user.cedula = coordinatorDetails.identificationCard || "Sin cédula";

              completedRequests++;
              if (completedRequests === this.data.length) resolve();
            });
          } else {
            completedRequests++;
            if (completedRequests === this.data.length) resolve();
          }
        } else {
          completedRequests++;
          if (completedRequests === this.data.length) resolve();
        }
      });

      if (this.data.length === 0) resolve();
    });
  }

  initializeDataTable(): void {
    setTimeout(() => {
      const tableId = '#datatableDashAdmin';  // Cambia por el ID correspondiente

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
              return `<img src="${data.image || 'assets/img/user2.png'}" alt="Perfil" 
                style="width:50px; height:50px; border-radius:50%;" 
                onerror="this.src='assets/img/user3.png'"/>`;
            }
          },
          {
            title: "Nombre",
            data: null,
            render: (data, type, row) => {
              return data.firstName ? `${data.firstName} ${data.lastName}` : data.organizationName || "Sin nombre";
            }
          },
          { title: "Email", data: "email" },
          { title: "Cédula", data: "cedula" }
        ],
        processing: true,
        scrollX: true,
        paging: false,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrando página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty: '<span style="font-size: 0.875rem;">No hay registros disponibles</span>',
          infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros totales)</span>',
          lengthMenu: '<span style="font-size: 0.875rem;">Mostrar _MENU_ registros por página</span>',
          zeroRecords: '<span style="font-size: 0.875rem;">No se encontraron resultados</span>',
        },
      });
    }, 1000);
  }

  ngAfterViewInit(): void {
    this.initializeDataTable();
  }
}
