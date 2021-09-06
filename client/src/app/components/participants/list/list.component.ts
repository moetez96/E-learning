import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormationService} from "../../../services/formation.service";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  fullList: any
  isEditable = false

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private formationService: FormationService) {
  }

  ngOnInit(): void {
    this.fullList = this.data.list
    this.isEditable = this.data.isEditable
  }

  onRemove(id: String) {
    this.formationService.removeParticipate(this.data.idf, id).subscribe(data => {
      console.log(data);
    }, error => {
      console.log(error);
    })
  }
}
