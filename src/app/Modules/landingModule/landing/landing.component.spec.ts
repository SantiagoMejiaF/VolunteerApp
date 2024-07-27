import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from '../navbar/navbar.component';
import { LandingComponent } from './landing.component';
import { RouterTestingModule } from '@angular/router/testing';
import { AboutComponent } from '../about/about.component';
import { BenefitsComponent } from '../benefits/benefits.component';
import { FooterComponent } from '../footer/footer.component';
import { TestimonialsComponent } from '../testimonials/testimonials.component';
import { MisionesComponent } from '../misiones/misiones.component';

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
