import { NgModule, LOCALE_ID } from '@angular/core';
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
import { MisionesComponent } from './Modules/Misiones/viewModel/misiones.component';
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
import { PerfilOComponent } from './Modules/Organization/viewModel/perfil-o.component';
import { registerLocaleData } from '@angular/common';
import { CommonModule } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { DetallesMComponent } from './Modules/Misiones/viewModel/detalles-m.component';
import { EditMComponent } from './Modules/Misiones/viewModel/edit-m.component';
import { CalendarAComponent } from './Modules/Misiones/viewModel/calendar-a.component'; // IMPORTA EL LOCALE ESPAÑOL
import { FullCalendarModule } from '@fullcalendar/angular';
import { DetallesAComponent } from './Modules/Misiones/viewModel/detalles-a.component';
import { EditAComponent } from './Modules/Misiones/viewModel/edit-a.component';
import { CoordinadoresComponent } from './Modules/Organization/viewModel/coordinadores.component';
import { PerfilCComponent } from './Modules/Coordinators/viewModel/perfil-c.component';
import { DetallesCComponent } from './Modules/Coordinators/viewModel/detalles-c.component';
import { VerPerfilVComponent } from './Modules/Volunteer/viewModel/ver-perfil-v.component';
import { VerPerfilOComponent } from './Modules/Organization/viewModel/ver-perfil-o.component';
import { VerPerfilCComponent } from './Modules/Coordinators/viewModel/ver-perfil-c.component';
import { ActividadDComponent } from './Modules/Misiones/viewModel/actividad-d.component';
import { VerVoluntariosComponent } from './Modules/Organization/viewModel/ver-voluntarios.component';
import { HomeVoluntariosComponent } from './Modules/Volunteer/viewModel/home-voluntarios.component';
import { MisFundacionesComponent } from './Modules/Volunteer/viewModel/mis-fundaciones.component';

import { ActividadesVComponent } from './Modules/Volunteer/viewModel/actividades-v.component';
import { DetallesAxCComponent } from './Modules/Organization/viewModel/detalles-ax-c.component';



registerLocaleData(localeEs, 'es');

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    NavbarComponent,
    AboutComponent,
    BenefitsComponent,
    TestimonialsComponent,
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
    PerfilComponent,
    PerfilOComponent,
    MisionesComponent,
    DetallesMComponent,
    EditMComponent,
    CalendarAComponent,
    DetallesAComponent,
    EditAComponent,
    CoordinadoresComponent,
    PerfilCComponent,
    DetallesCComponent,
    VerPerfilVComponent,
    VerPerfilOComponent,
    VerPerfilCComponent,
    ActividadDComponent,
    VerVoluntariosComponent,
    HomeVoluntariosComponent,
    MisFundacionesComponent,
    ActividadesVComponent,
    DetallesAxCComponent
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
    HttpClientModule,
    CommonModule,
    FullCalendarModule,
  ],
  providers: [
    TokenService,
    OauthService,
    { provide: LOCALE_ID, useValue: 'es' } ,
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
