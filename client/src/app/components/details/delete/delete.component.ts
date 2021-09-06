import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormationService} from "../../../services/formation.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.css']
})
export class DeleteComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private formationService: FormationService, private router: Router) {
  }

  ngOnInit(): void {
  }

  onDelete() {
    this.formationService.deleteFormation(this.data.id).subscribe(data => {
      console.log(data);
      this.router.navigate(['/mycourses']);
    }, error => {
      console.log(error);
      this.router.navigate(['/mycourses']);
    })
  }

}
