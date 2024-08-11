import { Component, ViewChild } from '@angular/core';
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
  styleUrls: ['./dashboardAdmin.component.css'], // Cambia `styleUrl` por `styleUrls` para que acepte un array de estilos
})
export class DashboardAdminComponent {
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;

  constructor() {
    setTimeout(() => {
      $('#datatableexample').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        //dom: 'ftip',
        scrollX:true,
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
            return val ;
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
        enabled: false, // Inicializa dataLabels aunque no esté habilitado
      },
    };
  }

  data = [
    { aja: 1, nombre: 'Juan Pérez', rol: 'voluntario', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 2, nombre: 'Alejandra Pérez', rol: 'organización', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 3, nombre: 'Juanes Pérez', rol: 'voluntario', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 4, nombre: 'Daniela Pérez', rol: 'organización', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 1, nombre: 'Juan Pérez', rol: 'voluntario', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 2, nombre: 'Alejandra Pérez', rol: 'organización', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 3, nombre: 'Juanes Pérez', rol: 'voluntario', email: 'juan@example.com', Cedula: '123456789' },
    { aja: 4, nombre: 'Daniela Pérez', rol: 'organización', email: 'juan@example.com', Cedula: '123456789' },
  ];

  

}
