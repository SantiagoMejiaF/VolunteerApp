import { Component, OnInit, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: '../view/navbar.component.html',
  styleUrls: ['../styles/navbar.component.css']
})
export class NavbarComponent implements OnInit {
  constructor(private renderer: Renderer2) { }

  ngOnInit(): void {
    const navEl = this.renderer.selectRootElement('.navbar', true);
    const logoEl = this.renderer.selectRootElement('#navbar-logo', true);
    const togglerIconEl = this.renderer.selectRootElement('.navbar-toggler-icon', true);

    window.addEventListener('scroll', () => {
      if (window.scrollY >= 70) {
        this.renderer.addClass(navEl, 'navbar-scrolled');
        this.renderer.setAttribute(logoEl, 'src', 'assets/img/logoColor.png');
        this.renderer.setStyle(togglerIconEl, 'background-image', 'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'16\' height=\'16\' fill=\'black\' class=\'bi bi-list\' viewBox=\'0 0 16 16\'%3E%3Cpath fill-rule=\'evenodd\' d=\'M2.5 12.5A.5.5 0 0 1 2 12h12a.5.5 0 0 1 0 1H2a.5.5 0 0 1-.5-.5zm0-5A.5.5 0 0 1 2 7h12a.5.5 0 0 1 0 1H2a.5.5 0 0 1-.5-.5zm0-5A.5.5 0 0 1 2 2h12a.5.5 0 0 1 0 1H2a.5.5 0 0 1-.5-.5z\'/%3E%3C/svg%3E")');
      } else {
        this.renderer.removeClass(navEl, 'navbar-scrolled');
        this.renderer.setAttribute(logoEl, 'src', 'assets/img/LogoBlancoN.png');
        this.renderer.setStyle(togglerIconEl, 'background-image', 'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'16\' height=\'16\' fill=\'white\' class=\'bi bi-list\' viewBox=\'0 0 16 16\'%3E%3Cpath fill-rule=\'evenodd\' d=\'M2.5 12.5A.5.5 0 0 1 2 12h12a.5.5 0 0 1 0 1H2a.5.5 0 0 1-.5-.5zm0-5A.5.5 0 0 1 2 7h12a.5.5 0 0 1 0 1H2a.5.5 0 0 1-.5-.5zm0-5A.5.5 0 0 1 2 2h12a.5.5 0 0 1 0 1H2a.5.5 0 0 1-.5-.5z\'/%3E%3C/svg%3E")');
      }
    });
  }
}
