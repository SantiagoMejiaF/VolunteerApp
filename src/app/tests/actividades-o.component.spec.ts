import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadesOComponent } from '../Modules/Organization/viewModel/actividades-o.component';

describe('ActividadesOComponent', () => {
  let component: ActividadesOComponent;
  let fixture: ComponentFixture<ActividadesOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActividadesOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadesOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
