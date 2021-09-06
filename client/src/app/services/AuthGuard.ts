import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";
import {TokenStorageService} from "./token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  private isLoggedIn: boolean = false;

  constructor(private authService: AuthService, private tokenStorageService: TokenStorageService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    this.isLoggedIn = !!this.tokenStorageService.getToken();
    console.log(!!route.data.roles)
    if (!this.isLoggedIn) {
      console.log(this.isLoggedIn)
      this.router.navigateByUrl('/auth/signin');
      return false;
    } else if (!!route.data.roles) {
      console.log(!route.data.roles)
      console.log("1 : " + this.tokenStorageService.getUser().roles)
      console.log("1 : " + route.data.roles)
      if (this.tokenStorageService.getUser().roles != 'ROLE_ADMIN') {
        console.log("2 : " + route.data.roles)
        this.router.navigateByUrl('/notauthorized');
        return false;
      }
      return true
    } else {
      return true
    }
  }
}
