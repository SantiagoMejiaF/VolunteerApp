import { Component, AfterViewInit, ViewChild } from '@angular/core';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexStroke,
} from 'ng-apexcharts';
import { VolunteerService } from '../model/services/volunteer.service';

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
  public completedActivities = 0;
  public averageRating = 0;
  public beneficiariesImpacted = 0;
  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;

  private y = 0;
  private userId: number;
  currentYear: number;
  currentMonth: number;
  selectedDate: Date;
  months: string[] = [
    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
  ];
  calendarDays: any[] = [];
  activities = [
    { id: 1, time: '09:30', name: 'Actividad de perritos', responsible: 'Henry Madariaga', date: new Date(2024, 6, 6) },
    { id: 2, time: '12:00', name: 'Enseñanza', responsible: 'Daniela Torres', date: new Date(2024, 6, 6) },
    { id: 3, time: '01:30', name: 'Actividad...', responsible: 'Nombre Responsable', date: new Date(2024, 6, 6) }
  ];

  constructor(private volunteerService: VolunteerService) {
    const today = new Date();
    this.currentYear = today.getFullYear();
    this.currentMonth = today.getMonth();
    this.selectedDate = today;
    this.userId = Number(localStorage.getItem('userId')); // Obtener el userId desde localStorage
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
          'ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic',
        ],
      },
    };
  }

  ngAfterViewInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData() {
    // Obtener actividades completadas
    this.volunteerService.getCompletedActivities(this.userId).subscribe(
      (data: number) => {
        this.completedActivities = data;
      },
      (error) => {
        console.error('Error al obtener las actividades completadas', error);
        this.completedActivities = 0; // Valor por defecto en caso de error
      }
    );

    // Obtener puntuación promedio
    this.volunteerService.getAverageRating(this.userId).subscribe(
      (data: number | null) => {
        this.averageRating = data ?? 0; // Si no hay datos, asignar 0
      },
      (error) => {
        console.error('Error al obtener la puntuación promedio', error);
        this.averageRating = 0; // Valor por defecto en caso de error
      }
    );

    // Obtener beneficiarios impactados
    this.volunteerService.getBeneficiaries(this.userId).subscribe(
      (data: number) => {
        this.beneficiariesImpacted = data;
      },
      (error) => {
        console.error('Error al obtener los beneficiarios impactados', error);
        this.beneficiariesImpacted = 0; // Valor por defecto en caso de error
      }
    );
  }


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
    return day && day.getDate() === today.getDate() &&
      day.getMonth() === today.getMonth() &&
      day.getFullYear() === today.getFullYear();
  }

  filteredActivities() {
    return this.activities.filter(activity => {
      return activity.date.toDateString() === this.selectedDate.toDateString();
    });
  }
}
