import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-actividad-d',
  templateUrl: '../view/actividad-d.component.html',
  styleUrls: ['../styles/actividad-d.component.css']
})
export class ActividadDComponent implements OnInit {
  imagen: string = '';
  btnClass: string = '';
  origen: string;
  actividadId: number = 0;
  public mostrarBotonUnirse: boolean = true;

  constructor(private route: ActivatedRoute, private router: Router) {
    // Obtener el parámetro 'origen' de la ruta
    this.route.queryParams.subscribe(params => {
      this.origen = params['from'];
    });

    // Aquí también puedes obtener el ID de la fundación si es necesario
    // this.idFundacion = ...;
  }

  ngOnInit() {
    // Leer los parámetros de la URL
    this.route.params.subscribe(params => {
      this.actividadId = +params['id'];
      this.imagen = 'assets/img/' + params['image'];
      this.mostrarBotonUnirse = params['fromMisActividades'] !== 'true';
    });
  }

  unirse(event: Event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del enlace
    alert(`Te has unido exitosamente a NOMBRE ACTIVIDAD`);
  }

  volver() {
    // Volver a los detalles de la fundación con el parámetro 'origen'
    
    if (this.origen === 'misF' ||this.origen === 'homeV') {
      this.router.navigate(['/verPerfilO'], { queryParams: { from: this.origen } });
    } else {
      this.router.navigate(['/misA']); // Navegar a Detalles de Fundación
    }
  }
  
}

