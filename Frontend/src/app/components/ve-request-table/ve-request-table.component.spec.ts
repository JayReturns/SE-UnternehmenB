import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VeRequestTableComponent } from './ve-request-table.component';

describe('VeRequestTableComponent', () => {
  let component: VeRequestTableComponent;
  let fixture: ComponentFixture<VeRequestTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VeRequestTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VeRequestTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
