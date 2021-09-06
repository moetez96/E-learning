import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {TokenStorageService} from "../../services/token-storage.service";
import {Router} from "@angular/router";
import {FormationService} from "../../services/formation.service";

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {

  constructor(private tokenStorageService: TokenStorageService, private router: Router) {
  }

  ngOnInit() {

  }

  logout() {
    this.tokenStorageService.signOut();
    this.router.navigate(['/auth/signin']);
  }
}
