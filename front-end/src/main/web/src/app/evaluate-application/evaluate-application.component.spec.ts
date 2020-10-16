import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluateApplicationComponent } from './evaluate-application.component';

describe('EvaluateApplicationComponent', () => {
  let component: EvaluateApplicationComponent;
  let fixture: ComponentFixture<EvaluateApplicationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EvaluateApplicationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EvaluateApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
