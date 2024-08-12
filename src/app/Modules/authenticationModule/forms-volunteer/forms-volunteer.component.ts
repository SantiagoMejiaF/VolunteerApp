import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { VolunteerService } from '../../../services/volunteer.service';
import { Volunteer } from '../../../models/volunteer.model';
import { HttpClient } from '@angular/common/http';

interface Elements {
  item_id: number;
  item_text: string;
}

@Component({
  selector: 'app-forms-volunteer',
  templateUrl: './forms-volunteer.component.html',
  styleUrls: ['./forms-volunteer.component.css'],
})
export class FormsVolunteerComponent implements OnInit {
  currentTab = 0;
  myForm: FormGroup;
  volunteerData: Volunteer;
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

  constructor(private fb: FormBuilder, private volunteerService: VolunteerService, private http: HttpClient) {
    this.myForm = this.fb.group({
      dni: [''],
      cell: [''],
      address: [''],
      age: [''],
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
        age: 0
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
    this.showTab(this.currentTab);

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
      singleSelection: true,  // Permitir solo una selección
      idField: 'item_id',
      textField: 'item_text',
      allowSearchFilter: this.ShowFilter,
    };

    this.loadDropdownData();
    this.loadTerms();
  }
  loadTerms() {
    this.http.get('assets/textos/terminos-y-condiciones.txt', { responseType: 'text' })
      .subscribe(data => this.termsContent = data);
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

  showTab(n: number) {
    const tabs = document.getElementsByClassName('tab') as HTMLCollectionOf<HTMLElement>;
    tabs[n].style.display = 'block';

    if (n === 0) {
      document.getElementById('prevBtn')!.style.display = 'none';
    } else {
      document.getElementById('prevBtn')!.style.display = 'inline';
    }

    if (n === tabs.length - 1) {
      document.getElementById('nextBtn')!.innerHTML = 'Confirmar';
    } else {
      document.getElementById('nextBtn')!.innerHTML = '<i class="fa fa-angle-double-right"></i>';
    }

    this.fixStepIndicator(n);
  }

  nextPrev(n: number) {
    const tabs = document.getElementsByClassName('tab') as HTMLCollectionOf<HTMLElement>;
    if (n === 1 && !this.validateForm()) return;

    tabs[this.currentTab].style.display = 'none';
    this.currentTab += n;

    if (this.currentTab >= tabs.length) {
      document.getElementById('nextprevious')!.style.display = 'none';
      document.getElementById('all-steps')!.style.display = 'none';
      document.getElementById('register')!.style.display = 'none';
      document.getElementById('text-message')!.style.display = 'block';
      document.getElementById('thanksBackground')!.style.display = 'block';
      return;
    }

    if (this.currentTab === tabs.length - 1) {
      this.setVolunteerData();
    }

    this.showTab(this.currentTab);
  }

  validateForm(): boolean {
    const tabs = document.getElementsByClassName('tab') as HTMLCollectionOf<HTMLElement>;
    const inputs = tabs[this.currentTab].getElementsByTagName('input');

    let valid = true;
    for (let i = 0; i < inputs.length; i++) {
      if (inputs[i].type === 'checkbox' && !inputs[i].checked) {
        inputs[i].className += ' invalid';
        valid = false;
      } else if (inputs[i].value === '') {
        inputs[i].className += ' invalid';
        valid = false;
      }
    }

    if (valid) {
      document.getElementsByClassName('step')[this.currentTab].className += ' finish';
    }

    return valid;
  }

  fixStepIndicator(n: number) {
    const steps = document.getElementsByClassName('step');
    for (let i = 0; i < steps.length; i++) {
      steps[i].className = steps[i].className.replace(' active', '');
    }
    steps[n].className += ' active';
  }

  onItemSelect(item: any) {
    console.log('onItemSelect', item);
  }

  onSelectAll(items: any) {
    console.log('onSelectAll', items);
  }

  setVolunteerData() {
    this.volunteerData = {
      userId: JSON.parse(localStorage.getItem('userInfo')!).id,
      personalInformation: {
        identificationCard: this.myForm.get('dni')?.value,
        phoneNumber: this.myForm.get('cell')?.value,
        address: this.myForm.get('address')?.value,
        age: this.myForm.get('age')?.value
      },
      volunteeringInformation: {
        availabilityDaysList: this.myForm.get('days')?.value.map((item: Elements) => item.item_text),
        interestsList: this.myForm.get('intereses')?.value.map((item: Elements) => item.item_text),
        skillsList: this.myForm.get('skills')?.value.map((item: Elements) => item.item_text),
      },
      emergencyInformation: {
        emergencyContactFirstName: this.myForm.get('emergencyContact1Name')?.value,
        emergencyContactLastName: this.myForm.get('emergencyContact1Surname')?.value,
        emergencyContactRelationship: this.myForm.get('emergencyContact1Relation')?.value[0].item_text,
        emergencyContactPhone: this.myForm.get('emergencyContact1Phone')?.value,
        emergencyContactEmail: this.myForm.get('emergencyContact1Email')?.value,
      }
    };
    console.log('Datos que se enviarán:', JSON.stringify(this.volunteerData, null, 2));
  }

  onSubmit() {
    this.volunteerService.createVolunteer(this.volunteerData).subscribe(
      (response) => {
        console.log('Volunteer created successfully:', response);
        // Mostrar mensaje de éxito
      },
      (error) => {
        console.error('Error creating volunteer:', error);
        // Mostrar mensaje de error
      }
    );
  }
}
