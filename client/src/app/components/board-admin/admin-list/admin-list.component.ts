import {Component, OnInit} from '@angular/core';
import {FormationService} from "../../../services/formation.service";
import {ListParticipantsComponent} from "../list-formation/list-participants/list-participants.component";
import {AddAdminComponent} from "./add-admin/add-admin.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-admin-list',
  templateUrl: './admin-list.component.html',
  styleUrls: ['./admin-list.component.css']
})
export class AdminListComponent implements OnInit {

  listUser: any[] = [];
  isLoading = true;
  deleteLoading = {id: "", loading: false};

  constructor(private formationService: FormationService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.formationService.getAllUsers().subscribe(data => {
      console.log(data)
      this.isLoading = false
      this.listUser = data
    }, error => {
      this.isLoading = false
      console.log(error)
    })
  }

  delete(id: any) {
    this.deleteLoading = {id: id, loading: true};
    console.log(id)
    this.formationService.deleteuser(id).subscribe(data => {
      this.ngOnInit();
      this.deleteLoading = {id: "", loading: false};
    }, error => {
      this.deleteLoading = {id: "", loading: false};
      console.log(error)
    })

  }
  openDialog() {
    const dialogRef = this.dialog.open(AddAdminComponent);
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
