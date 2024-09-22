import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerPerfilOComponent } from '../Modules/Organization/viewModel/ver-perfil-o.component';

describe('VerPerfilOComponent', () => {
  let component: VerPerfilOComponent;
  let fixture: ComponentFixture<VerPerfilOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerPerfilOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerPerfilOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
