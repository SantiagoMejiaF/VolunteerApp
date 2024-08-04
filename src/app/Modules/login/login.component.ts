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
    const intervalId = setInterval(() => {
      if (window.google && window.google.accounts && window.google.accounts.oauth2) {
        clearInterval(intervalId);
        window.google.accounts.oauth2.initTokenClient({
          client_id: '142245667829-afpupoofnh363onmduragfrhduii4jj5.apps.googleusercontent.com',
          scope: 'profile email',
          callback: (response: any) => {
            if (response && response.access_token) {
              const tokenGoogle = new TokenDto(response.access_token);
              console.log('Google Access Token:', response.access_token);
              this.oauthService.google(tokenGoogle).subscribe(
                (res) => {
                  console.log('Response from backend (Google):', res); // Imprimir respuesta del backend
                  this.tokenService.setToken(res.value);
                  this.islogged = true;
                  this.router.navigate(['/home'], { state: { userInfo: res } }); // Navega y pasa la información del usuario
                },
                (err) => {
                  console.log('Error fetching Google user info:', err); // Imprimir error
                  this.logOut();
                }
              );
            } else {
              console.error('No access token received');
            }
          }
        }).requestAccessToken();
      }
    }, 100);
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID).then((user) => {
      this.socialUser = user;
      const tokenFace = new TokenDto(this.socialUser.authToken);
      this.oauthService.facebook(tokenFace).subscribe(
        (res) => {
          console.log('Response from backend (Facebook):', res); // Imprimir respuesta del backend
          this.tokenService.setToken(res.value);
          this.islogged = true;
          this.router.navigate(['/home'], { state: { userInfo: res } }); // Navega y pasa la información del usuario
        },
        (err) => {
          console.log('Error fetching Facebook user info:', err); // Imprimir error
          this.logOut();
        }
      );
    }).catch((err) => {
      console.log('Error during Facebook login:', err); // Imprimir error
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
