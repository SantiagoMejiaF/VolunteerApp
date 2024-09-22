import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OrganizationService } from '../model/services/organization.service';
import { Organization } from '../model/organization.model';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface Elements {
  item_id: string;
  item_text: string;
}

@Component({
  selector: 'app-perfil-o',
  templateUrl: '../view/perfil-o.component.html',
  styleUrls: ['../styles/perfil-o.component.css']
})
export class PerfilOComponent implements OnInit {
  currentContent: string = 'content1';
  showContent(contentId: string) {
    this.currentContent = contentId;
  }

  currentTab = 0;
  myForm: FormGroup;
  organizationData: any;
  organizationId: number = 0;
  email: string = ''; // Para almacenar el email del usuario
  termsContent: string | undefined;
  ShowFilter = false;
  typeOrganization: Elements[] = [];
  dropdownSettings: any = {};
  volunteeringType: Elements[] = [];
  dropdownSettings2: any = {};
  sectorOrganization: Elements[] = [];
  dropdownSettings3: any = {};

  constructor(private fb: FormBuilder, private organizationService: OrganizationService, private http: HttpClient, private router: Router) {
    this.myForm = this.fb.group({
      responsiblePersonId: [''],
      responsiblePersonPhoneNumber: [''],
      organizationName: [''],
      nit: [''],
      organizationTypeEnum: [''],
      volunteeringTypeEnum: [''],
      sectorTypeEnum: [''],
      address: [''],
      acceptTerms: [false]
    });

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

  ngOnInit() {
    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    this.email = userInfo.email; // Extraer el email del localStorage

    this.loadTerms();
    this.loadDropdownData();
    this.loadOrganizationData();

    this.dropdownSettings = {
      singleSelection: true,
      idField: 'item_id',
      textField: 'item_text',
      allowSearchFilter: this.ShowFilter,
    };

    this.dropdownSettings2 = { ...this.dropdownSettings };
    this.dropdownSettings3 = { ...this.dropdownSettings };
  }

  loadTerms() {
    this.http.get('assets/textos/terminos-y-condiciones.txt', { responseType: 'text' })
      .subscribe(data => this.termsContent = data);
  }

  loadDropdownData() {
    this.organizationService.getOrganizationTypes().subscribe(data => {
      this.typeOrganization = data.map((item: any) => ({ item_id: item, item_text: item }));
    });

    this.organizationService.getVolunteeringTypes().subscribe(data => {
      this.volunteeringType = data.map((item: any) => ({ item_id: item, item_text: item }));
    });

    this.organizationService.getSectorTypes().subscribe(data => {
      this.sectorOrganization = data.map((item: any) => ({ item_id: item, item_text: item }));
    });
  }

  loadOrganizationData() {
    const userId = JSON.parse(localStorage.getItem('userInfo')!).id;
    this.organizationService.getOrganizationDetails(userId).subscribe(
      (organizationDetails) => {
        this.organizationData = organizationDetails; // Asumiendo que es un array y tomando el primer elemento
        this.organizationId = organizationDetails.id; // Asigna el ID de la organización
        this.setFormValues();
      },
      (error) => {
        console.error('Error loading organization details:', error);
      }
    );
  }

  setFormValues() {
    this.myForm.patchValue({
      responsiblePersonId: this.organizationData.responsiblePersonId,
      responsiblePersonPhoneNumber: this.organizationData.responsiblePersonPhoneNumber,
      organizationName: this.organizationData.organizationName,
      nit: this.organizationData.nit,
      organizationTypeEnum: this.typeOrganization.filter(type => type.item_text === this.organizationData.organizationTypeEnum),
      sectorTypeEnum: this.sectorOrganization.filter(sector => sector.item_text === this.organizationData.sectorTypeEnum),
      volunteeringTypeEnum: this.volunteeringType.filter(type => type.item_text === this.organizationData.volunteeringTypeEnum),
      address: this.organizationData.address
    });
  }

  onItemSelect(item: any) {
    console.log('onItemSelect', item);
  }

  onSubmit() {
    this.organizationData = {
      responsiblePersonPhoneNumber: this.myForm.get('responsiblePersonPhoneNumber')?.value,
      organizationName: this.myForm.get('organizationName')?.value,
      organizationTypeEnum: this.myForm.get('organizationTypeEnum')?.value[0].item_text,
      sectorTypeEnum: this.myForm.get('sectorTypeEnum')?.value[0].item_text,
      volunteeringTypeEnum: this.myForm.get('volunteeringTypeEnum')?.value[0].item_text,
      address: this.myForm.get('address')?.value,
    };

    console.log('Updated Data:', this.organizationData);

    this.organizationService.updateOrganization(this.organizationId, this.organizationData).subscribe(
      (response) => {
        console.log('Organization data updated successfully:', response);
      },
      (error) => {
        console.error('Error updating organization data:', error);
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
