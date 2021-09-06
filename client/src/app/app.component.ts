import {Component} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {AddformationComponent} from "./components/addformation/addformation.component";
import {TokenStorageService} from "./services/token-storage.service";
import {Router, NavigationEnd} from '@angular/router';
import {filter} from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username: string = "";
  currentRoute: string = "";
  route = ""
  currentUser: any = null

  constructor(public dialog: MatDialog, private tokenStorageService: TokenStorageService, private router: Router) {
    console.log(router.url);

    router.events.pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(event => {
        this.route = JSON.parse(JSON.stringify(event)).url;
        console.log(JSON.parse(JSON.stringify(event)).url);
      });
  }

  ngOnInit() {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      this.currentUser = this.tokenStorageService.getUser();

      this.roles = this.currentUser.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.roles.includes('ROLE_TEACHER');

      this.username = this.currentUser.username;
    }
  }

  update() {
    this.router.navigate(['/profile'])
  }

  logout() {
    this.tokenStorageService.signOut();
    this.router.navigate(['/auth/signin']);
  }

  openDialog() {
    const dialogRef = this.dialog.open(AddformationComponent);

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
