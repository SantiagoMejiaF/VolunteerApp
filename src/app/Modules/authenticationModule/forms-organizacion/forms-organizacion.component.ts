import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { OrganizationService } from '../../../services/organization.service';
import { Organization } from '../../../models/organization.model';

@Component({
  selector: 'app-forms-organizacion',
  templateUrl: './forms-organizacion.component.html',
  styleUrls: ['./forms-organizacion.component.css'],
})
export class FormsOrganizacionComponent implements OnInit {
  currentTab = 0;
  myForm: FormGroup;
  organizationData: Organization;

  constructor(private fb: FormBuilder, private organizationService: OrganizationService) {
    this.myForm = this.fb.group({
      foundationName: [''],
      nit: [''],
      website: [''],
      phoneNumber: [''],
      email: [''],
      address: ['']
    });

    this.organizationData = {
      userId: 0,
      institutionalInformation: {
        nit: '',
        foundationName: '',
        website: ''
      },
      contactInformation: {
        phoneNumber: '',
        email: '',
        address: ''
      }
    };
  }

  ngOnInit() {
    this.showTab(this.currentTab);
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
      this.setOrganizationData();
    }

    this.showTab(this.currentTab);
  }

  validateForm(): boolean {
    const tabs = document.getElementsByClassName('tab') as HTMLCollectionOf<HTMLElement>;
    const inputs = tabs[this.currentTab].getElementsByTagName('input');

    let valid = true;
    for (let i = 0; i < inputs.length; i++) {
      if (inputs[i].value === '') {
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

  setOrganizationData() {
    this.organizationData = {
      userId: JSON.parse(localStorage.getItem('userInfo')!).id,
      institutionalInformation: {
        nit: this.myForm.get('nit')?.value,
        foundationName: this.myForm.get('foundationName')?.value,
        website: this.myForm.get('website')?.value,
      },
      contactInformation: {
        phoneNumber: this.myForm.get('phoneNumber')?.value,
        email: this.myForm.get('email')?.value,
        address: this.myForm.get('address')?.value,
      }
    };
    console.log('Datos que se enviarán:', JSON.stringify(this.organizationData, null, 2));
  }

  onSubmit() {
    this.organizationService.createOrganization(this.organizationData).subscribe(
      (response) => {
        console.log('Organization created successfully:', response);
        // Mostrar mensaje de éxito
      },
      (error) => {
        console.error('Error creating organization:', error);
        // Mostrar mensaje de error
      }
    );
  }
}
