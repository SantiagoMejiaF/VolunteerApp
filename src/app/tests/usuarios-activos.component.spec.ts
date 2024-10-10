import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuariosActivosComponent } from './usuarios-activos.component';

describe('UsuariosActivosComponent', () => {
  let component: UsuariosActivosComponent;
  let fixture: ComponentFixture<UsuariosActivosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsuariosActivosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsuariosActivosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
