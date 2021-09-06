import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormationService} from "../../services/formation.service";
import {TokenStorageService} from "../../services/token-storage.service";
import {MatDialog} from "@angular/material/dialog";
import {ListComponent} from "../participants/list/list.component";
import {SettingsComponent} from "./settings/settings.component";
import {DeleteComponent} from "./delete/delete.component";

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {
  formation = history.state.formation
  id: string = "";
  isParticipated = false;
  participants = [];
  documents: any[] = [];
  isStudent = this.tokenStorageService.getUser().roles.includes("ROLE_STUDENT");
  isEditable = false;
  isLoading = this.formation == undefined;
  isLoadingP = false;

  constructor(private activatedRoute: ActivatedRoute, private formationService: FormationService,
              private tokenStorageService: TokenStorageService, public dialog: MatDialog) {
  }

  openDialog() {
    const dialogRef = this.dialog.open(SettingsComponent, {data: {formation: this.formation}});
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  openDialogDelete() {
    const dialogRef = this.dialog.open(DeleteComponent, {data: {id: this.formation.id}});
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  ngOnInit()
    :
    void {
    console.log(this.isLoading);

    console.log(this.activatedRoute.snapshot.paramMap.get('id'));
    // @ts-ignore
    this.id = this.activatedRoute.snapshot.paramMap.get('id')
    this.formationService.getById(this.id).subscribe(data => {
      this.formation = data;
      for (var i in this.formation.listParticipants) {
        // @ts-ignore
        this.participants.push(this.formation.listParticipants[i].id);
      }
      for (var i in this.formation.documents) {
        // @ts-ignore
        this.documents.push(this.formation.documents[i]);
      }
      // @ts-ignore
      this.isParticipated = this.participants.includes(this.tokenStorageService.getUser().id)
      this.isEditable = this.tokenStorageService.getUser().id == this.formation.teacher.id
      this.isLoading = false;
      console.log('edit: ' + this.isEditable)
      console.log(this.formation)
      console.log(this.isParticipated)
      console.log(this.documents)
      console.log(this.tokenStorageService.getUser().id)
    }, error => {
      console.log(error)
    });
  }

  participate() {
    this.isLoadingP = true;
    this.formationService.participate(this.id).subscribe(data => {
      this.formation = data
      console.log(data)
      this.isLoadingP = false;
      this.isParticipated = !this.isParticipated
    }, error => {
      console.log(error)
      this.isLoadingP = false;
    })
  }

  cancel() {
    this.isLoadingP = true;
    this.formationService.cancelParticipate(this.id).subscribe(data => {
      this.formation = data
      console.log(data)
      this.isLoadingP = false;
      this.isParticipated = !this.isParticipated
    }, error => {
      console.log(error)
      this.isLoadingP = false;
    })
  }
}
