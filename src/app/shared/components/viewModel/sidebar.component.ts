import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-sidebar',
  templateUrl: '../view/sidebar.component.html',
  styleUrls: ['../styles/sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  userName: string = '';
  userEmail: string = '';
  userRole: string = '';
  isSidebarExpanded: boolean = false; // Controla la expansión del sidebar
  isMobileSidebarVisible: boolean = false; // Controla si el sidebar es visible en móvil

  notifications = [
    { title: 'Título 1', message: 'Fuiste aceptado' },
    { title: 'Título 2', message: 'Nueva actividad' },
    { title: 'Título 3', message: 'Has sido invitado' },
    { title: 'Título 4', message: 'Rechazado' },
    // Puedes agregar más notificaciones aquí
  ];
  ngOnInit(): void {
    this.loadUserInfo();
  }

  loadUserInfo(): void {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
    this.userName = `${userInfo.firstName} ${userInfo.lastName}`;
    this.userEmail = userInfo.email;
    this.userRole = localStorage.getItem('role') || '';
  }

  constructor(private router: Router) { }
  // Alternar expansión del sidebar
  toggleSidebar(): void {
    if (window.innerWidth <= 768) { // Si es móvil, mostrar u ocultar el sidebar móvil
      this.isMobileSidebarVisible = !this.isMobileSidebarVisible;
    } else {
      this.isSidebarExpanded = !this.isSidebarExpanded;
    }
  }

  closeSidebar(): void {
    this.isMobileSidebarVisible = false; // Ocultar el sidebar móvil
    this.isSidebarExpanded = false; // También ocultar el sidebar en vista de escritorio
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/']);
  }

  isMenuVisible: boolean = false;
  isMenu2Visible: boolean = false;
  // Método para el primer toggle
  toggleMenu() {
    // Si el segundo menú está abierto, lo cerramos
    if (this.isMenu2Visible) {
      this.isMenu2Visible = false;
    }
    // Alternamos el estado del primer menú
    this.isMenuVisible = !this.isMenuVisible;
  }

  // Método para el segundo toggle
  toggle2Menu() {
    // Si el primer menú está abierto, lo cerramos
    if (this.isMenuVisible) {
      this.isMenuVisible = false;
    }
    // Alternamos el estado del segundo menú
    this.isMenu2Visible = !this.isMenu2Visible;
  }

  closeMenu() {
    this.isMenuVisible = false;
  }

  goToProfile() {
    if (this.userRole === 'ORGANIZACION') {
      this.router.navigate(['/perfilO']);
    } else if (this.userRole === 'VOLUNTARIO') {
      this.router.navigate(['/perfil']);
    } else if (this.userRole === 'COORDINADOR') {
      this.router.navigate(['/perfilC']);
    } else {
      console.error('Rol no reconocido');
    }
  }

  // Método que devuelve la clase CSS para los colores según la posición
  getCircularClass(index: number): string {
    const colors = ['red-bg', 'blue-bg', 'pink-bg', 'green-bg', 'yellow-bg'];  // Definimos los colores en orden
    return colors[index % colors.length]; // Se repite el ciclo de colores
  }
  getColorClass(index: number): string {
    const colors = ['red-bg-icon', 'blue-bg-icon', 'pink-bg-icon', 'green-bg-icon', 'yellow-bg-icon'];  // Definimos los colores en orden
    return colors[index % colors.length]; // Se repite el ciclo de colores
  }

}