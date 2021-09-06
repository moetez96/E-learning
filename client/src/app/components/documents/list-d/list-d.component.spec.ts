import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListDComponent } from './list-d.component';

describe('ListDComponent', () => {
  let component: ListDComponent;
  let fixture: ComponentFixture<ListDComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListDComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListDComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
