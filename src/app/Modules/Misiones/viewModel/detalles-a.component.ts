import { Component, ViewChild, EventEmitter, Output } from "@angular/core";


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
  currentContent: string = 'content1';
  selectedSection: string = 'descripcion';
  isEditing = false;
  public data: any[] = [
    {
      firstName: 'Juan',
      lastName: 'Pérez',
      email: 'juan.perez@example.com',
      cedula: '12345678',
      image: 'assets/img/user1.png',
    },
    {
      firstName: 'María',
      lastName: 'Gómez',
      email: 'maria.gomez@example.com',
      cedula: '87654321',
      image: 'assets/img/user2.png',
    },
    {
      firstName: 'Carlos',
      lastName: 'Rodríguez',
      email: 'carlos.rodriguez@example.com',
      cedula: '12349876',
      image: 'assets/img/user3.png',
    },
    {
      firstName: 'Lucía',
      lastName: 'Martínez',
      email: 'lucia.martinez@example.com',
      cedula: '98761234',
      image: '',
    },
    {
      firstName: 'Pedro',
      lastName: 'Sánchez',
      email: 'pedro.sanchez@example.com',
      cedula: '13579246',
      image: 'assets/img/user5.png',
    }
  ];
  
  showContent(contentId: string) {
    this.currentContent = contentId;
  }
  toggleEdit() {
    this.isEditing = !this.isEditing;
  }
  @Output() back = new EventEmitter<void>();

  onComeBack() {
    this.back.emit();
  }
  @ViewChild("chart") chart: ChartComponent;
  public chartOptions:ChartOptions;
  public chartOptions1:ChartOptions1;
 
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
        text: "# de Voluntarios", // Título de la gráfica
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
        text: "# de Beneficiarios", // Título de la gráfica
        align: 'center',
        style: {
          fontSize: '16px',
          color: '#333'
        }
      }
    };
  }
}