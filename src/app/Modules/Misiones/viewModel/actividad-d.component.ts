import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-actividad-d',
  templateUrl: '../view/actividad-d.component.html',
  styleUrls: ['../styles/actividad-d.component.css']
})
export class ActividadDComponent implements OnInit {
  imagen: string = '';
  btnClass: string = '';
  actividadId: number = 0;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    // Leer los parámetros de la URL
    this.route.params.subscribe(params => {
      this.actividadId = +params['id'];
      this.imagen = 'assets/img/' + params['image'];
      this.btnClass = params['btnClass'];
    });
  }

  unirse(event: Event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del enlace
    alert(`Te has unido exitosamente a NOMBRE ACTIVIDAD`);
  }
}

