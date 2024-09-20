import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-edit-a',
  templateUrl: '../view/edit-a.component.html',
  styleUrl: '../styles/edit-a.component.css'
})
export class EditAComponent {
  currentStep: number = 1; 
  @Output() cancel = new EventEmitter<void>();

  onCancel() {
    this.cancel.emit();
  }
  nextStep() {
    this.currentStep++;
  }

 
  previousStep() {
    this.currentStep--;
  }
}
