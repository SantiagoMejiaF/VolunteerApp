import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-mis-fundaciones',
  templateUrl: '../view/mis-fundaciones.component.html',
  styleUrl: '../styles/mis-fundaciones.component.css'
})
export class MisFundacionesComponent {
  organizaciones = [
    { titulo: 'Fundación Huellitas', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 9500, etiquetas: ['Empresa', 'Tecnología'] },
    { titulo: 'Fundación Amiguitos', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 7500, etiquetas: ['Salud', 'Cuidado'] },
    { titulo: 'Fundación Esperanza', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 12000, etiquetas: ['Educación', 'Desarrollo'] },
    { titulo: 'Fundación Huellitas', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 9500, etiquetas: ['Empresa', 'Tecnología'] },
    { titulo: 'Fundación Amiguitos', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 7500, etiquetas: ['Salud', 'Cuidado'] },
    { titulo: 'Fundación Esperanza', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 12000, etiquetas: ['Educación', 'Desarrollo'] },
    
    // Puedes añadir más actividades según lo que necesites
  ];

  imagenes = ['assets/img/card1.svg', 'assets/img/card2.svg', 'assets/img/card3.svg', 'assets/img/card4.svg', 'assets/img/card5.svg', 'assets/img/card6.svg'];
  constructor(private router: Router) {}
  // Función para obtener la imagen correspondiente, repitiendo cada 3
  getImage(index: number): string {
    return this.imagenes[index % this.imagenes.length];
  }

   // Función para asignar el color de fondo del botón "Detalles" según el índice
   getBackgroundColor(index: number): string {
    switch ((index % 3) + 1) {
      case 1:
        return '#dfdfdf'; // Para i=1
      case 2:
        return '#dfdfdf'; // Para i=2
      case 3:
        return '#dfdfdf'; // Para i=3
      default:
        return '#dfdfdf'; // Color por defecto (si es necesario)
    }
  }

  verDetalles() {
    this.router.navigate(['/verPerfilO'], { queryParams: { from: 'misF' } });

  }
}
