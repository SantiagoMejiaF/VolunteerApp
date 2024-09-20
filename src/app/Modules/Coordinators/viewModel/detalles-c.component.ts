import { Component, ViewChild, EventEmitter, Output, AfterViewInit } from "@angular/core";

@Component({
  selector: 'app-detalles-c',
  templateUrl: '../view/detalles-c.component.html',
  styleUrl: '../styles/detalles-c.component.css'
})
export class DetallesCComponent  {
  currentContent: string = 'content1';
  selectedSection: string = 'descripcion';
  showContent(contentId: string) {
    this.currentContent = contentId;
  }
  
  @Output() back = new EventEmitter<void>();

  onComeBack() {
    this.back.emit();
  }
  public data = [
    {
      firstName: 'Juan',
      lastName: 'Pérez',
      email: 'juan.perez@example.com',
      cedula: '1234567890',
      image: 'assets/img/user1.png'
    },
    {
      firstName: 'Ana',
      lastName: 'García',
      email: 'ana.garcia@example.com',
      cedula: '0987654321',
      image: 'assets/img/ana.png'
    },
    {
      firstName: 'Carlos',
      lastName: 'Martínez',
      email: 'carlos.martinez@example.com',
      cedula: '1122334455',
      image: ''
    }
  ];
  ngOnInit(): void {
    this.initializeDataTable();
  }

  
  initializeDataTable(): void {
    setTimeout(() => {
      $('#datatableexample').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        scrollX: true,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrar página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty: '<span style="font-size: 0.875rem;">No hay registros</span>',
          infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
          lengthMenu: '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
          zeroRecords: '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
        }
      });
    }, 1);
  }
}
