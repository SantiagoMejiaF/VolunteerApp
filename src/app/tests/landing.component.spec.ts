import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from '../shared/components/viewModel/navbar.component';
import { LandingComponent } from '../Modules/landingModule/viewModel/landing.component';
import { RouterTestingModule } from '@angular/router/testing';
import { AboutComponent } from '../Modules/landingModule/viewModel/about.component';
import { BenefitsComponent } from '../Modules/landingModule/viewModel/benefits.component';
import { FooterComponent } from '../shared/components/viewModel/footer.component';
import { TestimonialsComponent } from '../Modules/landingModule/viewModel/testimonials.component';
import { MisionesComponent } from '../Modules/landingModule/viewModel/misiones.component';

describe('LandingComponent', () => {
  let component: LandingComponent;
  let fixture: ComponentFixture<LandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        LandingComponent,
        NavbarComponent,
        AboutComponent,
        BenefitsComponent,
        FooterComponent,
        TestimonialsComponent,
        MisionesComponent
      ], // Declara NavbarComponent aquí también
      imports: [RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(LandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
