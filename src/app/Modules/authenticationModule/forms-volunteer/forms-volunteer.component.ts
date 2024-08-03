import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

interface Elements {
  item_id: number;
  item_text: string;
}

@Component({
  selector: 'app-forms-volunteer',
  templateUrl: './forms-volunteer.component.html',
  styleUrl: './forms-volunteer.component.css',
})
export class FormsVolunteerComponent {
  currentTab = 0;
  myForm: FormGroup;
  myForm2: FormGroup;
  myForm3: FormGroup;
  disabled = false;
  ShowFilter = false;
  limitSelection = false;
  skills: Elements[] = [];
  dropdownSettings: any = {};
  days: Elements[] = [];
  dropdownSettings3: any = {};
  intereses: Elements[] = [];
  dropdownSettings2: any = {};
  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.showTab(this.currentTab);

    document
      .getElementById('prevBtn')
      ?.addEventListener('click', () => this.nextPrev(-1));
    document
      .getElementById('nextBtn')
      ?.addEventListener('click', () => this.nextPrev(1));

    this.skills = [
      { item_id: 1, item_text: 'Angular' },
      { item_id: 2, item_text: 'Correr' },
      { item_id: 3, item_text: 'Trabajo en grupo' },
      { item_id: 4, item_text: 'Liderazgo' },
      { item_id: 5, item_text: 'Manualidades' },
      { item_id: 6, item_text: 'Cocina' },
    ];
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: this.ShowFilter,
    };
    this.intereses = [
      { item_id: 1, item_text: 'Arte' },
      { item_id: 2, item_text: 'Pedagogía' },
      { item_id: 3, item_text: 'Ambiente' },
    ];
    this.dropdownSettings2 = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: this.ShowFilter,
    };
    this.days = [
      { item_id: 1, item_text: 'Lunes' },
      { item_id: 2, item_text: 'Martes' },
      { item_id: 3, item_text: 'Miércoles' },
      { item_id: 4, item_text: 'Jueves' },
      { item_id: 5, item_text: 'Viernes' },
      { item_id: 6, item_text: 'Sábado' },
    ];
    this.dropdownSettings3 = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: this.ShowFilter,
    };

    this.myForm = this.fb.group({
      skills: ['']
    });
    this.myForm2 = this.fb.group({
      intereses: ['']
    });
    this.myForm3 = this.fb.group({
      days: ['']
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
      document.getElementById('nextBtn')!.innerHTML = '<i class="fa fa-angle-double-right"></i>';
    } else {
      document.getElementById('nextBtn')!.innerHTML = '<i class="fa fa-angle-double-right"></i>';
    }
  
    this.fixStepIndicator(n);
  }
  

  nextPrev(n: number) {
    const tabs = document.getElementsByClassName(
      'tab'
    ) as HTMLCollectionOf<HTMLElement>;
    if (n === 1 && !this.validateForm()) return;

    tabs[this.currentTab].style.display = 'none';
    this.currentTab += n;

    if (this.currentTab >= tabs.length) {
      document.getElementById('nextprevious')!.style.display = 'none';
      document.getElementById('all-steps')!.style.display = 'none';
      document.getElementById('register')!.style.display = 'none';
      document.getElementById('text-message')!.style.display = 'block';
      return;
    }

    this.showTab(this.currentTab);
  }

  validateForm(): boolean {
    const tabs = document.getElementsByClassName(
      'tab'
    ) as HTMLCollectionOf<HTMLElement>;
    const inputs = tabs[this.currentTab].getElementsByTagName('input');

    let valid = true;
    for (let i = 0; i < inputs.length; i++) {
      if (inputs[i].value === '') {
        inputs[i].className += ' invalid';
        valid = false;
      }
    }

    if (valid) {
      document.getElementsByClassName('step')[this.currentTab].className +=
        ' finish';
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
  toogleShowFilter() {
    this.ShowFilter = !this.ShowFilter;
    this.dropdownSettings = Object.assign({}, this.dropdownSettings, {
      allowSearchFilter: this.ShowFilter,
    });
  }

  handleLimitSelection() {
    if (this.limitSelection) {
      this.dropdownSettings = Object.assign({}, this.dropdownSettings, {
        limitSelection: 2,
      });
    } else {
      this.dropdownSettings = Object.assign({}, this.dropdownSettings, {
        limitSelection: null,
      });
    }
  }
}
