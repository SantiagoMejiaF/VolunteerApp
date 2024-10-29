import { Component, AfterViewInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexStroke,
} from 'ng-apexcharts';
import { VolunteerService } from '../model/services/volunteer.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';
import { MissionsService } from '../../Misiones/model/services/mission.service';

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
  public organizations: any[] = [];

  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;

  // Rango de años para el dropdown
  public selectedYear: number;
  public yearRange: number[] = [];

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

  constructor(
    private volunteerService: VolunteerService,
    private organizationService: OrganizationService,
    private missionsService: MissionsService,
    private router: Router
  ) {
    const today = new Date();
    this.selectedYear = today.getFullYear(); // Año actual
    this.currentYear = today.getFullYear();
    this.currentMonth = today.getMonth();
    this.selectedDate = today;
    this.userId = Number(localStorage.getItem('userId')); // Obtener el userId desde localStorage

    this.generateYearRange();
    this.generateCalendar();

    this.chartOptions = {
      series: [
        {
          name: 'actividades completadas',
          data: [], // Los datos se cargarán dinámicamente
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
    this.loadOrganizations();
    this.loadProgrammedActivities();
  }

  loadProgrammedActivities() {
    const volunteerId = Number(localStorage.getItem('volunteerId'));
    if (!volunteerId) {
      console.error('No se encontró volunteerId en localStorage.');
      return;
    }

    this.volunteerService.getActivitiesByVolunteerId(volunteerId).subscribe(
      (activities: any[]) => {
        // Iterar sobre las actividades y buscar los coordinadores
        activities.forEach(activity => {
          if (activity.activityCoordinator) {
            this.loadCoordinatorDetails(activity);
          } else {
            activity.responsible = 'No asignado';
          }
        });

        // Guardamos las actividades programadas en la lista "activities"
        this.activities = activities.map(activity => ({
          id: activity.id,
          time: `${activity.startTime}`, // Formato de la hora de inicio
          name: activity.title,  // Nombre de la actividad
          responsible: activity.responsible || 'Cargando...', // Inicialmente poner "Cargando..." si falta el nombre
          date: new Date(activity.date)  // Convertir la fecha
        }));

        // Actualizamos el calendario
        this.generateCalendar();
      },
      (error) => {
        console.error('Error al cargar las actividades programadas', error);
      }
    );
  }

  loadCoordinatorDetails(activity: any) {
    this.missionsService.getActivityCoordinator(activity.activityCoordinator).subscribe(
      (coordinatorDetails) => {
        if (coordinatorDetails.userId) {
          this.organizationService.getUserDetails(coordinatorDetails.userId).subscribe(
            (userDetails) => {
              // Actualizamos el campo responsable de la actividad en la lista original
              const foundActivity = this.activities.find(a => a.id === activity.id);
              if (foundActivity) {
                foundActivity.responsible = `${userDetails.firstName} ${userDetails.lastName}`;
              }
            },
            (error) => {
              console.error('Error al obtener detalles del usuario responsable', error);
              activity.responsible = 'No asignado';
            }
          );
        }
      },
      (error) => {
        console.error('Error al obtener detalles del coordinador', error);
        activity.responsible = 'No asignado';
      }
    );
  }


  // Método para cargar las fundaciones recientes del voluntario
  loadOrganizations() {
    const volunteerId = Number(localStorage.getItem('volunteerId'));
    if (!volunteerId) {
      console.error('No se encontró volunteerId en localStorage.');
      return;
    }

    // Usar el servicio para obtener fundaciones recientes
    this.volunteerService.getRecentOrganizations(volunteerId).subscribe(
      (orgData: any[]) => {
        if (!orgData || orgData.length === 0) {
          console.warn('No se encontraron fundaciones.');
          return;
        }

        // Tomar solo las tres últimas fundaciones
        const recentOrganizations = orgData.slice(-3);

        // Para cada organización, obtener los detalles de la fundación y el usuario responsable
        recentOrganizations.forEach((org) => {
          if (org.userId) {
            // Obtener detalles de la fundación
            this.organizationService.getOrganizationDetails(org.userId).subscribe(
              (orgDetails) => {
                org.organizationDetails = orgDetails;

                // Obtener los detalles del usuario responsable (imagen y correo)
                this.organizationService.getUserDetails(org.userId).subscribe(
                  (userDetails) => {
                    org.userDetails = userDetails; // Añadir los detalles del usuario (imagen y correo)
                  },
                  (error) => {
                    console.error('Error al obtener los detalles del usuario responsable', error);
                  }
                );
              },
              (error) => {
                console.error('Error al obtener detalles de la fundación', error);
              }
            );
          } else {
            console.warn('userId no definido para esta organización:', org);
          }
        });

        this.organizations = recentOrganizations;
      },
      (error) => {
        console.error('Error al obtener las fundaciones recientes', error);
      }
    );
  }

  // Generar el rango de años: 3 anteriores y 3 posteriores al año actual
  generateYearRange() {
    const currentYear = new Date().getFullYear();
    const startYear = currentYear - 3;
    const endYear = currentYear + 3;
    this.yearRange = Array.from({ length: endYear - startYear + 1 }, (_, i) => startYear + i);
  }

  loadChartData(year: number) {
    const volunteerId = Number(localStorage.getItem('volunteerId')); // Obtener el volunteerId desde localStorage
    this.volunteerService.getActivitiesByYear(volunteerId, year).subscribe(
      (activityData) => {
        const activitySeries = Object.values(activityData).map(val => Number(val));

        this.chartOptions.series = [
          {
            name: 'Actividades',
            data: activitySeries,
            color: "#FF5733"
          }
        ];
      },
      (error) => {
        console.error('Error al cargar los datos del gráfico', error);
      }
    );
  }

  onYearChange(event: any): void {
    this.selectedYear = event.target.value;
    this.loadChartData(this.selectedYear);
  }

  loadDashboardData() {
    this.loadChartData(this.selectedYear);

    this.volunteerService.getCompletedActivities(this.userId).subscribe(
      (data: number) => {
        this.completedActivities = data;
      },
      (error) => {
        console.error('Error al obtener las actividades completadas', error);
        this.completedActivities = 0;
      }
    );

    this.volunteerService.getAverageRating(this.userId).subscribe(
      (data: number | null) => {
        this.averageRating = data ?? 0;
      },
      (error) => {
        console.error('Error al obtener la puntuación promedio', error);
        this.averageRating = 0;
      }
    );

    this.volunteerService.getBeneficiaries(this.userId).subscribe(
      (data: number) => {
        this.beneficiariesImpacted = data;
      },
      (error) => {
        console.error('Error al obtener los beneficiarios impactados', error);
        this.beneficiariesImpacted = 0;
      }
    );
  }

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

  navigateToFundaciones() {
    this.router.navigate(['/misF']);  // Redireccionar a la ruta '/misF'
  }
}
