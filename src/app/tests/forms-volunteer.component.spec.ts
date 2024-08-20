import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsVolunteerComponent } from '../Modules/authenticationModule/viewModel/forms-volunteer.component';

describe('FormsVolunteerComponent', () => {
  let component: FormsVolunteerComponent;
  let fixture: ComponentFixture<FormsVolunteerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormsVolunteerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormsVolunteerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
