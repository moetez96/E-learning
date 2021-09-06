import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormationService} from "../../../../services/formation.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-add-admin',
  templateUrl: './add-admin.component.html',
  styleUrls: ['./add-admin.component.css']
})
export class AddAdminComponent implements OnInit {
  formGroup: FormGroup = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    birthDate: new FormControl(''),
    username: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
  });
  submitted = false;
  loading = false;
  isLoginFailed = false;
  errorMessage: any;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private _formBuilder: FormBuilder, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.formGroup = this._formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      birthDate: ['', Validators.required],
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  dateFormatter(date: any) {
    let fdate = ""
    if (date.getMonth() < 10) {
      fdate = date.getFullYear() + "-" +
        0 + "" + date.getMonth() + "-" +
        date.getDay()
    }
    return fdate
  }

  onSubmit() {
    this.loading = true;
    this.submitted = true;
    if (this.formGroup.valid) {
      const requestJson = {
        "username": this.formGroup.value.username,
        "email": this.formGroup.value.email,
        "roles": ['admin'],
        "password": this.formGroup.value.password,
        "firstName": this.formGroup.value.firstName,
        "lastName": this.formGroup.value.lastName,
        "birthDate": this.dateFormatter(this.formGroup.value.birthDate)
      }
      console.log(requestJson)
      this.authService.registerAdmin(requestJson).subscribe(
        data => {
          console.log(data);
        },
        err => {
          console.log(err);
          this.loading = false;
          this.errorMessage = err.error.message;
          this.isLoginFailed = true;
          setTimeout(() => {
            this.isLoginFailed = false;
          }, 5000);
        }
      );
    }


  }

  get f() {
    return this.formGroup.controls;
  }
}
