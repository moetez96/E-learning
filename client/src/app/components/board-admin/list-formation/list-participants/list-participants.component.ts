import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormationService} from "../../../../services/formation.service";

@Component({
  selector: 'app-list-participants',
  templateUrl: './list-participants.component.html',
  styleUrls: ['./list-participants.component.css']
})
export class ListParticipantsComponent implements OnInit {
  fullList: any;
  deleteLoading = {id: "", loading: false};

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private formationService: FormationService) {
  }

  ngOnInit(): void {
    console.log(this.data)
    this.fullList = this.data.participants
  }

  onRemove(id: any) {
    this.deleteLoading = {id: id, loading: true};
    this.formationService.removeParticipate(this.data.id, id).subscribe(data => {
      console.log(data);
      this.deleteLoading = {id: "", loading: false};
    }, error => {
      console.log(error);
      this.deleteLoading = {id: "", loading: false};
    })
  }
}
