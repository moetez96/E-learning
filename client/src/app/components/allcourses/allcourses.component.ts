import {Component, OnInit} from '@angular/core';
import {FormationService} from "../../services/formation.service";
import {TokenStorageService} from "../../services/token-storage.service";

@Component({
  selector: 'app-allcourses',
  templateUrl: './allcourses.component.html',
  styleUrls: ['./allcourses.component.css']
})
export class AllcoursesComponent implements OnInit {
  listFormation: any[] = []
  isLoading = true
  show = false

  constructor(private formationService: FormationService, private tokenStorageService: TokenStorageService) {
  }

  ngOnInit(): void {
    //this.formationService.getAll().subscribe(data => {
    this.formationService.getBySepc(this.tokenStorageService.getUser().speciality).subscribe(data => {
      this.listFormation = data
      this.isLoading = false
    }, error => {
      this.show = true
      this.isLoading = false
      console.log(error);
    })
  }

  Sort(type: string) {
    if (type == 'date') {
      this.listFormation = this.listFormation.sort((a, b) => (a.date > b.date) ? 1 : -1)
    } else if (type == 'name') {
      this.listFormation = this.listFormation.sort((a, b) => (a.name > b.name) ? 1 : -1)
    }
  }
}
