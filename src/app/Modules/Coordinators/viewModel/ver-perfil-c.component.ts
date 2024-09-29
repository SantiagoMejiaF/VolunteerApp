import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ver-perfil-c',
  templateUrl: '../view/ver-perfil-c.component.html',
  styleUrl: '../styles/ver-perfil-c.component.css'
})
export class VerPerfilCComponent {
  currentContent: string = 'content1';
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerId: number = 0;
  data = [
    {
      tittle: 'Limpieza de parque',
      city: 'Madrid',
      address: 'Calle Mayor 123',
      noVolunteers: 20,
      Status: 'Activo',
    },
    {
      tittle: 'Reforestación urbana',
      city: 'Barcelona',
      address: 'Avenida Diagonal 456',
      noVolunteers: 15,
      Status: 'Pendiente',
    },
    {
      tittle: 'Recogida de alimentos',
      city: 'Valencia',
      address: 'Calle de la Paz 789',
      noVolunteers: 30,
      Status: 'Completado',
    },
    {
      tittle: 'Ayuda en comedor social',
      city: 'Sevilla',
      address: 'Plaza Nueva 10',
      noVolunteers: 25,
      Status: 'Activo',
    },
    {
      tittle: 'Reciclaje comunitario',
      city: 'Bilbao',
      address: 'Gran Vía 14',
      noVolunteers: 12,
      Status: 'Aplazado',
    }
  ];
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
  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  myForm: FormGroup;
  volunteerData: any;
  disabled = false;
  ShowFilter = false;
  limitSelection = false;
  

  constructor(
    private volunteerService: VolunteerService,
    private router: Router 
   
  ) {
    
    this.volunteerData = {
      userId: 0,
      personalInformation: {
        identificationCard: '',
        phoneNumber: '',
        address: '',
        birthDate: ''
      },
      volunteeringInformation: {
        availabilityDaysList: [],
        interestsList: [],
        skillsList: []
      },
      emergencyInformation: {
        emergencyContactFirstName: '',
        emergencyContactLastName: '',
        emergencyContactRelationship: '',
        emergencyContactPhone: '',
        emergencyContactEmail: ''
      }
    };
  }

  ngOnInit() {
   

    
    this.loadVolunteerData();
   

    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    this.firstName = userInfo.firstName;
    this.lastName = userInfo.lastName;
    this.email = userInfo.email;
  }

 
  

  loadVolunteerData() {
    const userId = JSON.parse(localStorage.getItem('userInfo')!).id;
    this.volunteerService.getVolunteerDetails(userId).subscribe(
      (volunteerDetails) => {
        this.volunteerData = volunteerDetails;
        this.volunteerId = volunteerDetails.id;  // Asignar el ID del voluntario
       
      },
      (error) => {
        console.error('Error loading volunteer details:', error);
      }
    );
  }


 

  
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
  getStatusClass(status: string): string {
    switch (status) {
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
  
}


