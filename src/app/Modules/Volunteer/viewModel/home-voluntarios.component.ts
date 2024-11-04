import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VolunteerService } from '../model/services/volunteer.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';

@Component({
  selector: 'app-home-voluntarios',
  templateUrl: '../view/home-voluntarios.component.html',
  styleUrls: ['../styles/home-voluntarios.component.css'],
})
export class HomeVoluntariosComponent implements OnInit {
  organizacionesRecomendadas: any[] = []; // Organizaciones recomendadas
  todasLasOrganizaciones: any[] = []; // Todas las organizaciones
  filteredRecommendedOrganizations: any[] = []; // Organizaciones recomendadas filtradas
  filteredAllOrganizations: any[] = []; // Todas las organizaciones filtradas
  searchText: string = '';
  category: string = 'TODOS'; // Categoría seleccionada
  imagenes = [
    'assets/img/card1.svg',
    'assets/img/card2.svg',
    'assets/img/card3.svg',
    'assets/img/card4.svg',
    'assets/img/card5.svg',
    'assets/img/card6.svg',
  ];
  sectors: string[] = ['TODOS']; // Lista de sectores para el filtro
  // Variables para la paginación
  pageSize: number = 6; // Número de elementos por página
  currentPage: number = 1; // Página actual
  totalItems: number = 0; // Total de organizaciones
  totalPages: number = 0;
  Math = Math;
  constructor(
    private volunteerService: VolunteerService,
    private organizationService: OrganizationService,
    private router: Router
  ) { }
  onPageChange(page: number) {
    this.currentPage = page;
    this.paginateOrganizations();
  }

  // Función para paginar las organizaciones filtradas
  paginateOrganizations() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.filteredAllOrganizations = this.todasLasOrganizaciones.slice(start, end);
  }
  ngOnInit(): void {
    const volunteerId = Number(localStorage.getItem('volunteerId') || 1); // Obtiene el volunteerId correctamente

    // Obtener las fundaciones recomendadas
    this.volunteerService.getMatchingOrganizations(volunteerId, 3).subscribe(
      (data) => {
        this.organizacionesRecomendadas = data.map((org) => ({
          id: org.organization.id,
          titulo: org.organization.organizationName,
          descripcion:
            org.organization.description || 'No hay descripción disponible.',
          voluntarios: org.authorizedVolunteersCount,
          etiquetas: [
            org.organization.organizationTypeEnum,
            org.organization.sectorTypeEnum,
          ],
          photoUrl: org.photoUrl || 'assets/img/user3.png',
        }));
        this.filteredRecommendedOrganizations = this.organizacionesRecomendadas; // Inicialmente muestra todas
      },
      (error) => {
        console.error('Error al cargar las fundaciones recomendadas', error);
      }
    );

    // Obtener todas las organizaciones
    this.volunteerService.getAllOrganizations().subscribe(
      (data) => {
        this.todasLasOrganizaciones = data.map((org) => ({
          id: org.organizationId,
          titulo: org.name,
          descripcion: org.description || 'No hay descripción disponible.',
          voluntarios: org.authorizedVolunteersCount,
          etiquetas: [org.organizationType, org.sector],
          photoUrl: org.photoUrl || 'assets/img/user3.png',
        }));
        this.totalItems = this.todasLasOrganizaciones.length;
        this.totalPages = Math.ceil(this.totalItems / this.pageSize);  // Establecer el número total de organizaciones
        this.paginateOrganizations(); // Inicialmente muestra todas

      },
      (error) => {
        console.error('Error al cargar todas las organizaciones', error);
      }
    );

    // Obtener los sectores para el menú desplegable
    this.organizationService.getSectorTypes().subscribe(
      (sectors) => {
        this.sectors = ['TODOS', ...sectors]; // Agregar la opción "TODOS" como predeterminada
      },
      (error) => {
        console.error('Error al cargar los sectores', error);
      }
    );
  }

  // Función para obtener la imagen correspondiente
  getImage(index: number): string {
    return this.imagenes[index % this.imagenes.length];
  }

  // Función para filtrar organizaciones recomendadas y todas las organizaciones
  filterOrganizations(): void {
    const searchLower = this.searchText.toLowerCase();

    // Filtrar organizaciones recomendadas
    this.filteredRecommendedOrganizations =
      this.organizacionesRecomendadas.filter((org) => {
        const matchesSearch =
          org.titulo.toLowerCase().includes(searchLower) ||
          org.descripcion.toLowerCase().includes(searchLower);
        const matchesCategory =
          this.category === 'TODOS' || org.etiquetas.includes(this.category);
        return matchesSearch && matchesCategory;
      });

    // Filtrar todas las organizaciones
    const filteredAllOrgs = this.todasLasOrganizaciones.filter((org) => {
      const matchesSearch =
        org.titulo.toLowerCase().includes(searchLower) ||
        org.descripcion.toLowerCase().includes(searchLower);
      const matchesCategory =
        this.category === 'TODOS' || org.etiquetas.includes(this.category);
      return matchesSearch && matchesCategory;
    });

    // Actualizar total de elementos y paginación en base a los resultados filtrados
    this.totalItems = filteredAllOrgs.length;
    this.totalPages = Math.ceil(this.totalItems / this.pageSize);

    // Paginación de los resultados filtrados
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.filteredAllOrganizations = filteredAllOrgs.slice(start, end);
  }


  // Función para ver los detalles de una organización
  // Función para ver los detalles de una organización
  verDetalles(organization: any) {
    localStorage.setItem('SelectedOrganization', JSON.stringify(organization));

    this.router.navigate(['/verPerfilO'], {
      queryParams: { id: organization.id, from: 'homeV' },
    });
  }

}
