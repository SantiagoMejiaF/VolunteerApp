import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilOComponent } from '../Modules/Organization/viewModel/perfil-o.component';

describe('PerfilOComponent', () => {
  let component: PerfilOComponent;
  let fixture: ComponentFixture<PerfilOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PerfilOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
