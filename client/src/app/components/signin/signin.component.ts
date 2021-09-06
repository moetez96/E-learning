import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from 'src/app/services/token-storage.service';
import {AuthService} from "../../services/auth.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {
  isLoggedIn = false;
  isLoginFailed = false;
  loading = false;
  errorMessage = '';
  roles: string[] = [];
  hide = true;
  submitted = false;

  loginFormGroup: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  constructor(private _formBuilder: FormBuilder, private authService: AuthService,
              private tokenStorage: TokenStorageService, private router: Router) {
  }

  ngOnInit(): void {
    this.loginFormGroup = this._formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
    }
  }

  get f() {
    return this.loginFormGroup.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.loginFormGroup.valid) {
      this.loading = true;
      this.authService.login(this.loginFormGroup.value).subscribe(
        data => {
          //window.location.reload();
          this.tokenStorage.saveToken(data.accessToken);
          this.tokenStorage.saveUser(data);

          this.isLoginFailed = false;
          this.isLoggedIn = true;
          this.roles = this.tokenStorage.getUser().roles;
          if (this.roles.includes('ROLE_ADMIN')){
            this.router.navigate(['/admin'])
            console.log('admin')
          }else{
            this.router.navigate(['/mycourses']);
            console.log('mouch admin')
          }
          console.log(this.roles);
          console.log(data);
        },
        err => {
          this.loading = false;
          console.log(err);
          this.errorMessage = err.error.message;
          this.isLoginFailed = true;
          setTimeout(() => {
            this.isLoginFailed = false;
          }, 5000);

        }
      );
    }
  }

  reloadPage() {
    window.location.reload();
  }
}
