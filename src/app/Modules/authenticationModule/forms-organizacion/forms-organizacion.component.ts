import { Component } from '@angular/core';

@Component({
  selector: 'app-forms-organizacion',
  templateUrl: './forms-organizacion.component.html',
  styleUrl: './forms-organizacion.component.css',
})
export class FormsOrganizacionComponent {
  currentTab = 0;

  ngOnInit() {
    this.showTab(this.currentTab);

    document
      .getElementById('prevBtn')
      ?.addEventListener('click', () => this.nextPrev(-1));
    document
      .getElementById('nextBtn')
      ?.addEventListener('click', () => this.nextPrev(1));
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

}
