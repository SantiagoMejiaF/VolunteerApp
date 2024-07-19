import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './landingModule/landing/landing.component';
import { NavbarComponent } from './landingModule/navbar/navbar.component';
import { AboutComponent } from './landingModule/about/about.component';
import { BenefitsComponent } from './landingModule/benefits/benefits.component';
import { TestimonialsComponent } from './landingModule/testimonials/testimonials.component';
import { MisionesComponent } from './landingModule/misiones/misiones.component';
import { FooterComponent } from './landingModule/footer/footer.component';
import { LoginComponent } from './authenticationModule/login/login.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    NavbarComponent,
    AboutComponent,
    BenefitsComponent,
    TestimonialsComponent,
    MisionesComponent,
    FooterComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
