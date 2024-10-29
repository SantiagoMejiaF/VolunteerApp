import { Component, ViewChild, EventEmitter, Output, Input } from "@angular/core";


import {
  ApexNonAxisChartSeries,
  ApexPlotOptions,
  ApexTitleSubtitle,
  ApexChart,
  ApexFill,
  ChartComponent
} from "ng-apexcharts";



export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  plotOptions: ApexPlotOptions;
  fill: ApexFill;
  title: ApexTitleSubtitle; // Agregamos el título
};

export type ChartOptions1 = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  plotOptions: ApexPlotOptions;
  fill: ApexFill;
  title: ApexTitleSubtitle; // Agregamos el título
};

@Component({
  selector: 'app-detalles-a',
  templateUrl: '../view/detalles-a.component.html',
  styleUrl: '../styles/detalles-a.component.css'
})
export class DetallesAComponent {
  @Input() activity: any;  // Recibe los detalles de la actividad seleccionada
  @Input() iconType: 'calendar' | 'back' = 'calendar'; // Tipo de ícono que se muestra
  currentContent: string = 'content1';
  isEditing = false;

  @ViewChild("chart") chart: ChartComponent;
  public chartOptions: ChartOptions;
  public chartOptions1: ChartOptions1;

  constructor() {
    this.chartOptions = {
      series: [76],
      chart: {
        type: "radialBar",
        offsetY: -20
      },
      plotOptions: {
        radialBar: {
          startAngle: -90,
          endAngle: 90,
          track: {
            background: "#e7e7e7",
            strokeWidth: "97%",
            margin: 5, // margin is in pixels
            dropShadow: {
              enabled: true,
              top: 2,
              left: 0,
              opacity: 0.31,
              blur: 2
            }
          },
          dataLabels: {
            name: {
              show: false
            },
            value: {
              offsetY: -2,
              fontSize: "22px"
            }
          }
        }
      },
      fill: {
        type: "gradient",
        gradient: {
          shade: "light",
          shadeIntensity: 0.4,
          inverseColors: false,
          opacityFrom: 1,
          opacityTo: 1,
          stops: [0, 50, 53, 91]
        }
      },
      labels: ["Average Results"],
      title: {
        text: "# de Voluntarios",
        align: 'center',
        style: {
          fontSize: '16px',
          color: '#333'
        }
      }
    };

    this.chartOptions1 = {
      series: [76],
      chart: {
        type: "radialBar",
        offsetY: -20
      },
      plotOptions: {
        radialBar: {
          startAngle: -90,
          endAngle: 90,
          track: {
            background: "#e7e7e7",
            strokeWidth: "97%",
            margin: 5,
            dropShadow: {
              enabled: true,
              top: 2,
              left: 0,
              opacity: 0.31,
              blur: 2
            }
          },
          dataLabels: {
            name: {
              show: false
            },
            value: {
              offsetY: -2,
              fontSize: "22px"
            }
          }
        }
      },
      fill: {
        type: "gradient",
        gradient: {
          shade: "light",
          shadeIntensity: 0.4,
          inverseColors: false,
          opacityFrom: 1,
          opacityTo: 1,
          stops: [0, 50, 53, 91]
        }
      },
      labels: ["Average Results"],
      title: {
        text: "# de Beneficiarios",
        align: 'center',
        style: {
          fontSize: '16px',
          color: '#333'
        }
      }
    };
  }

  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  onComeBack() {
    console.log('Back button clicked'); // Simplemente para depuración
  }

  ngOnChanges() {
    console.log('Activity:', this.activity);
  }

}