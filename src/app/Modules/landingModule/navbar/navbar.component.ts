import { Component, OnInit, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  constructor(private renderer: Renderer2) { }

  ngOnInit(): void {
    const navEl = this.renderer.selectRootElement('.navbar', true);
    const logoEl = this.renderer.selectRootElement('#navbar-logo', true);

    window.addEventListener('scroll', () => {
      if (window.scrollY >= 70) {
        this.renderer.addClass(navEl, 'navbar-scrolled');
        this.renderer.setAttribute(logoEl, 'src', 'assets/img/logoColor.png');
      } else {
        this.renderer.removeClass(navEl, 'navbar-scrolled');
        this.renderer.setAttribute(logoEl, 'src', 'assets/img/LogoBlancoN.png');
      }
    });
  }
}
