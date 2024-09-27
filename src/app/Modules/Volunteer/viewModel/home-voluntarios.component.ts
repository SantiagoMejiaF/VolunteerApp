import { Component } from '@angular/core';

@Component({
  selector: 'app-home-voluntarios',
  templateUrl: '../view/home-voluntarios.component.html',
  styleUrl: '../styles/home-voluntarios.component.css'
})
export class HomeVoluntariosComponent {
  organizaciones = [
    { titulo: 'Fundación Huellitas', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 9500, etiquetas: ['Empresa', 'Tecnología'] },
    { titulo: 'Fundación Amiguitos', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 7500, etiquetas: ['Salud', 'Cuidado'] },
    { titulo: 'Fundación Esperanza', descripcion: 'Descripción: Lorem ipsum dolor sit amet consectetur adipisicing elit. Eius soluta iure sapiente, at culpa officia? Ratione id dignissimos aperiam quam nam obcaecati', voluntarios: 12000, etiquetas: ['Educación', 'Desarrollo'] },
    
    // Puedes añadir más actividades según lo que necesites
  ];

  imagenes = ['assets/img/card1.jpg', 'assets/img/card2.svg', 'assets/img/card3.svg', 'assets/img/card4.svg', 'assets/img/card5.svg', 'assets/img/card6.svg'];

  // Función para obtener la imagen correspondiente, repitiendo cada 3
  getImage(index: number): string {
    return this.imagenes[index % this.imagenes.length];
  }

   // Función para asignar el color de fondo del botón "Detalles" según el índice
   getBackgroundColor(index: number): string {
    switch ((index % 3) + 1) {
      case 1:
        return '#1E1450'; // Para i=1
      case 2:
        return '#ED4B4B'; // Para i=2
      case 3:
        return '#EFC940'; // Para i=3
      default:
        return '#ffffff'; // Color por defecto (si es necesario)
    }
  }
}
