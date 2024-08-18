import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardOrganizationComponent } from '../Modules/Organization/viewModel/dashboard-organization.component';

describe('DashboardOrganizationComponent', () => {
  let component: DashboardOrganizationComponent;
  let fixture: ComponentFixture<DashboardOrganizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardOrganizationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardOrganizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
