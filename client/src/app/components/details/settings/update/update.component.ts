import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {FormationService} from "../../../../services/formation.service";

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent implements OnInit {
  @Input()
  formation = {id: String, name: String, startDate: String, endDate: String, type: String, description: String}

  range = new FormGroup({
    start: new FormControl(this.formation.startDate),
    end: new FormControl(this.formation.endDate)
  });

  selected = 'option2';
  submitted = false;

  addFormGroup: FormGroup = new FormGroup({
    name: new FormControl(this.formation.name),
    description: new FormControl(''),
    type: new FormControl(this.formation.type),
  });
  close = false;
  loading = false;
  isFailed = false;
  errorMessage = "";
  isSuccess = false;
  // @ts-ignore
  file: File

  constructor(private _formBuilder: FormBuilder, private formationService: FormationService) {
  }

  ngOnInit(): void {
    console.log(this.formation)
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
      this.formationService.update({
        name: this.addFormGroup.value.name,
        description: this.addFormGroup.value.description,
        type: this.addFormGroup.value.type,
        startDate: this.dateFormatter(this.range.value.start),
        endDate: this.dateFormatter(this.range.value.end),
        picture: this.file
      }, this.formation.id).subscribe(data => {
        this.loading = false
        console.log(data)
        this.isSuccess = true;
        setTimeout(() => {
          this.isSuccess = false;
          window.location.reload();
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

  onSelect($event: Event) {
    // @ts-ignore
    this.file = $event.target.files[0]
  }
}
