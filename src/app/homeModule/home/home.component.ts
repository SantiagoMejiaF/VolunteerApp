import { Component, OnInit } from '@angular/core';
import { SocialUser, SocialAuthService } from '@abacritt/angularx-social-login';
import { Router } from '@angular/router';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  user!: SocialUser;

  constructor(
    private authService: SocialAuthService,
    private tokenService: TokenService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.user = user;
    });
  }

  logOut(): void {
    this.authService.signOut().then(() => {
      this.tokenService.logOut();
      this.router.navigate(['/login']);
    });
  }
}
