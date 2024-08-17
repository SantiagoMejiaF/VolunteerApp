import { Component, OnInit, ViewChild } from '@angular/core';
import { AdminService } from '../../../services/admin.service';
import { OauthService } from '../../../services/oauth.service';
import { VolunteerService } from '../../../services/volunteer.service';
import { OrganizationService } from '../../../services/organization.service';
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
  templateUrl: './dashboardAdmin.component.html',
  styleUrls: ['./dashboardAdmin.component.css'],
})
export class DashboardAdminComponent implements OnInit {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  public userName: string = '';
  public totalUsers: number = 0;
  public activeVolunteers: number = 0;
  public activeOrganizations: number = 0;
  public data: any[] = [];

  constructor(private adminService: AdminService, private oauthService: OauthService, private volunteerService: VolunteerService,
    private organizationService: OrganizationService) {
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
      this.populateUserRoles();
    });

    // Inicializar DataTable
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
        },
      });
    }, 1);
  }

  populateUserRoles(): void {
    this.data.forEach((user) => {
      this.oauthService.getUserRole(user.roleId).subscribe((role) => {
        user.rol = role.roleType;
      });
    });
  }
}
