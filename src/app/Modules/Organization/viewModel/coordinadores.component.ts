import { Component, OnInit } from '@angular/core';
import { OrganizationService } from '../../Organization/model/services/organization.service';
import { OauthService } from '../../authenticationModule/model/services/oauth.service';
import { TokenDto } from '../../authenticationModule/model/token-dto';
import { forkJoin } from 'rxjs';
import * as bootstrap from 'bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'app-coordinadores',
  templateUrl: '../view/coordinadores.component.html',
  styleUrls: ['../styles/coordinadores.component.css']
})
export class CoordinadoresComponent implements OnInit {
  public data: any[] = [];
  public showSocialButtons: boolean = true;
  showAlert = false;
  showAlert2 = false;
  showAlert3 = false;
  constructor(
    private organizationService: OrganizationService,
    private oauthService: OauthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCoordinators();
  }

  loadCoordinators(): void {
    const orgId = localStorage.getItem('OrgId');
    if (orgId) {
      this.organizationService.getActivityCoordinators(orgId).subscribe(
        (coordinators) => {
          const userRequests = coordinators.map((coordinator: any) => {
            return this.organizationService.getUserDetails(coordinator.userId);
          });

          forkJoin(userRequests).subscribe(
            (userDetails: any[]) => {
              this.data = coordinators.map((coordinator: any, index: number) => {
                return {
                  ...coordinator,
                  firstName: userDetails[index].firstName,
                  lastName: userDetails[index].lastName,
                  email: userDetails[index].email,
                  image: userDetails[index].image
                };
              });
              this.initializeDataTable();
            },
            (error) => {
              console.error('Error loading user details:', error);
            }
          );
        },
        (error) => {
          console.error('Error loading coordinators:', error);
        }
      );
    }
  }

  onGoogleSignIn(): void {
    const intervalId = setInterval(() => {
      if (window.google && window.google.accounts && window.google.accounts.oauth2) {
        clearInterval(intervalId);
        window.google.accounts.oauth2.initTokenClient({
          client_id: '142245667829-afpupoofnh363onmduragfrhduii4jj5.apps.googleusercontent.com',
          scope: 'profile email',
          callback: (response: any) => {
            if (response && response.access_token) {
              const tokenGoogle = new TokenDto(response.access_token);
              this.oauthService.google(tokenGoogle).subscribe(
                (res) => {
                  const user = res.user;
                  console.log('User from backend (Google):', user);

                  // Ocultar botones sociales y mostrar formulario
                  this.showSocialButtons = false;

                  // Guardar el userId para crear el coordinador más tarde
                  localStorage.setItem('GoogleUserId', user.id.toString());
                },
                (err) => {
                  console.error('Error during Google sign in:', err);
                }
              );
            }
          }
        }).requestAccessToken();
      }
    }, 100);
  }

  createCoordinator(): void {
    const orgId = localStorage.getItem('OrgId');
    const userId = localStorage.getItem('GoogleUserId');
    const identificationCard = (document.getElementById('cedula') as HTMLInputElement).value;
    const phoneActivityCoordinator = (document.getElementById('celular') as HTMLInputElement).value;

    if (orgId && userId && identificationCard && phoneActivityCoordinator) {
      const coordinatorData = {
        organizationId: orgId,
        userId: +userId,
        identificationCard: identificationCard,
        phoneActivityCoordinator: phoneActivityCoordinator
      };

      this.organizationService.createActivityCoordinator(coordinatorData).subscribe(
        (response) => {
          console.log('Coordinator created:', response);
          this.closeModal();
          this.showAlert=true;

        },
        (error) => {
          this.closeModal();
          console.error('Error creating coordinator:', error);
          this.showAlert2=true;
        }
      );
    } else {
      this.showAlert3=true;
    }
  }

  initializeDataTable(): void {
    if ($.fn.dataTable.isDataTable('#datatableCoordinadores')) {
      $('#datatableCoordinadores').DataTable().destroy();
    }
    setTimeout(() => {
      $('#datatableCoordinadores').DataTable({
        pagingType: 'full_numbers',
        pageLength: 5,
        processing: true,
        lengthMenu: [5, 10, 25],
        scrollX: true,
        language: {
          info: '<span style="font-size: 0.875rem;">Mostrar página _PAGE_ de _PAGES_</span>',
          search: '<span style="font-size: 0.875rem;">Buscar</span>',
          infoEmpty: '<span style="font-size: 0.875rem;">No hay registros</span>',
          infoFiltered: '<span style="font-size: 0.875rem;">(Filtrado de _MAX_ registros)</span>',
          lengthMenu: '<span style="font-size: 0.875rem;">_MENU_ registros por página</span>',
          zeroRecords: '<span style="font-size: 0.875rem;">No se encuentra - perdón</span>',
        }
      });
    }, 1);
  }

  viewCoordinatorProfile(coordinator: any): void {
    localStorage.setItem('SelectedCoordinator', JSON.stringify(coordinator));
    console.log('Coordinador seleccionado guardado:', coordinator); // Verifica los datos en la consola
    this.router.navigate(['/verPerfilC']);
  }

  closeModal() {
    const modal = document.getElementById('VolunteerModal');
    if (modal) {
      const modalInstance = (window as any).bootstrap.Modal.getInstance(modal);
      modalInstance.hide();
      const backdrops = document.querySelectorAll('.modal-backdrop');
      backdrops.forEach((backdrop) => backdrop.remove());
    }
  }
}
