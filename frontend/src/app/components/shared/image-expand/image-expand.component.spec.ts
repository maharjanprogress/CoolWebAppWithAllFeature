import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageExpandComponent } from './image-expand.component';

describe('ImageExpandComponent', () => {
  let component: ImageExpandComponent;
  let fixture: ComponentFixture<ImageExpandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImageExpandComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImageExpandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
