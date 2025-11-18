import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleChooseComponent } from './role-choose.component';

describe('RoleChooseComponent', () => {
  let component: RoleChooseComponent;
  let fixture: ComponentFixture<RoleChooseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleChooseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleChooseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
