import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminListUserComponent } from './admin-list-user.component';

describe('AdminListUserComponent', () => {
  let component: AdminListUserComponent;
  let fixture: ComponentFixture<AdminListUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminListUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminListUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
