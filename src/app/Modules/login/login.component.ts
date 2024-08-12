import { Component, OnInit } from '@angular/core';
import { SocialAuthService, SocialUser, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { Router, ActivatedRoute } from '@angular/router';
import { OauthService } from '../../services/oauth.service';
import { TokenService } from '../../services/token.service';
import { TokenDto } from '../../models/token-dto';
import { HttpClient } from '@angular/common/http';

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
    private tokenService: TokenService,
    private http: HttpClient // Añadimos HttpClient para hacer las solicitudes HTTP
  ) { }

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.userlogged = user;
      this.islogged = this.userlogged != null && this.tokenService.getToken() != null;
      if (this.islogged) {
        this.router.navigate(['/forms']);
      }
    });

    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        this.tokenService.setToken(token);
        this.islogged = true;
        this.router.navigate(['/forms']);
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
                  console.log('Response from backend (Google):', res);
                  this.tokenService.setToken(res.value);
                  localStorage.setItem('userInfo', JSON.stringify(res));
                  this.islogged = true;
                  this.getRoleAndRedirect(res.user.roleId, res.user.authorizationType);
                },
                (err) => {
                  console.log('Error fetching Google user info:', err);
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
          console.log('Response from backend (Facebook):', res);
          this.tokenService.setToken(res.value);
          localStorage.setItem('userInfo', JSON.stringify(res));
          this.islogged = true;
          this.getRoleAndRedirect(res.user.roleId, res.user.authorizationType);
        },
        (err) => {
          console.log('Error fetching Facebook user info:', err);
          this.logOut();
        }
      );
    }).catch((err) => {
      console.log('Error during Facebook login:', err);
    });
  }

  getRoleAndRedirect(roleId: number, authorizationType: string): void {
    this.oauthService.getUserRole(roleId).subscribe((role: any) => {
      const roleType = role.roleType;

      switch (roleType) {
        case 'SUPER_ADMIN':
          this.router.navigate(['/dashAdmin']);
          break;
        case 'SIN_ASIGNAR':
          this.router.navigate(['/forms']);
          break;
        case 'VOLUNTARIO':
          if (authorizationType === 'PENDIENTE') {
            this.router.navigate(['/formsV']);
          } else if (authorizationType === 'AUTORIZADO') {
            this.router.navigate(['/dashVolunteer']);
          }
          break;
        case 'ORGANIZACION':
          if (authorizationType === 'PENDIENTE') {
            this.router.navigate(['/formsO']);
          } else if (authorizationType === 'AUTORIZADO') {
            this.router.navigate(['/dashOrganization']);
          }
          break;
        default:
          console.error('Unknown role type:', roleType);
      }
    }, (error) => {
      console.error('Error fetching role info:', error);
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
