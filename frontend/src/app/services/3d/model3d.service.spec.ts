import { TestBed } from '@angular/core/testing';

import { Model3dService } from './model3d.service';

describe('Model3dService', () => {
  let service: Model3dService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Model3dService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
