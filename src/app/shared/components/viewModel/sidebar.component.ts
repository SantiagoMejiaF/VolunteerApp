import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: '../view/sidebar.component.html',
  styleUrls: ['../../../styles/sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  role: string = '';

  ngOnInit(): void {
    const roleStr = localStorage.getItem('role');
    if (roleStr) {
      this.role = roleStr;
    }

    const hamBurger = document.querySelector(".toggle-btn") as HTMLElement;
    hamBurger.addEventListener("click", () => {
      const sidebar = document.querySelector("#sidebar") as HTMLElement;
      sidebar.classList.toggle("expand");
    });
  }

  logout(): void {
    localStorage.clear();
  }
}
