import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';

@NgModule({
    declarations: [
        HomeComponent
    ],
    imports: [
        CommonModule // Importa CommonModule para usar ngIf y otras directivas comunes
    ]
})
export class HomeModule { }
