import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {FormationService} from "../../services/formation.service";
import {TokenStorageService} from "../../services/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-addformation',
  templateUrl: './addformation.component.html',
  styleUrls: ['./addformation.component.css']
})
export class AddformationComponent implements OnInit {
  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });

  selected = 'option2';
  submitted = false;

  addFormGroup: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    type: new FormControl(''),
  });
  close = false;
  loading = false;
  isFailed = false;
  errorMessage = "";
  isSuccess = false;
  // @ts-ignore
  file: File

  constructor(private _formBuilder: FormBuilder, private formationService: FormationService, private router: Router) {
  }

  ngOnInit(): void {
    this.addFormGroup = this._formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      type: ['', Validators.required],
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
    this.submitted = true;
    if (this.addFormGroup.valid && this.range.valid) {
      this.loading = true;
      this.formationService.add({
        name: this.addFormGroup.value.name,
        description: this.addFormGroup.value.description,
        type: this.addFormGroup.value.type,
        startDate: this.dateFormatter(this.range.value.start),
        endDate: this.dateFormatter(this.range.value.end),
        picture: this.file
      }).subscribe(data => {
        this.loading = false
        console.log(data)
        this.isSuccess = true;
        this.router.navigate(['/details/' + data.id]);
        setTimeout(() => {
          this.isSuccess = false;
        }, 5000);
      }, error => {
        this.loading = false
        this.errorMessage = error.error.message;
        this.isFailed = true;
        setTimeout(() => {
          this.isFailed = false;
        }, 5000);
        console.log(error)
      })
    }
  }

  get f() {
    return this.addFormGroup.controls;
  }

  addImage() {

  }

  onSelect($event: Event) {
    // @ts-ignore
    this.file = $event.target.files[0]
  }
}
