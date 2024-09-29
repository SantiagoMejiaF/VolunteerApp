import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadDComponent } from '../Modules/Misiones/viewModel/actividad-d.component';

describe('ActividadDComponent', () => {
  let component: ActividadDComponent;
  let fixture: ComponentFixture<ActividadDComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActividadDComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadDComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
