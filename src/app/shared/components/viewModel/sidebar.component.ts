import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: '../view/sidebar.component.html',
  styleUrls: ['../styles/sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  role: string = '';
  isSidebarExpanded: boolean = false; // Controla la expansión del sidebar
  isMobileSidebarVisible: boolean = false; // Controla si el sidebar es visible en móvil

  ngOnInit(): void {
    const roleStr = localStorage.getItem('role');
    if (roleStr) {
      this.role = roleStr;
    }
  }

  // Alternar expansión del sidebar
  toggleSidebar(): void {
    if (window.innerWidth <= 768) { // Si es móvil, mostrar u ocultar el sidebar móvil
      this.isMobileSidebarVisible = !this.isMobileSidebarVisible;
    } else {
      this.isSidebarExpanded = !this.isSidebarExpanded;
    }
  }

   // Método para cerrar el sidebar en móviles
   closeSidebar(): void {
    this.isMobileSidebarVisible = false; // Ocultar el sidebar móvil
    this.isSidebarExpanded = false; // También ocultar el sidebar en vista de escritorio
  }
  logout(): void {
    localStorage.clear();
  }
}