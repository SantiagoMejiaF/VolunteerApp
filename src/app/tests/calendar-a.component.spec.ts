import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarAComponent } from '../Modules/Misiones/viewModel/calendar-a.component';

describe('CalendarAComponent', () => {
  let component: CalendarAComponent;
  let fixture: ComponentFixture<CalendarAComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CalendarAComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CalendarAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
