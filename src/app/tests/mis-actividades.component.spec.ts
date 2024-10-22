import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisActividadesComponent } from '../Modules/Volunteer/viewModel/mis-actividades.component';

describe('MisActividadesComponent', () => {
  let component: MisActividadesComponent;
  let fixture: ComponentFixture<MisActividadesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MisActividadesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisActividadesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
