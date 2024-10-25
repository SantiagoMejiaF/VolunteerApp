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
  timelineData = [
    {
      id: 1,
      title: 'Título de actividad 1',
      review: 'Reseña que se dio: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed porttitor gravida aliquam.',
      stars: 3,
      date: '13/01/2018, 13:05'
    },
    {
      id: 2,
      title: 'Título de actividad 2',
      review: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed porttitor gravida aliquam.',
      stars: 4,
      date: '15/02/2019, 14:10'
    },
    {
      id: 3,
      title: 'Título de actividad 3',
      review: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed porttitor gravida aliquam.',
      stars: 5,
      date: '18/03/2020, 16:20'
    },
    {
      id: 3,
      title: 'Título de actividad 3',
      review: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed porttitor gravida aliquam.',
      stars: 5,
      date: '18/03/2020, 16:20'
    },
    
  ];
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
    const imagenId = (validIndex % 6) + 1;
  
    // Navegar a la ruta con los parámetros calculados
    this.router.navigate(['/actividad', validIndex, `card${imagenId}.svg`]);
  }
  

  // Función para mostrar el mensaje de alerta
  unirse(index: number) {
    const validIndex = index ?? 1;
    const imagenId = (validIndex % 3) + 1;
    const btnClass = 'btn-outline-primary' + imagenId;
    this.router.navigate(['/actividad', validIndex, `card${imagenId}.jpg`]);
  }

  unirseF(event: Event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del enlace
    alert(`Se ha enviado tu solicitud de forma exitosa`);
  }
}
