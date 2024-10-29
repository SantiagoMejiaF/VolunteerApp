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
import { VerPerfilVComponent } from './Modules/Volunteer/viewModel/ver-perfil-v.component';
import { VerPerfilOComponent } from './Modules/Organization/viewModel/ver-perfil-o.component';
import { ActividadDComponent } from './Modules/Misiones/viewModel/actividad-d.component';
import { VerPerfilCComponent } from './Modules/Coordinators/viewModel/ver-perfil-c.component';
import { VerVoluntariosComponent } from './Modules/Organization/viewModel/ver-voluntarios.component';
import { HomeVoluntariosComponent } from './Modules/Volunteer/viewModel/home-voluntarios.component';
import { MisFundacionesComponent } from './Modules/Volunteer/viewModel/mis-fundaciones.component';
import { ActividadesVComponent } from './Modules/Volunteer/viewModel/actividades-v.component';
import { DetallesAxCComponent } from './Modules/Organization/viewModel/detalles-ax-c.component';
import { ActividadesCComponent } from './Modules/Coordinators/viewModel/actividades-c.component';
import { UsuariosActivosComponent } from './Modules/AdminUser/viewModel/usuarios-activos.component';
import { ActividadesOComponent } from './Modules/Organization/viewModel/actividades-o.component';
import { MisActividadesComponent } from './Modules/Volunteer/viewModel/mis-actividades.component';
import { SolicitudesVComponent } from './Modules/Organization/viewModel/solicitudes-v.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'forms', component: AuthComponent },
  { path: 'formsV', component: FormsVolunteerComponent },
  { path: 'formsO', component: FormsOrganizacionComponent },
  { path: 'sideB', component: SidebarComponent },

  {
    path: '', // Utiliza una ruta vacía como prefijo para las rutas con Navbar
    component: SidebarComponent, // Este componente actúa como un layout
    children: [
      //ADMIN
      {
        path: 'gestion-users',
        component: GestionUsuariosComponent,
      },
      {
        path: 'dashAdmin',
        component: DashboardAdminComponent,
      },
      {
        path: 'active-users',
        component: UsuariosActivosComponent,
      },




      //PANTALLAS ORGANIZACIÓN
      {
        path: 'dashOrganization',
        component: DashboardOrganizationComponent,
      },
      {
        path: 'verPerfilO',
        component: VerPerfilOComponent,
      },
      {
        path: 'perfilO',
        component: PerfilOComponent,
      },
      {
        path: 'verVoluntarios',
        component: VerVoluntariosComponent,
      },
      {
        path: 'verCoordinador',
        component: CoordinadoresComponent,
      },
      {
        path: 'verDetallesAxC',
        component: DetallesAxCComponent,
      },
      {
        path: 'ActividadesO',
        component: ActividadesOComponent,
      },
      {
        path: 'SolicitudesV',
        component:SolicitudesVComponent,
      },



      //MISIONES 
      {
        path: 'misiones',
        component: MisionesComponent,
      },
      {
        path: 'detallesM',
        component: DetallesMComponent,
      },
      {
        path: 'editM',
        component: EditMComponent,
      },
      {
        path: 'calendar',
        component: CalendarAComponent,
      },
      {
        path: 'detallesA',
        component: DetallesAComponent,
      },
      {
        path: 'actividad/:id/:image',
        component: ActividadDComponent
      },

      //PANTALLAS DE VOLUNTARIO
      {
        path: 'homeV',
        component: HomeVoluntariosComponent,
      },
      {
        path: 'dashVolunteer',
        component: DashboardVolunteerComponent,
      },
      {
        path: 'misF',
        component: MisFundacionesComponent,
      },
      {
        path: 'miC',
        component: ActividadesVComponent,
      },
      {
        path: 'verPerfilV',
        component: VerPerfilVComponent,
      },
      {
        path: 'perfil',
        component: PerfilComponent,
      },
      {
        path: 'misA',
        component: MisActividadesComponent
      },

      //COORDINADOR

      {
        path: 'perfilC',
        component: PerfilCComponent,
      },
      {
        path: 'verPerfilC',
        component: VerPerfilCComponent,
      },
      {
        path: 'detallesC',
        component: DetallesCComponent,
      },
      {
        path: 'misAC',
        component: ActividadesCComponent,
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
