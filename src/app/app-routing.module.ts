import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './landingModule/landing/landing.component';
import { LoginComponent } from './authenticationModule/login/login.component';
import { RegisterComponent } from './authenticationModule/register/register.component';

const routes: Routes = [
  {path: '', component:LandingComponent},
  {path: 'login', component:LoginComponent},
  {path: 'register', component:RegisterComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
