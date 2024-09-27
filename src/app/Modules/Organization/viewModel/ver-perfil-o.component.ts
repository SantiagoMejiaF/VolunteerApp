import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-ver-perfil-o',
  templateUrl: '../view/ver-perfil-o.component.html',
  styleUrl: '../styles/ver-perfil-o.component.css'
})
export class VerPerfilOComponent {
  currentContent: string = 'content1';
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerId: number = 0;
  organizationData: any;

  actividades = [
    {
      nombre: 'Actividad 1',
      descripcion: 'Descripción: Actividad emocionante para aprender Angular.',
      fecha: '25 de Septiembre, 2024',
      cuposRestantes: 5,
      cuposTotales: 20
    },
    {
      nombre: 'Actividad 2',
      descripcion: 'Descripción: Taller práctico de desarrollo web.',
      fecha: '26 de Septiembre, 2024',
      cuposRestantes: 10,
      cuposTotales: 30
    },
    {
      nombre: 'Actividad 3',
      descripcion: 'Descripción: Sesión sobre buenas prácticas de desarrollo.',
      fecha: '27 de Septiembre, 2024',
      cuposRestantes: 15,
      cuposTotales: 25
    }
  ];

  constructor(private router: Router ) {
    

    this.organizationData = {
      userId: 0,
      responsiblePersonId: '',
      responsiblePersonPhoneNumber: '',
      organizationName: '',
      organizationTypeEnum: '',
      sectorTypeEnum: '',
      volunteeringTypeEnum: '',
      nit: '',
      address: ''
    };
  }
  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  getStars(rating: number): string[] {
    const totalStars = 5;
    return Array(totalStars).fill('gray').map((_, index) => index < rating ? 'gold' : 'gray');
  }

  verDetalles(index: number | undefined) {
    // Asignar 1 por defecto si el index es undefined o null
    const validIndex = index ?? 1;
  
    // Asegurarse de que la imagenId esté en el rango adecuado (1-3)
    const imagenId = (validIndex % 3) + 1;
    const btnClass = 'btn-outline-primary' + imagenId;
  
    // Navegar a la ruta con los parámetros calculados
    this.router.navigate(['/actividad', validIndex, `card${imagenId}.jpg`, btnClass]);
  }
  

  // Función para mostrar el mensaje de alerta
  unirse(index: number) {
    const validIndex = index ?? 1;
    const imagenId = (validIndex % 3) + 1;
    const btnClass = 'btn-outline-primary' + imagenId;
    this.router.navigate(['/actividad', validIndex, `card${imagenId}.jpg`, btnClass]);
  }
}
