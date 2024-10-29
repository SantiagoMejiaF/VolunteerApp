import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitudesVComponent } from '../Modules/Organization/viewModel/solicitudes-v.component';

describe('SolicitudesVComponent', () => {
  let component: SolicitudesVComponent;
  let fixture: ComponentFixture<SolicitudesVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SolicitudesVComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SolicitudesVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
