import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { VolunteerService } from '../model/services/volunteer.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface Elements {
  item_id: number;
  item_text: string;
}

@Component({
  selector: 'app-perfil',
  templateUrl: '../view/perfil.component.html',
  styleUrls: ['../styles/perfil.component.css']
})
export class PerfilComponent implements OnInit {
  currentContent: string = 'content1';
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  volunteerId: number = 0;
  showAlert = false;
  showAlert2 = false;
  image: any;

  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  myForm: FormGroup;
  volunteerData: any;
  disabled = false;
  ShowFilter = false;
  limitSelection = false;
  skills: Elements[] = [];
  dropdownSettings: any = {};
  days: Elements[] = [];
  dropdownSettings3: any = {};
  intereses: Elements[] = [];
  dropdownSettings2: any = {};
  relationships: Elements[] = [];
  dropdownSettings4: any = {};
  termsContent: string | undefined;
  timelineData: any[] = [];


  constructor(
    private fb: FormBuilder,
    private volunteerService: VolunteerService,
    private http: HttpClient,
    private router: Router
  ) {
    this.myForm = this.fb.group({
      dni: [''],
      cell: [''],
      address: [''],
      birthDate: [''],
      skills: [''],
      intereses: [''],
      days: [''],
      emergencyContact1Name: [''],
      emergencyContact1Surname: [''],
      emergencyContact1Relation: [''],
      emergencyContact1Phone: [''],
      emergencyContact1Email: [''],
      acceptTerms: [false]
    });

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
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: this.ShowFilter,
    };

    this.dropdownSettings2 = { ...this.dropdownSettings };
    this.dropdownSettings3 = { ...this.dropdownSettings };
    this.dropdownSettings4 = {
      singleSelection: true,
      idField: 'item_id',
      textField: 'item_text',
      allowSearchFilter: this.ShowFilter,
    };

    this.loadDropdownData();
    this.loadTerms();
    this.loadVolunteerData();
    this.loadVolunteerHistory();


    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    this.firstName = userInfo.firstName;
    this.lastName = userInfo.lastName;
    this.email = userInfo.email;
    this.image = userInfo.image;
  }

  loadTerms() {
    this.http.get('assets/textos/terminos-y-condiciones.txt', { responseType: 'text' })
      .subscribe(data => this.termsContent = data);
  }

  loadVolunteerHistory() {
    const volunteerId = localStorage.getItem('volunteerId');

    if (volunteerId) {
      this.volunteerService.getVolunteerHistory(+volunteerId).subscribe(
        (history) => {
          this.timelineData = history; // Asegúrate de que history sea un array
          console.log('Historial de voluntario cargado:', this.timelineData); // Verifica el contenido de timelineData
        },
        (error) => {
          console.error('Error loading volunteer history:', error);
          this.timelineData = []; // Resetea a un array vacío en caso de error
        }
      );
    } else {
      console.error('Volunteer ID no encontrado en localStorage');
      this.timelineData = []; // Resetea a un array vacío si no hay volunteerId
    }
  }

  loadDropdownData() {
    this.volunteerService.getSkills().subscribe(data => {
      this.skills = data.map((item, index) => ({ item_id: index + 1, item_text: item }));
    });

    this.volunteerService.getInterests().subscribe(data => {
      this.intereses = data.map((item, index) => ({ item_id: index + 1, item_text: item }));
    });

    this.volunteerService.getAvailabilities().subscribe(data => {
      this.days = data.map((item, index) => ({ item_id: index + 1, item_text: item }));
    });

    this.volunteerService.getRelationships().subscribe(data => {
      this.relationships = data.map((item, index) => ({ item_id: index + 1, item_text: item }));
    });
  }

  loadVolunteerData() {
    const userId = JSON.parse(localStorage.getItem('userInfo')!).id;
    this.volunteerService.getVolunteerDetails(userId).subscribe(
      (volunteerDetails) => {
        this.volunteerData = volunteerDetails;
        this.volunteerId = volunteerDetails.id;  // Asignar el ID del voluntario
        this.setFormValues();
      },
      (error) => {
        console.error('Error loading volunteer details:', error);
      }
    );
  }

  setFormValues() {
    this.myForm.patchValue({
      dni: this.volunteerData.personalInformation.identificationCard,
      cell: this.volunteerData.personalInformation.phoneNumber,
      address: this.volunteerData.personalInformation.address,
      birthDate: this.volunteerData.personalInformation.birthDate,
      skills: this.skills.filter(skill => this.volunteerData.volunteeringInformation.skillsList.includes(skill.item_text)),
      intereses: this.intereses.filter(interest => this.volunteerData.volunteeringInformation.interestsList.includes(interest.item_text)),
      days: this.days.filter(day => this.volunteerData.volunteeringInformation.availabilityDaysList.includes(day.item_text)),
      emergencyContact1Name: this.volunteerData.emergencyInformation.emergencyContactFirstName,
      emergencyContact1Surname: this.volunteerData.emergencyInformation.emergencyContactLastName,
      emergencyContact1Relation: this.relationships.filter(rel => rel.item_text === this.volunteerData.emergencyInformation.emergencyContactRelationship),
      emergencyContact1Phone: this.volunteerData.emergencyInformation.emergencyContactPhone,
      emergencyContact1Email: this.volunteerData.emergencyInformation.emergencyContactEmail
    });
  }

  onItemSelect(item: any) {
    console.log('onItemSelect', item);
  }

  onSelectAll(items: any) {
    console.log('onSelectAll', items);
  }

  onSubmit() {
    const updatedData = {
      personalUpdateInformationRequest: {
        phoneNumber: this.myForm.get('cell')?.value,
        address: this.myForm.get('address')?.value,
        birthDate: this.myForm.get('birthDate')?.value
      },
      volunteeringInformation: {
        availabilityDaysList: this.myForm.get('days')?.value.map((item: any) => item.item_text),
        interestsList: this.myForm.get('intereses')?.value.map((item: any) => item.item_text),
        skillsList: this.myForm.get('skills')?.value.map((item: any) => item.item_text)
      },
      emergencyInformation: {
        emergencyContactFirstName: this.myForm.get('emergencyContact1Name')?.value,
        emergencyContactLastName: this.myForm.get('emergencyContact1Surname')?.value,
        emergencyContactPhone: this.myForm.get('emergencyContact1Phone')?.value,
        emergencyContactEmail: this.myForm.get('emergencyContact1Email')?.value,
        emergencyContactRelationship: this.myForm.get('emergencyContact1Relation')?.value[0].item_text
      }
    };

    console.log('Updated Data:', updatedData); // Verifica los datos antes de enviarlos
    this.showAlert = true;
    setTimeout(() => (this.showAlert = false), 3000);
    this.volunteerService.updateVolunteer(this.volunteerId, updatedData).subscribe(
      (response) => {
        console.log('Volunteer data updated successfully:', response);
      },
      (error) => {
        console.error('Error updating volunteer data:', error);
        this.showAlert2 = true;
        setTimeout(() => (this.showAlert2 = false), 3000);
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
