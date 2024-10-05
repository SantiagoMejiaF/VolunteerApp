import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'app-landing',
  templateUrl: '../view/landing.component.html',
  styleUrls: ['../styles/landing.component.css']
})
export class LandingComponent  {
  constructor(
    private route: ActivatedRoute,
    private viewportScroller: ViewportScroller,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Detectar el fragmento al cargar la página
    this.route.fragment.subscribe(fragment => {
      if (fragment) {
        this.scrollToFragment(fragment);
      }
    });
  }

  scrollToFragment(fragment: string): void {
    // Usar ViewportScroller para hacer scroll a la sección correspondiente
    this.viewportScroller.scrollToAnchor(fragment);
  }
  
}
