import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexStroke,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  dataLabels: ApexDataLabels;
};

@Component({
  selector: 'app-dashboard-volunteer',
  templateUrl: '../view/dashboard-volunteer.component.html',
  styleUrls: ['../../../styles/dashboard-volunteer.component.css'],
})
export class DashboardVolunteerComponent implements AfterViewInit {
  public data: any[] = [];
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  


  private y = 0;

  constructor() {
    this.chartOptions = {
      series: [
        {
          name: 'actividades completadas',
          data: [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110],
        },
      ],
      chart: {
        height: 350,
        type: 'area',
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
      },
      xaxis: {
        categories: [
          'ene',
          'feb',
          'mar',
          'abr',
          'may',
          'jun',
          'jul',
          'ago',
          'sep',
          'oct',
          'nov',
          'dic',
        ],
        labels: {
          formatter: function (val) {
            return val;
          },
        },
      },
    };
  }

  ngAfterViewInit(): void {
    const slide = document.getElementById('slide');
    const upArrow = document.getElementById('upArrow');
    const downArrow = document.getElementById('downArrow');

    if (upArrow && downArrow && slide) {
        downArrow.addEventListener('click', () => {
            if (this.y > -600) {
                this.y -= 300;
                slide.style.transform = `translateY(${this.y}px)`;
            }
        });

        upArrow.addEventListener('click', () => {
            if (this.y < 0) {
                this.y += 300;
                slide.style.transform = `translateY(${this.y}px)`;
            }
        });
    } else {
        console.error('One or more elements are not available:', {
            upArrow,
            downArrow,
            slide
        });
    }
}



}
