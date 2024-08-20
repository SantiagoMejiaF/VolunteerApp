import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './Modules/landingModule/viewModel/landing.component';
import { LoginComponent } from './Modules/authenticationModule/viewModel/login.component';
import { AuthComponent } from './Modules/authenticationModule/viewModel/auth.component';
import { FormsVolunteerComponent } from './Modules/authenticationModule/viewModel/forms-volunteer.component';
import { FormsOrganizacionComponent } from './Modules/authenticationModule/viewModel/forms-organizacion.component';
import { SidebarComponent } from './shared/components/viewModel/sidebar.component';
import { GestionUsuariosComponent } from './Modules/AdminUser/viewModel/gestion-usuarios.component';
import { DashboardAdminComponent } from './Modules/AdminUser/viewModel/dashboardAdmin.component';
import { DashboardVolunteerComponent } from './Modules/Volunteer/viewModel/dashboard-volunteer.component';
import { DashboardOrganizationComponent } from './Modules/Organization/viewModel/dashboard-organization.component';
import { PerfilComponent } from './Modules/Volunteer/viewModel/perfil.component';
import { PerfilOComponent } from './Modules/Organization/viewModel/perfil-o.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'forms', component: AuthComponent },
  { path: 'formsV', component: FormsVolunteerComponent },
  { path: 'formsO', component: FormsOrganizacionComponent },

  {
    path: '', // Utiliza una ruta vacía como prefijo para las rutas con Navbar
    component: SidebarComponent, // Este componente actúa como un layout
    children: [
      {
        path: 'gestion-users',
        component: GestionUsuariosComponent,
      },
      {
        path: 'dashAdmin',
        component: DashboardAdminComponent,
      },
      {
        path: 'dashVolunteer',
        component: DashboardVolunteerComponent,
      },
      {
        path: 'dashOrganization',
        component: DashboardOrganizationComponent,
      },
      {
        path: 'perfil',
        component: PerfilComponent,
      },
      {
        path: 'perfilO',
        component: PerfilOComponent,
      },
      // Ruta hija
      // Puedes añadir más rutas hijas que requieran Navbar aquí
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
