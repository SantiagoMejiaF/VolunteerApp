import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { AdminService } from '../../../Modules/AdminUser/model/services/admin.service';
@Component({
  selector: 'app-sidebar',
  templateUrl: '../view/sidebar.component.html',
  styleUrls: ['../styles/sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  userName: string = '';
  userEmail: string = '';
  userRole: string = '';
  userImage: SafeUrl = ''
  isSidebarExpanded: boolean = false; // Controla la expansión del sidebar
  isMobileSidebarVisible: boolean = false; // Controla si el sidebar es visible en móvil

  notifications: any[] = [];
  ngOnInit(): void {
    this.loadUserInfo();
    this.loadNotifications();
    console.log(this.userRole);
  }

  loadUserInfo(): void {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
    this.userName = `${userInfo.firstName} ${userInfo.lastName}`;
    this.userEmail = userInfo.email;
    this.userRole = localStorage.getItem('role') || '';
    this.userImage = this.sanitizer.bypassSecurityTrustUrl(userInfo.image || 'assets/img/default-user.png');
  }

  loadNotifications(): void {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
    console.log('User Info:', userInfo); // Añade esto para depuración
    const userId = userInfo.id; // Asegúrate de que esta propiedad exista

    if (userId) {
      this.adminService.getNotifications(userId).subscribe(
        (data) => {
          this.notifications = data; // Asignar las notificaciones obtenidas
        },
        (error) => {
          console.error('Error al cargar las notificaciones', error);
        }
      );
    } else {
      console.error('User ID no disponible. Verifica que el usuario esté correctamente autenticado.');
    }
  }



  constructor(private router: Router, private sanitizer: DomSanitizer, private adminService: AdminService) { }
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
  menuOpen = false;

  toggleMenu4() {
    this.menuOpen = !this.menuOpen;
  }
}