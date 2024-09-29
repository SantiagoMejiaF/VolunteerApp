import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerVoluntariosComponent } from '../Modules/Organization/viewModel/ver-voluntarios.component';

describe('VerVoluntariosComponent', () => {
  let component: VerVoluntariosComponent;
  let fixture: ComponentFixture<VerVoluntariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerVoluntariosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerVoluntariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
