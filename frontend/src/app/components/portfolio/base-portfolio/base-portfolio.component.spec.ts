import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasePortfolioComponent } from './base-portfolio.component';

describe('BasePortfolioComponent', () => {
  let component: BasePortfolioComponent;
  let fixture: ComponentFixture<BasePortfolioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BasePortfolioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BasePortfolioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
