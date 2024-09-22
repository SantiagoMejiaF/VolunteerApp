import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerPerfilVComponent } from './ver-perfil-v.component';

describe('VerPerfilVComponent', () => {
  let component: VerPerfilVComponent;
  let fixture: ComponentFixture<VerPerfilVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VerPerfilVComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerPerfilVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
