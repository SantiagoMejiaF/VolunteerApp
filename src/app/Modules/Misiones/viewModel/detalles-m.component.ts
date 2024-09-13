import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-detalles-m',
  templateUrl: '../view/detalles-m.component.html',
  styleUrl: '../../../styles/detalles-m.component.css'
})
export class DetallesMComponent {
  selectedSection: string = 'descripcion'; // Por defecto, la sección de Descripción está activa
  currentContent: string = 'content1';
  isEditing = false;
    // Datos de ejemplo (puedes reemplazar estos valores por los tuyos)
    tipoMision = 'Educación';
    fechaInicio = '1995-07-14';
    fechaFin = '1995-07-24';
    departamento = 'Cucuta';
    descripcion = 'Lorem Ipsum es simplemente el texto de relleno...';
  // Función para cambiar la sección activa
  selectSection(section: string) {
    this.selectedSection = section;
  }

  showContent(contentId: string) {
    this.currentContent = contentId;
  }
  constructor(private router: Router) { }

  
  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  save() {
    // Aquí puedes manejar la lógica de guardar los cambios
    this.isEditing = false;
  }
}
