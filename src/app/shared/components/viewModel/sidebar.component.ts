import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-sidebar',
  templateUrl: '../view/sidebar.component.html',
  styleUrl: '../../../styles/sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  nombreEmpresa: string = '';
  modulos: { id: number, description: string, rolId: number, status: string }[] = [];
  roles: string[] = [];

  showAdminCard: boolean = true;
  showRecruitmentCard: boolean = true;
  showDismissalCard: boolean = true;
  showNominaCard: boolean = true;
  showSSTCard: boolean = true;
  showBICard: boolean = true;

  

  ngOnInit(): void {
    const roleStr = localStorage.getItem('role');
    console.log('Role string from localStorage:', roleStr);

   

    const hamBurger = document.querySelector(".toggle-btn") as HTMLElement;
    hamBurger.addEventListener("click", () => {
      const sidebar = document.querySelector("#sidebar") as HTMLElement;
      sidebar.classList.toggle("expand");
    });

    const uniqueModules = new Set(); // Usar un Set para asegurar la unicidad
    let pendingRequests = this.roles.length; // Contador para solicitudes pendientes

  
  }

 
 
  logout(): void {
    localStorage.clear();
  }
}