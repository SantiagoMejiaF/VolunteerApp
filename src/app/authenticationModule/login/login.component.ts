import { Component, OnInit } from '@angular/core';
import { SocialAuthService, SocialUser, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { Router, ActivatedRoute } from '@angular/router';
import { OauthService } from '../../services/oauth.service';
import { TokenService } from '../../services/token.service';
import { TokenDto } from '../../models/token-dto';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  socialUser!: SocialUser;
  userlogged!: SocialUser;
  islogged: boolean = false;

  constructor(
    private authService: SocialAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private oauthService: OauthService,
    private tokenService: TokenService
  ) { }

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.userlogged = user;
      this.islogged = this.userlogged != null && this.tokenService.getToken() != null;
      if (this.islogged) {
        this.router.navigate(['/home']);
      }
    });

    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        this.tokenService.setToken(token);
        this.islogged = true;
        this.router.navigate(['/home']);
      }
    });
  }

  signInWithGoogle(): void {
    const clientId = '142245667829-afpupoofnh363onmduragfrhduii4jj5.apps.googleusercontent.com';
    const redirectUri = 'http://localhost:4200/auth/google/callback';
    const scope = 'profile email';
    const responseType = 'code';
    const state = ''; // Opcional: puedes incluir un estado para CSRF

    const url = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&response_type=${responseType}&state=${state}`;
    window.location.href = url;
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID).then((user) => {
      this.socialUser = user;
      const tokenFace = new TokenDto(this.socialUser.authToken);
      this.oauthService.facebook(tokenFace).subscribe(
        (res) => {
          this.tokenService.setToken(res.value);
          this.islogged = true;
          this.router.navigate(['/home']);
        },
        (err) => {
          console.log(err);
          this.logOut();
        }
      );
    }).catch((err) => {
      console.log(err);
    });
  }

  signInWithApple(): void {
    console.log('Inicio de sesión con Apple no implementado');
  }

  logOut(): void {
    this.authService.signOut().then(() => {
      this.tokenService.logOut();
      this.islogged = false;
      this.router.navigate(['/login']);
    });
  }
}
