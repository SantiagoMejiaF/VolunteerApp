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

    // Cargar la biblioteca de Google Identity Services
    this.loadGoogleScript();
  }

  loadGoogleScript(): void {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    document.body.appendChild(script);
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
              console.log('Google access token being sent to backend:', tokenGoogle);
              this.oauthService.google(tokenGoogle).subscribe(
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
      console.log('Facebook access token being sent to backend:', tokenFace);
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
