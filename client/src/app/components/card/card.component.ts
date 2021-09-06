import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {
  @Input()
  formation: any
  participants = [];

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    for (var i in this.formation.listParticipants) { // @ts-ignore
      this.participants.push(this.formation.listParticipants[i]);
    }
  }

  onClick() {
    this.router.navigate(['/details', this.formation.id], {state: {formation: this.formation}});
  }
}
