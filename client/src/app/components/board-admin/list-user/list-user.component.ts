import {Component, OnInit} from '@angular/core';
import {FormationService} from "../../../services/formation.service";

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css']
})
export class ListUserComponent implements OnInit {
  listUser: any[] = [];
  isLoading = true;
  deleteLoading = {id: "", loading: false};

  constructor(private formationService: FormationService) {
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
}
