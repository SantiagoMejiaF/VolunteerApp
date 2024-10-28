import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerService } from '../model/services/volunteer.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-home-voluntarios',
  templateUrl: '../view/home-voluntarios.component.html',
  styleUrls: ['../styles/home-voluntarios.component.css']
})
export class HomeVoluntariosComponent implements OnInit {
  organizaciones: any[] = [];
  filteredOrganizations: any[] = [];
  searchText: string = '';
  category: string = 'TODOS'; // Categoría seleccionada
  imagenes = ['assets/img/card1.svg', 'assets/img/card2.svg', 'assets/img/card3.svg', 'assets/img/card4.svg', 'assets/img/card5.svg', 'assets/img/card6.svg'];
  sectors: string[] = ['TODOS']; // Lista de sectores para el filtro

  constructor(private volunteerService: VolunteerService, private organizationService: OrganizationService, private router: Router) { }

  ngOnInit(): void {
    const volunteerId = Number(localStorage.getItem('volunteerId') || 1); // Cambia por la obtención correcta del volunteerId

    // Obtener las fundaciones sugeridas
    this.volunteerService.getMatchingOrganizations(volunteerId, 3).subscribe(
      (data) => {
        this.organizaciones = data.map((org) => ({
          id: org.organization.id,
          titulo: org.organization.organizationName,
          descripcion: org.organization.description || 'No hay descripción disponible.',
          voluntarios: org.authorizedVolunteersCount,
          etiquetas: [org.organization.organizationTypeEnum, org.organization.sectorTypeEnum],
          photoUrl: org.photoUrl || 'assets/img/user3.png'
        }));
        this.filteredOrganizations = this.organizaciones;
      },
      (error) => {
        console.error('Error al cargar las fundaciones', error);
      }
    );

    // Obtener los sectores para el menú desplegable
    this.organizationService.getSectorTypes().subscribe(
      (sectors) => {
        this.sectors = ['TODOS', ...sectors]; // Agregar la opción "TODOS" como opción predeterminada
      },
      (error) => {
        console.error('Error al cargar los sectores', error);
      }
    );
  }

  // Función para obtener la imagen correspondiente, repitiendo cada 3
  getImage(index: number): string {
    return this.imagenes[index % this.imagenes.length];
  }

  // Función para filtrar las organizaciones según la búsqueda y categoría
  filterOrganizations(): void {
    const searchLower = this.searchText.toLowerCase();

    this.filteredOrganizations = this.organizaciones.filter(org => {
      const matchesSearch = org.titulo.toLowerCase().includes(searchLower) || org.descripcion.toLowerCase().includes(searchLower);
      const matchesCategory = this.category === 'TODOS' || org.etiquetas.includes(this.category);

      return matchesSearch && matchesCategory;
    });
  }

  verDetalles(organizationId: number) {
    this.router.navigate(['/verPerfilO'], { queryParams: { id: organizationId, from: 'homeV' } });
  }
}
