import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './Modules/landingModule/viewModel/landing.component';
import { NavbarComponent } from './shared/components/viewModel/navbar.component';
import { AboutComponent } from './Modules/landingModule/viewModel/about.component';
import { BenefitsComponent } from './Modules/landingModule/viewModel/benefits.component';
import { TestimonialsComponent } from './Modules/landingModule/viewModel/testimonials.component';
import { MisionesComponent } from './Modules/landingModule/viewModel/misiones.component';
import { FooterComponent } from './shared/components/viewModel/footer.component';
import { LoginComponent } from './Modules/authenticationModule/viewModel/login.component';
import { TokenService } from './Modules/authenticationModule/model/services/token.service';
import { OauthService } from './Modules/authenticationModule/model/services/oauth.service';
import { AuthComponent } from './Modules/authenticationModule/viewModel/auth.component'; // Importa el OauthService
import { FormsModule } from '@angular/forms';
import { FormsVolunteerComponent } from './Modules/authenticationModule/viewModel/forms-volunteer.component';
import { FormsOrganizacionComponent } from './Modules/authenticationModule/viewModel/forms-organizacion.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { ReactiveFormsModule } from '@angular/forms';
import { SidebarComponent } from './shared/components/viewModel/sidebar.component';
import { GestionUsuariosComponent } from './Modules/AdminUser/viewModel/gestion-usuarios.component';
import { DataTablesModule } from 'angular-datatables';
import { NgApexchartsModule } from "ng-apexcharts";
import { DashboardAdminComponent } from './Modules/AdminUser/viewModel/dashboardAdmin.component';
import { DashboardVolunteerComponent } from './Modules/Volunteer/viewModel/dashboard-volunteer.component';
import { DashboardOrganizationComponent } from './Modules/Organization/viewModel/dashboard-organization.component';
import { PerfilComponent } from './Modules/Volunteer/viewModel/perfil.component';

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
    AuthComponent,
    FormsVolunteerComponent,
    FormsOrganizacionComponent,
    SidebarComponent,
    GestionUsuariosComponent,
    DashboardAdminComponent,
    DashboardVolunteerComponent,
    DashboardOrganizationComponent,
    PerfilComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SocialLoginModule,
    ReactiveFormsModule,
    FormsModule,
    NgMultiSelectDropDownModule.forRoot(),
    DataTablesModule,
    NgApexchartsModule,
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
