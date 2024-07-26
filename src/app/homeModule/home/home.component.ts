import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  userInfo: any;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    this.userInfo = navigation?.extras.state?.['userInfo'];
  }

  ngOnInit(): void {
    if (!this.userInfo) {
      this.router.navigate(['/login']);
    }
  }

  logOut(): void {
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }
}
