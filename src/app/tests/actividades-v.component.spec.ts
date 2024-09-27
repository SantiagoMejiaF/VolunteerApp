import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadesVComponent } from '../Modules/Volunteer/viewModel/actividades-v.component';

describe('ActividadesVComponent', () => {
  let component: ActividadesVComponent;
  let fixture: ComponentFixture<ActividadesVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActividadesVComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadesVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
