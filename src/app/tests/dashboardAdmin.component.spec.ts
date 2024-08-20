import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardAdminComponent } from '../Modules/AdminUser/viewModel/dashboardAdmin.component';

describe('DashBorrarComponent', () => {
  let component: DashboardAdminComponent;
  let fixture: ComponentFixture<DashboardAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
