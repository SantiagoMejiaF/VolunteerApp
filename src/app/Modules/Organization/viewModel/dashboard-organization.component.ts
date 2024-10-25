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
  styleUrl: '../styles/dashboard-organization.component.css'
})
export class DashboardOrganizationComponent implements AfterViewInit {
  public data: any[] = [];
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  public chartOptions2: ChartOptions2;
  public chartOptions3: ChartOptions3;
  


  private y = 0;

  constructor() {
    const today = new Date();
    this.currentYear = today.getFullYear();
    this.currentMonth = today.getMonth();
    this.selectedDate = today;
    this.generateCalendar();
    this.chartOptions = {
      series: [
        {
          name: 'actividades completadas',
          data: [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110],
          color: "#FF5733"
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
      }
    };
    this.chartOptions2 = {
      series: [
        {
          name: "Desktops",
          data: [
            {
              x: "ABC",
              y: 10,
              fillColor: "#06C9D7"
            },
            {
              x: "DEF",
              y: 60,
              fillColor: "#06C9D7"
            },
            {
              x: "XYZ",
              y: 41,
              fillColor: "#06C9D7"
            }
          ]
        },
        {
          name: "Mobile",
          data: [
            {
              x: "ABCD",
              y: 10,
              fillColor: "#fb9778"
            },
            {
              x: "DEFG",
              y: 20,
              fillColor: "#fb9778"
            },
            {
              x: "WXYZ",
              y: 51,
              fillColor: "#fb9778"
            },
            {
              x: "PQR",
              y: 30,
              fillColor: "#fb9778"
            },
            {
              x: "MNO",
              y: 20,
              fillColor: "#fb9778"
            },
            {
              x: "CDE",
              y: 30,
              fillColor: "#fb9778"
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
      }
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

currentYear: number;
currentMonth: number;
selectedDate: Date;
months: string[] = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
calendarDays: any[] = [];  // Array para almacenar las fechas del mes

activities = [
  { id: 1, time: '09:30', name: 'Actividad de perritos', responsible: 'Henry Madariaga', date: new Date(2024, 6, 6) },
  { id: 2, time: '12:00', name: 'Enseñanza', responsible: 'Daniela Torres', date: new Date(2024, 6, 6) },
  { id: 3, time: '01:30', name: 'Actividad...', responsible: 'Nombre Responsable', date: new Date(2024, 6, 6) }
];

// Método para generar las fechas del mes
generateCalendar() {
  this.calendarDays = [];
  const firstDay = new Date(this.currentYear, this.currentMonth, 1).getDay();
  const daysInMonth = new Date(this.currentYear, this.currentMonth + 1, 0).getDate();

  let day = 1;
  for (let i = 0; i < 6; i++) {
    const week = [];
    for (let j = 0; j < 7; j++) {
      if (i === 0 && j < firstDay) {
        week.push(null);
      } else if (day > daysInMonth) {
        week.push(null);
      } else {
        week.push(new Date(this.currentYear, this.currentMonth, day));
        day++;
      }
    }
    this.calendarDays.push(week);
  }
}

// Método para verificar si hay actividades en un día específico
hasActivities(day: Date): boolean {
  return this.activities.some(activity => 
    activity.date.toDateString() === day?.toDateString()
  );
}

// Otros métodos de navegación y selección de días
prevMonth() {
  if (this.currentMonth === 0) {
    this.currentMonth = 11;
    this.currentYear--;
  } else {
    this.currentMonth--;
  }
  this.generateCalendar();
}

nextMonth() {
  if (this.currentMonth === 11) {
    this.currentMonth = 0;
    this.currentYear++;
  } else {
    this.currentMonth++;
  }
  this.generateCalendar();
}

selectDay(day: Date) {
  if (day) {
    this.selectedDate = day;
  }
}

isToday(day: Date) {
  const today = new Date();
  return day && day.getDate() === today.getDate() && day.getMonth() === today.getMonth() && day.getFullYear() === today.getFullYear();
}

filteredActivities() {
  return this.activities.filter(activity => {
    return activity.date.toDateString() === this.selectedDate.toDateString();
  });
}
}
