import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerService } from '../model/services/volunteer.service';

@Component({
  selector: 'app-mis-fundaciones',
  templateUrl: '../view/mis-fundaciones.component.html',
  styleUrls: ['../styles/mis-fundaciones.component.css']
})
export class MisFundacionesComponent implements OnInit {
  organizaciones: any[] = [];
  filteredOrganizations: any[] = [];
  searchText: string = ''; // Para almacenar el texto de búsqueda
  imagenes = ['assets/img/card1.svg', 'assets/img/card2.svg', 'assets/img/card3.svg', 'assets/img/card4.svg', 'assets/img/card5.svg', 'assets/img/card6.svg'];

  constructor(private volunteerService: VolunteerService, private router: Router) { }

  ngOnInit(): void {
    // Obtener el volunteerId desde localStorage
    const volunteerId = localStorage.getItem('volunteerId');

    if (volunteerId) {
      // Llamar al servicio para obtener las fundaciones
      this.volunteerService.getVolunteerFoundations(Number(volunteerId)).subscribe(
        (data) => {
          this.organizaciones = data.map((org) => ({
            id: org.organizationId,  // Aquí agregamos el organizationId
            titulo: org.name,
            descripcion: org.description,
            voluntarios: org.authorizedVolunteersCount,
            etiquetas: [org.organizationType, org.sector],
            photoUrl: org.photoUrl
          }));
          // Inicializa el arreglo filtrado con todas las organizaciones
          this.filteredOrganizations = this.organizaciones;
        },
        (error) => {
          console.error('Error al cargar las fundaciones', error);
        }
      );
    }
  }


  // Función para obtener la imagen de fondo correspondiente, repitiendo cada 3
  getImage(index: number): string {
    return this.imagenes[index % this.imagenes.length];
  }

  // Función para asignar el color de fondo de las etiquetas
  getBackgroundColor(index: number): string {
    switch ((index % 3) + 1) {
      case 1:
        return '#dfdfdf'; // Color para el caso 1
      case 2:
        return '#dfdfdf'; // Color para el caso 2
      case 3:
        return '#dfdfdf'; // Color para el caso 3
      default:
        return '#dfdfdf'; // Color por defecto
    }
  }

  // Filtrar organizaciones basado en el texto de búsqueda
  filterOrganizations(): void {
    if (!this.searchText) {
      // Si no hay texto en el campo de búsqueda, mostrar todas las organizaciones
      this.filteredOrganizations = this.organizaciones;
    } else {
      // Filtrar por título o descripción
      const searchLower = this.searchText.toLowerCase();
      this.filteredOrganizations = this.organizaciones.filter(org =>
        org.titulo.toLowerCase().includes(searchLower) ||
        (org.descripcion && org.descripcion.toLowerCase().includes(searchLower))
      );
    }
  }

  verDetalles(organizacion: any) {
    // Almacena toda la información de la organización en localStorage
    localStorage.setItem('SelectedOrganization', JSON.stringify(organizacion));

    // Navega a la pantalla de verPerfilO
    this.router.navigate(['/verPerfilO'], { queryParams: { id: organizacion.id, from: 'misF' } });
  }
}