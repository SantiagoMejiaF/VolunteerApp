import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  tempRole: string = '';
  userName: string = '';

  constructor(private router: Router, private userService: UserService) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    if (userInfo) {
      this.userName = userInfo.firstName;
    }
  }

  onSiguiente() {
    const userInfo = JSON.parse(localStorage.getItem('userInfo')!);
    if (!userInfo) {
      console.error('No se encontró información del usuario');
      return;
    }
    const userId = userInfo.id;
    if (this.tempRole === 'Voluntario') {
      this.userService.updateRole(Number(userId), 2).subscribe(
        () => {
          this.router.navigate(['/formsV']);
        },
        (error) => {
          console.error('Error updating role:', error);
        }
      );
    } else if (this.tempRole === 'Organizacion') {
      this.userService.updateRole(Number(userId), 3).subscribe(
        () => {
          this.router.navigate(['/formsO']);
        },
        (error) => {
          console.error('Error updating role:', error);
        }
      );
    } else {
      console.error('No se ha seleccionado ningún rol');
    }
  }

  ngOnInit(): void { }
}
