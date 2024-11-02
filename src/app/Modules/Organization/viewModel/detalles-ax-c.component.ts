import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MissionsService } from '../../Misiones/model/services/mission.service';
import { ActivityService } from '../../Coordinators/model/services/activity.service';

@Component({
  selector: 'app-detalles-ax-c',
  templateUrl: '../view/detalles-ax-c.component.html',
  styleUrls: ['../styles/detalles-ax-c.component.css']
})
export class DetallesAxCComponent implements OnInit {
  imagen: string = '';
  actividadId: number = 0;
  status: string = '';
  origen: string;
  role: string = '';
  qrCodeUrlInicial: string = '';
  qrCodeUrlFinal: string = '';
  activityDetails: any;
  data: any[] = [];

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router, private missionsService: MissionsService,
    private activityService: ActivityService
  ) {
    this.route.queryParams.subscribe(params => {
      this.origen = params['from'];
    });
  }

  ngOnInit() {
    // Obtener la actividad seleccionada del localStorage
    const selectedActivity = localStorage.getItem('selectedActivity');
    if (selectedActivity) {
      this.activityDetails = JSON.parse(selectedActivity);
      this.actividadId = this.activityDetails.id;
      this.status = this.activityDetails.status; // Obtener el status de la actividad
      this.setImagenByStatus(this.status); // Configurar la imagen según el estado
      this.loadVolunteers();

      this.missionsService.getQRInicial(this.actividadId).subscribe(
        (response) => {
          // Crear un objeto URL para la imagen del QR
          const url = window.URL.createObjectURL(response);
          this.qrCodeUrlInicial = url;
        },
        (error) => {
          console.error('Error al obtener el QR inicial', error);
        }
      );

      this.missionsService.getQRFinal(this.actividadId).subscribe(
        (response) => {
          const url = window.URL.createObjectURL(response);
          this.qrCodeUrlFinal = url;
        },
        (error) => {
          console.error('Error al obtener el QR final', error);
        }
      );

    } else {
      console.error('No hay actividad seleccionada en localStorage');
    }

    const roleStr = localStorage.getItem('role');
    if (roleStr) {
      this.role = roleStr;
    }
  }

  loadVolunteers(): void {
    this.activityService.getVolunteersByActivity(this.actividadId).subscribe(
      (volunteers) => {
        this.data = volunteers; // Almacena la lista de voluntarios en 'data'
      },
      (error) => console.error('Error al cargar los voluntarios inscritos:', error)
    );
  }

  setImagenByStatus(status: string) {
    switch (status) {
      case 'DISPONIBLE':
        this.imagen = 'assets/img/card4.svg';
        break;
      case 'PENDIENTE':
        this.imagen = 'assets/img/card6.svg';
        break;
      case 'COMPLETADA':
        this.imagen = 'assets/img/card5.svg';
        break;
      case 'CANCELADA':
        this.imagen = 'assets/img/card3.svg';
        break;
      default:
        this.imagen = 'assets/img/default.svg'; // Imagen por defecto
        break;
    }
  }

  getStatusClass(): string {
    switch (this.status) {
      case 'DISPONIBLE':
        return 'status-activo';
      case 'PENDIENTE':
        return 'status-pendiente';
      case 'COMPLETADA':
        return 'status-completado';
      case 'CANCELADA':
        return 'status-aplazado';
      default:
        return '';
    }
  }

  downloadQRInicial(): void {
    const a = document.createElement('a');
    a.href = this.qrCodeUrlInicial;
    a.download = 'qr_inicial.png'; // Nombre del archivo que se descargará
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }


  downloadQRFinal(): void {
    const a = document.createElement('a');
    a.href = this.qrCodeUrlFinal;
    a.download = 'qr_final.png';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  }

  verDetalles() {
    this.router.navigate(['/verPerfilV'], { queryParams: { from: 'verDetallesAxC' } });
  }

  volver() {
    // Volver a los detalles de la fundación con el parámetro 'origen'
    if (this.origen === 'verPerfilC') {
      this.router.navigate(['/verPerfilC'], { queryParams: { from: this.origen } });
    } else {
      this.router.navigate(['/misAC']); // Navegar a Detalles de Fundación
    }
  }
}
