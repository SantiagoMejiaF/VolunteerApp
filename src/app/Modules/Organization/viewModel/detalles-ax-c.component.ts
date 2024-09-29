import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-detalles-ax-c',
  templateUrl: '../view/detalles-ax-c.component.html',
  styleUrls: ['../styles/detalles-ax-c.component.css'] // Ojo con la 's' en styles.
})
export class DetallesAxCComponent implements OnInit {

  // MARTIN NECESITO EL STATUS DE LA ACTIVIDAD PARA COLOCAR LA IMAGEN SEGÚN ESO, TAMBIÉN 
  //NECESITO EL ROL EN ESTA PANTALLA Y LLENAR LOS WR CON LOS SERVICIOS
  imagen: string = '';
  actividadId: number = 0;
  status: string = '';
  role: string = '';
  qrCodeUrlInicial: string = '';
  qrCodeUrlFinal: string = '';

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

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit() {

    // MARTIN NECESITO EL STATUS PARA COLOCAR LA IMAGEN SEGÚN ESO
    this.status = 'Activo'; 
    this.qrCodeUrlInicial = 'assets/img/LOGO_VOLUNTEERE.png';
    this.qrCodeUrlFinal = 'assets/img/LOGO_VOLUNTEERE.png';
    this.setImagenByStatus(this.status);
    const roleStr = localStorage.getItem('role');
    if (roleStr) {
      this.role = roleStr;
    }
  }

  setImagenByStatus(status: string) {
    switch (status) {
      case 'Activo':
        this.imagen = 'assets/img/card4.svg';
        break;
      case 'Pendiente':
        this.imagen = 'assets/img/card6.svg';
        break;
      case 'Completado':
        this.imagen = 'assets/img/card5.svg';
        break;
      case 'Aplazado':
        this.imagen = 'assets/img/card3.svg';
        break;
      default:
        this.imagen = 'assets/img/default.svg'; // Imagen por defecto
        break;
    }
  }
  getStatusClass(): string {
    
    switch (this.status) {
      case 'Activo':
        return 'status-activo';
      case 'Pendiente':
        return 'status-pendiente';
      case 'Completado':
        return 'status-completado';
      case 'Aplazado':
        return 'status-aplazado';
      default:
        return '';
    }
  }
  downloadQRInicial(): void {
    this.http.get(this.qrCodeUrlInicial, { responseType: 'blob' }).subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'qrInicial.png';  // Nombre del archivo que se descargará
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);  // Limpiar el objeto URL después de descargar
    });
  }
  downloadQRFinal(): void {
    this.http.get(this.qrCodeUrlInicial, { responseType: 'blob' }).subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'qrFinal.png';  // Nombre del archivo que se descargará
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);  // Limpiar el objeto URL después de descargar
    });
  }
}
