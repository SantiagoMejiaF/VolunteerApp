import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeVoluntariosComponent } from '../Modules/Volunteer/viewModel/home-voluntarios.component';

describe('HomeVoluntariosComponent', () => {
  let component: HomeVoluntariosComponent;
  let fixture: ComponentFixture<HomeVoluntariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomeVoluntariosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeVoluntariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
