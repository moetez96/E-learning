import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormationService} from "../../../services/formation.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  formation: any

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private formationService: FormationService) {
  }

  ngOnInit(): void {
  }

}
