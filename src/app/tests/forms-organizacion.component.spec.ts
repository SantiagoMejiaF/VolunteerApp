import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsOrganizacionComponent } from '../Modules/authenticationModule/viewModel/forms-organizacion.component';

describe('FormsOrganizacionComponent', () => {
  let component: FormsOrganizacionComponent;
  let fixture: ComponentFixture<FormsOrganizacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormsOrganizacionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormsOrganizacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
