import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesMComponent } from '../Modules/Misiones/viewModel/detalles-m.component';

describe('DetallesMComponent', () => {
  let component: DetallesMComponent;
  let fixture: ComponentFixture<DetallesMComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetallesMComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetallesMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
