import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { VolunteerService } from '../model/services/volunteer.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ver-perfil-v',
  templateUrl: '../view/ver-perfil-v.component.html',
  styleUrl: '../styles/ver-perfil-v.component.css'
})
export class VerPerfilVComponent implements OnInit {
  currentContent: string = 'content1';
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerId: number = 0;

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
  
  
}

