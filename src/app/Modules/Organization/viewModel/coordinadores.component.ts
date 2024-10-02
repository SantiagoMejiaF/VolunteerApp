import { Component, OnInit } from '@angular/core';
import { OrganizationService } from '../../Organization/model/services/organization.service';
import { OauthService } from '../../authenticationModule/model/services/oauth.service';
import { TokenDto } from '../../authenticationModule/model/token-dto';
import { forkJoin } from 'rxjs';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-coordinadores',
  templateUrl: '../view/coordinadores.component.html',
  styleUrls: ['../styles/coordinadores.component.css']
})
export class CoordinadoresComponent implements OnInit {
  public data: any[] = [];
  public showSocialButtons: boolean = true;

  constructor(
    private organizationService: OrganizationService,
    private oauthService: OauthService
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
              this.oauthService.googleC(tokenGoogle).subscribe(
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
          alert('Coordinador creado exitosamente.');

          // Ocultar el modal usando Bootstrap
          const modalElement = document.getElementById('VolunteerModal');
          if (modalElement) {
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            modalInstance.hide();
          }
        },
        (error) => {
          console.error('Error creating coordinator:', error);
          alert('Hubo un error al crear el coordinador.');
        }
      );
    } else {
      alert('Por favor complete todos los campos.');
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
}
