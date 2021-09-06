import {Component, Input, OnInit} from '@angular/core';
import {ListComponent} from "../participants/list/list.component";
import {ListDComponent} from "./list-d/list-d.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.css']
})
export class DocumentsComponent implements OnInit {
  @Input()
  documents: any
  @Input()
  isEditable: boolean | undefined
  @Input()
  idf: any

  constructor(public dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  openDialog() {
    const dialogRef = this.dialog.open(ListDComponent, {
      data: {
        documents: this.documents,
        idf: this.idf,
        isEditable: this.isEditable
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
