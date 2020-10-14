import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SupervisorListApplicationComponent } from './supervisor-list-application.component';

describe('SupervisorListApplicationComponent', () => {
  let component: SupervisorListApplicationComponent;
  let fixture: ComponentFixture<SupervisorListApplicationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SupervisorListApplicationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SupervisorListApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
