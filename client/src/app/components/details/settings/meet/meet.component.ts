import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {FormationService} from "../../../../services/formation.service";

@Component({
  selector: 'app-meet',
  templateUrl: './meet.component.html',
  styleUrls: ['./meet.component.css']
})
export class MeetComponent implements OnInit {
  @Input()
  id: any

  public value: Date = new Date(2019, 5, 1, 22);
  public format = "MM/dd/yyyy HH:mm";
  loading = false;
  isFailed = false;
  isSuccess = false;
  errorMessage = "";

  addMeetForm: FormGroup = new FormGroup({
    link: new FormControl(''),
    date: new FormControl(''),
  });

  constructor(private _formBuilder: FormBuilder, private formationService: FormationService) {
  }

  ngOnInit(): void {
    console.log(this.id)
    this.addMeetForm = this._formBuilder.group({
      link: ['', Validators.required],
    });
  }

  onSubmit() {
    console.log({
      link: this.addMeetForm.value.link,
      date: this.value.toLocaleString()
    })
    this.formationService.addMeet(this.id, {
      link: this.addMeetForm.value.link,
      date: this.value.toLocaleString()
    }).subscribe(data => {
      console.log(data)
    }, error => {
      console.log(error)
    })
  }
}
