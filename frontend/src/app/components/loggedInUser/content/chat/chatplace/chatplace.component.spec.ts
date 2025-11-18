import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatplaceComponent } from './chatplace.component';

describe('ChatplaceComponent', () => {
  let component: ChatplaceComponent;
  let fixture: ComponentFixture<ChatplaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatplaceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatplaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
