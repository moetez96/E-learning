import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TokenStorageService} from "../../../services/token-storage.service";
import {FormationService} from "../../../services/formation.service";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {
  @Input()
  comment: any;

  @Output() commentsChanged: EventEmitter<any> = new EventEmitter();

  userId: any;
  loading = false;

  constructor(private tokenStorageService: TokenStorageService, private formationService: FormationService) {
  }

  ngOnInit(): void {
    this.userId = this.tokenStorageService.getUser().id
    console.log(this.userId)
  }

  delete() {
    this.loading = true
    this.formationService.deleteComment(this.comment.formationId, this.comment.id).subscribe(data => {
      console.log(data);
      this.loading = false
      this.commentsChanged.emit(data)
    }, error => {
      console.log(error)
      this.loading = false
    })
  }
}
