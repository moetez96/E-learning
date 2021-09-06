import {Component, Input, OnInit} from '@angular/core';
import {FormationService} from "../../services/formation.service";
import {TokenStorageService} from "../../services/token-storage.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {
  @Input()
  formation: any

  loading = false;
  errorMessage = '';
  roles: string[] = [];
  submitted = false;

  commentForm: FormGroup = new FormGroup({
    comment: new FormControl(''),
  });

  constructor(private _formBuilder: FormBuilder, private formationService: FormationService,
              private tokenStorageService: TokenStorageService) {
  }

  ngOnInit(): void {
    this.commentForm = this._formBuilder.group({
      comment: ['', [Validators.required]]
    });
  }

  addComment() {
    var dNow = new Date();
    var date = (dNow.getMonth() + 1) + '/' + dNow.getDate() + '/' + dNow.getFullYear() + ' ' + dNow.getHours() + ':' + dNow.getMinutes();

    const request = {
      userId: this.tokenStorageService.getUser().id,
      formationId: this.formation.id,
      userName: this.tokenStorageService.getUser().firstName + ' ' + this.tokenStorageService.getUser().lastName,
      text: this.commentForm.value.comment,
      date: date
    }
    if (this.commentForm.valid) {
      this.loading = true;
      this.formationService.addComment(request).subscribe(data => {
        console.log(data);
        this.formation = data
        this.loading = false;
      }, error => {
        console.log(error);
        this.loading = false;
      })
    } else {
      console.log('error')
    }
  }

  commentsChangedHandler(formation: any) {
    console.log(formation)
    this.formation = formation
  }
}
