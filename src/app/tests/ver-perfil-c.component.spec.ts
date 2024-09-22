import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerPerfilCComponent } from '../Modules/Coordinators/viewModel/ver-perfil-c.component';

describe('VerPerfilCComponent', () => {
  let component: VerPerfilCComponent;
  let fixture: ComponentFixture<VerPerfilCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerPerfilCComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerPerfilCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
