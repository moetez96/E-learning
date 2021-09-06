import {Component, OnInit} from '@angular/core';
import {FormationService} from "../../services/formation.service";
import {TokenStorageService} from "../../services/token-storage.service";

@Component({
  selector: 'app-mycourses',
  templateUrl: './mycourses.component.html',
  styleUrls: ['./mycourses.component.css']
})
export class MycoursesComponent implements OnInit {

  listFormation: any[] = []
  list: any[] = []
  isLoading = true
  show = false;

  constructor(private formationService: FormationService, private tokenStorageService: TokenStorageService) {
  }

  ngOnInit(): void {
    this.formationService.getAll().subscribe(data => {
      console.log(data)
      for (let i in data) {
        this.list.push(data[i]);
      }
      console.log(this.list)
      if (this.tokenStorageService.getUser().roles.includes("ROLE_TEACHER")) {
        for (let i in this.list) {
          if (this.tokenStorageService.getUser().id == this.list[i].teacher.id) {
            this.listFormation.push(this.list[i])
          }
        }
      } else {
        for (let i in this.list) {
          for (let j in this.list[i].listParticipants)
            if (this.tokenStorageService.getUser().id == this.list[i].listParticipants[j].id) {
              this.listFormation.push(this.list[i])
            }
        }
      }
      if (this.list.length == 0)
        this.show = true
      this.isLoading = false
      console.log(this.listFormation)
    }, error => {
      this.isLoading = false
      console.log(error);
    })

  }

}
