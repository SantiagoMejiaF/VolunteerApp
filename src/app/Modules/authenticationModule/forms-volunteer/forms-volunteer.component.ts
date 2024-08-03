import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

interface City {
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
  disabled = false;
  ShowFilter = false;
  limitSelection = false;
  cities: City[] = [];
  selectedItems: City[] = [];
  dropdownSettings: any = {};
  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.showTab(this.currentTab);

    document
      .getElementById('prevBtn')
      ?.addEventListener('click', () => this.nextPrev(-1));
    document
      .getElementById('nextBtn')
      ?.addEventListener('click', () => this.nextPrev(1));

    this.cities = [
      { item_id: 1, item_text: 'New Delhi' },
      { item_id: 2, item_text: 'Mumbai' },
      { item_id: 3, item_text: 'Bangalore' },
      { item_id: 4, item_text: 'Pune' },
      { item_id: 5, item_text: 'Chennai' },
      { item_id: 6, item_text: 'Navsari' },
    ];

    console.log('Cities:', this.cities);
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: this.ShowFilter,
    };

    console.log('Dropdown Settings:', this.dropdownSettings);
    this.myForm = this.fb.group({
      city: [this.selectedItems],
    });
  }

  showTab(n: number) {
    const tabs = document.getElementsByClassName(
      'tab'
    ) as HTMLCollectionOf<HTMLElement>;
    tabs[n].style.display = 'block';

    if (n === 0) {
      document.getElementById('prevBtn')!.style.display = 'none';
    } else {
      document.getElementById('prevBtn')!.style.display = 'inline';
    }

    if (n === tabs.length - 1) {
      document.getElementById('nextBtn')!.innerHTML =
        '<i class="fa fa-angle-double-right"></i>';
    } else {
      document.getElementById('nextBtn')!.innerHTML =
        '<i class="fa fa-angle-double-right"></i>';
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
