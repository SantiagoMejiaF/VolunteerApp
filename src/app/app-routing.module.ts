import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './Modules/landingModule/landing/landing.component';
import { LoginComponent } from './Modules/login/login.component';
import { HomeComponent } from './Modules/homeModule/home/home.component';
import { AuthComponent } from './Modules/authenticationModule/auth/auth.component';
import { FormsVolunteerComponent } from './Modules/authenticationModule/forms-volunteer/forms-volunteer.component';
import { FormsOrganizacionComponent } from './Modules/authenticationModule/forms-organizacion/forms-organizacion.component';
import { SidebarComponent } from './Modules/dashboardModule/sidebar/sidebar.component';
import { GestionUsuariosComponent } from './Modules/AdminUser/gestion-usuarios/gestion-usuarios.component';
import { DashboardAdminComponent} from './Modules/AdminUser/dashboardAdmin/dashboardAdmin.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  {path: 'forms', component: AuthComponent},
  {path: 'formsV', component: FormsVolunteerComponent},
  {path: 'formsO', component: FormsOrganizacionComponent},
  
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
