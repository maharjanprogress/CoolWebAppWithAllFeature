import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseWelcomeComponent } from './base-welcome.component';

describe('BasePortfolioComponent', () => {
  let component: BaseWelcomeComponent;
  let fixture: ComponentFixture<BaseWelcomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BaseWelcomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BaseWelcomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
