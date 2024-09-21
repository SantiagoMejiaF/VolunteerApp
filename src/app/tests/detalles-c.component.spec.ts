import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesCComponent } from '../Modules/Coordinators/viewModel/detalles-c.component';

describe('DetallesCComponent', () => {
  let component: DetallesCComponent;
  let fixture: ComponentFixture<DetallesCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetallesCComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetallesCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
