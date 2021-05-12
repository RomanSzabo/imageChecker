import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckImageComponent } from './check-image.component';

describe('CheckImageComponent', () => {
  let component: CheckImageComponent;
  let fixture: ComponentFixture<CheckImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
