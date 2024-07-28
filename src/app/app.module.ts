import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './Modules/landingModule/landing/landing.component';
import { NavbarComponent } from './Modules/landingModule/navbar/navbar.component';
import { AboutComponent } from './Modules/landingModule/about/about.component';
import { BenefitsComponent } from './Modules/landingModule/benefits/benefits.component';
import { TestimonialsComponent } from './Modules/landingModule/testimonials/testimonials.component';
import { MisionesComponent } from './Modules/landingModule/misiones/misiones.component';
import { FooterComponent } from './Modules/landingModule/footer/footer.component';
import { LoginComponent } from './Modules/login/login.component';
import { TokenService } from './services/token.service';
import { OauthService } from './services/oauth.service';
import { HomeComponent } from './Modules/homeModule/home/home.component'; // Importa el OauthService

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
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    HttpClientModule
  ],
  providers: [
    TokenService,
    OauthService,
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
