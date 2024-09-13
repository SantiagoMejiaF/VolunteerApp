import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesAComponent } from '../Modules/Misiones/viewModel/detalles-a.component';

describe('DetallesAComponent', () => {
  let component: DetallesAComponent;
  let fixture: ComponentFixture<DetallesAComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetallesAComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetallesAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
