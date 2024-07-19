import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { HttpClientModule } from '@angular/common/http';

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
import { HomeComponent } from './homeModule/home/home.component'; // Importa el HomeComponent
import { TokenService } from './services/token.service'; // Asegúrate de importar el TokenService

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
    LoginComponent,
    HomeComponent // Asegúrate de que HomeComponent está declarado aquí
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule
  ],
  providers: [
    TokenService, // Asegúrate de que TokenService está registrado como proveedor
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '142245667829-afpupoofnh363onmduragfrhduii4jj5.apps.googleusercontent.com'
            )
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('8189553181096059')
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
