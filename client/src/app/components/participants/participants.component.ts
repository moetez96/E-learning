import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {ListComponent} from "./list/list.component";

@Component({
  selector: 'app-participants',
  templateUrl: './participants.component.html',
  styleUrls: ['./participants.component.css']
})
export class ParticipantsComponent implements OnInit {
  @Input()
  participants: any[] = []
  @Input()
  idf: any
  @Input()
  isEditable: boolean | undefined
  smallList: any
  list: any[] = []

  constructor(public dialog: MatDialog) {
  }

  ngOnChanges() {
    this.ngOnInit();
    console.log(this.participants);
  }

  ngOnInit(): void {

  }

  openDialog() {
    const dialogRef = this.dialog.open(ListComponent, {
      data: {
        list: this.participants,
        idf: this.idf,
        isEditable: this.isEditable
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
