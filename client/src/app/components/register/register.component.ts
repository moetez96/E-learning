import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {TokenStorageService} from "../../services/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  User: any = ['student', 'teacher'];

  firstFormGroup: FormGroup = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    birthDate: new FormControl(''),
    speciality: new FormControl(''),
    classe: new FormControl(''),
  });
  secondFormGroup: FormGroup = new FormGroup({
    username: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
    role: new FormControl(''),
  });
  isEditable = true;
  submitted1 = false;
  submitted2 = false;
  hide = true;
  loading = false;
  isLoginFailed = false;
  errorMessage: any;
  Specs: any = ['Web_Dev', 'Mobile_Dev', 'IOT'];
  checked = false;
  indeterminate = false;
  disabled = false;

  constructor(private _formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    this.firstFormGroup = this._formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      birthDate: ['', Validators.required],
      speciality: ['', Validators.required],
    });
    this.secondFormGroup = this._formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['', Validators.required],
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
    const requestJson = {
      "username": this.secondFormGroup.value.username,
      "email": this.secondFormGroup.value.email,
      "roles": [this.secondFormGroup.value.role],
      "password": this.secondFormGroup.value.password,
      "firstName": this.firstFormGroup.value.firstName,
      "lastName": this.firstFormGroup.value.lastName,
      "speciality": this.firstFormGroup.value.speciality,
      "birthDate": this.dateFormatter(this.firstFormGroup.value.birthDate)
    }
    console.log(requestJson)
    this.authService.register(requestJson).subscribe(
      data => {
        console.log(data);
        this.router.navigate(['/auth/signin']);
        /*this.isSuccessful = true;
        this.isSignUpFailed = false;*/
      },
      err => {
        console.log(err);
        this.loading = false;
        this.errorMessage = err.error.message;
        /*this.isSignUpFailed = true;*/
        this.isLoginFailed = true;
        setTimeout(() => {
          this.isLoginFailed = false;
        }, 5000);
      }
    );
    /*console.log(this.firstFormGroup.value);
    console.log(this.secondFormGroup.value);
    console.log(Object.assign(this.firstFormGroup.value, this.secondFormGroup.value))*/
  }

  onSubmit1() {
    this.submitted1 = true;
  }

  onSubmit2() {
    this.submitted2 = true;
  }

  get f1() {
    return this.firstFormGroup.controls;
  }

  get f2() {
    return this.secondFormGroup.controls;
  }
}
