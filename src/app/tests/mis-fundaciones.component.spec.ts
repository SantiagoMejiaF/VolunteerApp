import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisFundacionesComponent } from '../Modules/Volunteer/viewModel/mis-fundaciones.component';

describe('MisFundacionesComponent', () => {
  let component: MisFundacionesComponent;
  let fixture: ComponentFixture<MisFundacionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MisFundacionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisFundacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
