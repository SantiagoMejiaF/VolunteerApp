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




@Component({
  selector: 'app-dashboard-volunteer',
  templateUrl: '../view/dashboard-volunteer.component.html',
  styleUrls: ['../styles/dashboard-volunteer.component.css'],
})
export class DashboardVolunteerComponent implements AfterViewInit {
  public data: any[] = [];
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  


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
