import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexStroke,
  ApexTitleSubtitle,
  ApexPlotOptions,
  ApexLegend,
  ApexNonAxisChartSeries,
  ApexResponsive,
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  dataLabels: ApexDataLabels;
};


export type ChartOptions2 = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  title: ApexTitleSubtitle;
  plotOptions: ApexPlotOptions;
  legend: ApexLegend;
  colors: string[];
};



export type ChartOptions3 = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  colors: string[];
  legend: ApexLegend;
  plotOptions: ApexPlotOptions;
  responsive: ApexResponsive[] | ApexResponsive[];
};
@Component({
  selector: 'app-dashboard-organization',
  templateUrl: '../view/dashboard-organization.component.html',
  styleUrl: '../../../styles/dashboard-organization.component.css'
})
export class DashboardOrganizationComponent implements AfterViewInit {
  public data: any[] = [];
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  public chartOptions2: ChartOptions2;
  public chartOptions3: ChartOptions3;
  


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
    this.chartOptions2 = {
      series: [
        {
          name: "Desktops",
          data: [
            {
              x: "ABC",
              y: 10
            },
            {
              x: "DEF",
              y: 60
            },
            {
              x: "XYZ",
              y: 41
            }
          ]
        },
        {
          name: "Mobile",
          data: [
            {
              x: "ABCD",
              y: 10
            },
            {
              x: "DEFG",
              y: 20
            },
            {
              x: "WXYZ",
              y: 51
            },
            {
              x: "PQR",
              y: 30
            },
            {
              x: "MNO",
              y: 20
            },
            {
              x: "CDE",
              y: 30
            }
          ]
        }
      ],
      legend: {
        show: false
      },
      chart: {
        height: 350,
        type: "treemap"
      },
      title: {
        text: "",
        align: "center"
      },
      dataLabels: {
        enabled: true
      },
      plotOptions: {
        treemap: {
          enableShades: true,
          distributed: false
        }
      },
      colors: ["#FF4560", "#00E396", "#008FFB"]
    };
    this.chartOptions3 = {
      series: [76, 67, 61, 90, 45],
      chart: {
        height: 390,
        type: "radialBar"
      },
      plotOptions: {
        radialBar: {
          offsetY: 0,
          startAngle: 0,
          endAngle: 270,
          hollow: {
            margin: 5,
            size: "30%",
            background: "transparent",
            image: undefined
          },
          dataLabels: {
            name: {
              show: true, 
              fontSize: '14px' 
            },
            value: {
              show: true, 
              fontSize: '16px'
            }
          }
        }
      },
      colors: ["#1ab7ea", "#0084ff", "#39539E", "#0077B5", "#306e86"],
      labels: ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes"],
      legend: {
        show: true,
        floating: true,
        fontSize: "16px",
        position: "left",
        offsetX: 50,
        offsetY: 10,
        labels: {
          useSeriesColors: true
        },
        formatter: function (seriesName, opts) {
          return seriesName + ":  " + opts.w.globals.series[opts.seriesIndex];
        },
        itemMargin: {
          horizontal: 3
        }
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              height: 300
            },
            legend: {
              show: false
            },
            plotOptions: {
              radialBar: {
                dataLabels: {
                  name: {
                    show: true,
                    fontSize: '9px' // Tamaño de fuente reducido para pantallas pequeñas
                  },
                  value: {
                    show: true,
                    fontSize: '10px' // Tamaño de fuente reducido para pantallas pequeñas
                  }
                }
              }
            }
          }
        }
      ]
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

