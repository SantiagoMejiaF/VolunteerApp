import { Component, OnInit, ViewChild } from '@angular/core';
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
import { OrganizationService } from '../model/services/organization.service';
import { MissionsService } from '../../Misiones/model/services/mission.service';
import { Router } from '@angular/router';

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
  responsive: ApexResponsive[];
};

@Component({
  selector: 'app-dashboard-organization',
  templateUrl: '../view/dashboard-organization.component.html',
  styleUrls: ['../styles/dashboard-organization.component.css']
})
export class DashboardOrganizationComponent implements OnInit {
  public completedMissions: number = 0;
  public totalBeneficiaries: number = 0;
  public volunteerAvailability: any = {};
  public skillCounts: any = {};
  public calendarDays: any[] = [];
  public selectedDate: Date;
  public currentYear: number;
  public currentMonth: number;
  public months: string[] = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
  public data: any[] = [];
  public volunteersCount: number = 0;
  public activities: any[] = [];

  @ViewChild('chart') chart: ChartComponent;
  public chartOptions: ChartOptions;
  public chartOptions2: ChartOptions2;
  public chartOptions3: ChartOptions3;

  constructor(private organizationService: OrganizationService, private missionsService: MissionsService, private router: Router) {
    this.chartOptions = {
      series: [
        {
          name: 'Actividades',
          data: [],
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
      }
    };

    this.chartOptions2 = {
      series: [],
      chart: {
        height: 350,
        type: 'treemap',
      },
      title: {
      },

      dataLabels: {
        enabled: true,
      },
      plotOptions: {
        treemap: {
          enableShades: true,
          distributed: false,
        },
      },
      legend: {
        show: false,
      },
    };

    this.chartOptions3 = {
      series: [],
      chart: {
        height: 390,
        type: 'radialBar',
      },
      plotOptions: {
        radialBar: {
          offsetY: 0,
          startAngle: 0,
          endAngle: 270,
          hollow: {
            margin: 5,
            size: '30%',
            background: 'transparent',
          },
          dataLabels: {
            name: {
              show: true,
              fontSize: '14px',
            },
            value: {
              show: true,
              fontSize: '16px',
            },
          },
        },
      },
      colors: ["#1ab7ea", "#0084ff", "#39539E", "#0077B5", "#306e86", "#45C4B0", "#007566"],
      labels: ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"],
      legend: {
        show: true,
        floating: true,
        fontSize: '14px',
        position: 'left',
        offsetX: 80,
        offsetY: -20,
        labels: {
          useSeriesColors: true,
        },
        formatter: function (seriesName, opts) {
          return seriesName + ":  " + opts.w.globals.series[opts.seriesIndex];
        },
        itemMargin: {
          horizontal: 3,
        },
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              height: 300,
            },
            legend: {
              show: false,
            },
          },
        },
      ],
    };
  }

  // Actualizar la gráfica de actividades con los datos recibidos
  updateActivityChart(data: any) {
    this.chartOptions.series = [
      {
        name: 'Actividades Completadas',
        data: [
          data['ENERO'], data['FEBRERO'], data['MARZO'], data['ABRIL'], data['MAYO'],
          data['JUNIO'], data['JULIO'], data['AGOSTO'], data['SEPTIEMBRE'],
          data['OCTUBRE'], data['NOVIEMBRE'], data['DICIEMBRE']
        ],
        color: "#FF5733"
      }
    ];
  }

  // Obtener el rango de años para mostrar en el dropdown
  getYearRange(): number[] {
    const currentYear = new Date().getFullYear();
    const range = [];
    for (let i = currentYear - 3; i <= currentYear + 3; i++) {
      range.push(i);
    }
    return range;
  }

  // Cambiar el año seleccionado y cargar las actividades correspondientes
  changeYear(year: number) {
    const orgId = localStorage.getItem('OrgId'); // Obtener el OrgId desde localStorage
    if (orgId) {
      this.loadActivitiesByYear(+orgId, year);
    }
  }

  // Cargar actividades por año
  loadActivitiesByYear(orgId: number, year: number) {
    if (!year || isNaN(year)) {
      console.error('El valor del año no es válido:', year);
      return;
    }

    // Llamada al servicio para obtener las actividades por año
    this.organizationService.getActivitiesCountByYear(orgId, year).subscribe(
      data => {
        this.updateActivityChart(data);  // Actualizar la gráfica con los datos obtenidos
      },
      error => {
        console.error('Error al obtener los datos del gráfico', error);
      }
    );
  }

  loadRecentVolunteers() {
    const orgId = localStorage.getItem('OrgId');
    if (orgId) {
      this.organizationService.getRecentAcceptedVolunteers(+orgId).subscribe(recentVolunteers => {
        recentVolunteers.forEach(volunteer => {
          this.organizationService.getUserDetails(volunteer.userId).subscribe(userDetails => {
            this.data.push({
              id: volunteer.id,
              identificationCard: volunteer.personalInformation.identificationCard,
              firstName: userDetails.firstName,
              lastName: userDetails.lastName,
              email: userDetails.email,
              image: userDetails.image || 'assets/img/user2.png',
            });
          });
        });
      }, error => {
        console.error('Error al cargar voluntarios recientes', error);
      });
    } else {
      console.error('No se encontró el OrgId en localStorage');
    }
  }

  navigateToVolunteers() {
    this.router.navigate(['/verVoluntarios']);
  }

  // Inicializar el componente y cargar datos iniciales
  ngOnInit(): void {
    const orgId = localStorage.getItem('OrgId'); // Obtener OrgId del localStorage
    const today = new Date();
    this.currentYear = today.getFullYear();

    if (orgId) {
      // Cargar actividades del año actual
      this.loadActivitiesByYear(+orgId, this.currentYear);
      this.loadProgrammedActivities();
      this.loadRecentVolunteers();

      // Cargar otros datos relevantes
      this.organizationService.getCompletedMissionsCount(+orgId).subscribe(data => {
        this.completedMissions = data;
      });

      this.organizationService.getTotalBeneficiariesImpacted(+orgId).subscribe(data => {
        this.totalBeneficiaries = data;
      });

      this.organizationService.getAcceptedVolunteersCount(+orgId).subscribe(data => {
        this.volunteersCount = data;
      });

      this.organizationService.getVolunteerAvailabilityCount(+orgId).subscribe(data => {
        this.volunteerAvailability = data;
        this.updateVolunteerAvailabilityChart();
      });

      this.organizationService.getSkillCounts(+orgId).subscribe(data => {
        this.skillCounts = data;
        this.updateSkillChart();
      });
    } else {
      console.error('No se encontró el OrgId en localStorage');
    }

    this.currentMonth = today.getMonth();
    this.selectedDate = today;
    this.generateCalendar();
  }

  isSelected(day: Date): boolean {
    return this.selectedDate && day && day.toDateString() === this.selectedDate.toDateString();
  }
  loadProgrammedActivities() {
    const orgId = Number(localStorage.getItem('OrgId')); // Obtener OrgId del localStorage
    if (!orgId) {
      console.error('No se encontró OrgId en localStorage.');
      return;
    }

    this.organizationService.getActivitiesByOrganization(orgId).subscribe(
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


  // Generar el calendario para mostrar los días
  generateCalendar() {
    // Tu lógica para generar el calendario
    const firstDay = new Date(this.currentYear, this.currentMonth, 1).getDay();
    const daysInMonth = new Date(this.currentYear, this.currentMonth + 1, 0).getDate();
    const calendarDays: any[] = [];
    let day = 1;
    for (let i = 0; i < 6; i++) {
      const week: any[] = [];
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
      calendarDays.push(week);
    }
    this.calendarDays = calendarDays;
  }

  // Navegar al mes anterior en el calendario
  prevMonth() {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }
    this.generateCalendar();
  }

  // Navegar al mes siguiente en el calendario
  nextMonth() {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }
    this.generateCalendar();
  }

  // Seleccionar un día del calendario
  selectDay(day: Date) {
    if (day) {
      this.selectedDate = day;
    }
  }

  // Verificar si el día seleccionado es el actual
  isToday(day: Date) {
    const today = new Date();
    return day && day.getDate() === today.getDate() && day.getMonth() === today.getMonth() && day.getFullYear() === today.getFullYear();
  }

  // Verificar si hay actividades en un día específico
  hasActivities(day: Date): boolean {
    return this.activities.some(activity => new Date(activity.date).toDateString() === day?.toDateString());
  }

  // Filtrar las actividades para el día seleccionado
  filteredActivities() {
    return this.activities.filter(activity => {
      return activity.date.toDateString() === this.selectedDate.toDateString();
    });
  }

  // Actualizar la gráfica de disponibilidad de voluntarios
  updateVolunteerAvailabilityChart() {
    this.chartOptions3.series = [
      this.volunteerAvailability['LUNES'],
      this.volunteerAvailability['MARTES'],
      this.volunteerAvailability['MIERCOLES'],
      this.volunteerAvailability['JUEVES'],
      this.volunteerAvailability['VIERNES'],
      this.volunteerAvailability['SABADO'],
      this.volunteerAvailability['DOMINGO'],
    ];
  }

  // Actualizar la gráfica de habilidades
  updateSkillChart() {
    this.chartOptions2.series = [
      {
        name: 'Habilidades',
        data: [
          { x: 'COMUNICACION', y: this.skillCounts['COMUNICACION'] },
          { x: 'ORGANIZACION', y: this.skillCounts['ORGANIZACION'] },
          { x: 'LIDERAZGO', y: this.skillCounts['LIDERAZGO'] },
          { x: 'TRABAJO_EN_EQUIPO', y: this.skillCounts['TRABAJO_EN_EQUIPO'] },
          { x: 'PRIMEROS_AUXILIOS', y: this.skillCounts['PRIMEROS_AUXILIOS'] },
          { x: 'EDUCACION', y: this.skillCounts['EDUCACION'] },
          { x: 'GESTION_DE_PROYECTOS', y: this.skillCounts['GESTION_DE_PROYECTOS'] },
          { x: 'MARKETING', y: this.skillCounts['MARKETING'] },
          { x: 'FINANZAS', y: this.skillCounts['FINANZAS'] },
          { x: 'TECNOLOGIA', y: this.skillCounts['TECNOLOGIA'] },
          { x: 'IDIOMAS', y: this.skillCounts['IDIOMAS'] },
        ],
      },
    ];
  }
}
