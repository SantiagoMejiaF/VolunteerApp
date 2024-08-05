import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  tempRole: string = '';
  userName: string = '';

  constructor(private router: Router) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    if (userInfo) {
      this.userName = userInfo.firstName;
    }
  }

  onSiguiente() {
    if (this.tempRole === 'Voluntario') {
      this.router.navigate(['/formsV']);
    } else if (this.tempRole === 'Organizacion') {
      this.router.navigate(['/formsO']);
    } else {
      console.error('No se ha seleccionado ningún rol');
    }
  }

  ngOnInit(): void { }
}
