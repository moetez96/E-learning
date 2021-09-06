import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-list-cards',
  templateUrl: './list-cards.component.html',
  styleUrls: ['./list-cards.component.css']
})
export class ListCardsComponent implements OnInit {
  @Input()
  list: any

  constructor() {
  }

  ngOnInit(): void {
  }

}
