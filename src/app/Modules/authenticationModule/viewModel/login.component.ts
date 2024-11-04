import { Component, OnInit } from '@angular/core';
import { SocialAuthService, SocialUser, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { Router, ActivatedRoute } from '@angular/router';
import { OauthService } from '../model/services/oauth.service';
import { TokenService } from '../model/services/token.service';
import { OrganizationService } from '../../Organization/model/services/organization.service';
import { TokenDto } from '../model/token-dto';
import { HttpClient } from '@angular/common/http';
import { VolunteerService } from '../../Volunteer/model/services/volunteer.service';
import { AdminService } from '../../AdminUser/model/services/admin.service';

@Component({
  selector: 'app-login',
  templateUrl: '../view/login.component.html',
  styleUrls: ['../styles/login.component.css']
})
export class LoginComponent implements OnInit {
  socialUser!: SocialUser;
  userlogged!: SocialUser;
  islogged: boolean = false;
  pendingMessage: string = ''; // Variable para almacenar el mensaje cargado desde el archivo

  constructor(
    private authService: SocialAuthService,
    private router: Router,
    private route: ActivatedRoute,
    private oauthService: OauthService,
    private tokenService: TokenService,
    private organizationService: OrganizationService,
    private volunteerService: VolunteerService,
    private http: HttpClient,
    private adminService: AdminService,
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
                  this.tokenService.setToken(res.token);
                  localStorage.setItem('userInfo', JSON.stringify(res.user));
                  this.islogged = true;
                  this.getRoleAndRedirect(res.user.role.roleType, res.user.authorizationType, res.user.id);
                },
                (err) => {
                  this.logOut();
                }
              );
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
          this.tokenService.setToken(res.token); // Guardar el token por separado
          localStorage.setItem('userInfo', JSON.stringify(res.user)); // Guardar solo el objeto `user`
          this.islogged = true;
          this.getRoleAndRedirect(res.user.role.roleType, res.user.authorizationType, res.user.id);
        },
        (err) => {
          this.logOut();
        }
      );
    }).catch((err) => {
      console.log('Error during Facebook login:', err);
    });
  }

  getRoleAndRedirect(roleType: string | null, authorizationType: string, userId: number): void {
    if (!roleType) {
      alert('Ha ocurrido un error, por favor vuélvalo a intentar');
      this.logOut();
      return;
    }

    localStorage.setItem('role', roleType);

    switch (roleType) {
      case 'SUPER_ADMIN':
        this.router.navigate(['/dashAdmin']);
        break;
      case 'SIN_ASIGNAR':
        this.router.navigate(['/forms']);
        break;
      case 'VOLUNTARIO':
        if (authorizationType === 'PENDIENTE') {
          this.loadPendingMessage();
        } else if (authorizationType === 'AUTORIZADO') {
          this.volunteerService.getVolunteerDetails(userId).subscribe(
            (volunteerDetails) => {
              console.log('VolunteerId:', volunteerDetails.id);
              localStorage.setItem('volunteerId', volunteerDetails.id.toString()); // Guardar el volunteerId en localStorage
              this.router.navigate(['/homeV']);
            },
            (error) => {
              console.error('Error al obtener los detalles del voluntario:', error);
              this.logOut();
            }
          );
        }
        break;

      case 'ORGANIZACION':
        if (authorizationType === 'PENDIENTE') {
          this.loadPendingMessage();
        } else if (authorizationType === 'AUTORIZADO') {
          this.organizationService.getOrganizationByUserId(userId).subscribe(
            (orgDetails) => {
              console.log('OrgId:', orgDetails.id);
              localStorage.setItem('OrgId', orgDetails.id.toString());
              this.router.navigate(['/dashOrganization']);
            },
            (error) => {
              console.error('Error al obtener los detalles de la organización:', error);
              this.logOut();
            }
          );
        }
        break;
      case 'COORDINADOR_ACTIVIDAD':
        this.adminService.getCoordinatorDetails(userId).subscribe(
          (coordinatorDetails) => {
            console.log('Coordinator ID:', coordinatorDetails.id);
            localStorage.setItem('coordinatorId', coordinatorDetails.id.toString()); // Guardar el coordinatorId en localStorage
            this.router.navigate(['/misAC']);
          },
          (error) => {
            console.error('Error al obtener los detalles del coordinador de actividad:', error);
            this.logOut();
          }
        );
        break;
      default:
        console.error('Unknown role type:', roleType);
    }
  }



  loadPendingMessage(): void {
    this.http.get('assets/textos/pendiente.txt', { responseType: 'text' }).subscribe(
      (data) => {
        this.pendingMessage = data;
        this.showPendingAlert();
      },
      (error) => {
        console.error('Error loading pending message:', error);
      }
    );
  }

  showPendingAlert(): void {
    alert(this.pendingMessage);
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
