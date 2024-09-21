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
import { MisionesComponent } from './Modules/Misiones/viewModel/misiones.component';
import { DetallesMComponent } from './Modules/Misiones/viewModel/detalles-m.component';
import { EditMComponent } from './Modules/Misiones/viewModel/edit-m.component';
import { CalendarAComponent } from './Modules/Misiones/viewModel/calendar-a.component';
import { DetallesAComponent } from './Modules/Misiones/viewModel/detalles-a.component';
import { CoordinadoresComponent } from './Modules/Organization/viewModel/coordinadores.component';
import { PerfilCComponent } from './Modules/Coordinators/viewModel/perfil-c.component';
import { DetallesCComponent } from './Modules/Coordinators/viewModel/detalles-c.component';

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
      {
        path: 'misiones',
        component:MisionesComponent,
      },
      {
        path: 'detallesM',
        component:DetallesMComponent,
      },
      {
        path: 'editM',
        component:EditMComponent,
      },
      {
        path: 'calendar',
        component:CalendarAComponent,
      },
      {
        path: 'detallesA',
        component:DetallesAComponent,
      },
      {
        path: 'coordinador',
        component:CoordinadoresComponent,
      },
      {
        path: 'perfilc',
        component:PerfilCComponent,
      },
      {
        path: 'detallesC',
        component:DetallesCComponent,
      },
      

      
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
