import {Component, OnInit} from '@angular/core';
import {FormationService} from "../../../services/formation.service";
import {MatDialog} from "@angular/material/dialog";
import {ListParticipantsComponent} from "./list-participants/list-participants.component";
import {Router} from "@angular/router";

@Component({
  selector: 'app-list-formation',
  templateUrl: './list-formation.component.html',
  styleUrls: ['./list-formation.component.css']
})
export class ListFormationComponent implements OnInit {
  listFormations: any[] = []
  isLoading = true;
  deleteLoading = {id: "", loading: false};

  constructor(private formationService: FormationService, public dialog: MatDialog, private router: Router) {
  }

  ngOnInit(): void {
    this.formationService.getAll().subscribe(data => {
      console.log(data)
      this.isLoading = false
      this.listFormations = data
    }, error => {
      this.isLoading = false
      console.log(error)
    })
  }

  delete(id: any) {
    this.deleteLoading = {id: id, loading: true};
    this.formationService.deleteFormationByAdmin(id).subscribe(data => {
      console.log(data)
      this.listFormations = data
      this.deleteLoading = {id: "", loading: false};
    }, error => {
      this.deleteLoading = {id: "", loading: false};
      console.log(error)
    })

  }

  openDialog(listParticipants: any, id: any) {
    const dialogRef = this.dialog.open(ListParticipantsComponent, {
      data: {
        participants: listParticipants, id: id,
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  Details(id: any) {
    this.router.navigate(['/details', id])
  }
}
