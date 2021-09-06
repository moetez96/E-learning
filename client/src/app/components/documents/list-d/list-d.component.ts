import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormationService} from "../../../services/formation.service";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-list-d',
  templateUrl: './list-d.component.html',
  styleUrls: ['./list-d.component.css']
})
export class ListDComponent implements OnInit {
  fullList: any
  isEditable = false
  show = false
  // @ts-ignore
  file: File
  // @ts-ignore
  uploadForm: FormGroup;
  isLoading = false;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private formBuilder: FormBuilder, private formationService: FormationService) {
  }

  ngOnInit(): void {
    this.uploadForm = this.formBuilder.group({
      file: ['']
    });
    this.fullList = this.data.documents
    this.isEditable = this.data.isEditable
  }

  onRemove(id: String) {
    this.formationService.removeFile(id, this.data.idf).subscribe(data => {
      console.log(data);
    }, error => {
      console.log(error);
    })
  }

  onSelect($event: Event) {
    this.show = true
    // @ts-ignore
    this.file = $event.target.files[0]
  }

  add() {
    this.isLoading = true;
    console.log(this.file)
    this.formationService.addFile(this.file, this.data.idf).subscribe(data => {
      console.log(JSON.parse(data).documents);
      this.data.documents = JSON.parse(data).documents;
      this.ngOnInit();
      console.log(this.fullList);
      this.isLoading = false;
    }, error => {
      console.log(error);
    })
  }
}
