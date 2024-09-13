import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-edit-m',
  templateUrl: '../view/edit-m.component.html',
  styleUrl: '../../../styles/edit-m.component.css'
})
export class EditMComponent {
  @Output() cancel = new EventEmitter<void>();

  onCancel() {
    this.cancel.emit();
  }
}
