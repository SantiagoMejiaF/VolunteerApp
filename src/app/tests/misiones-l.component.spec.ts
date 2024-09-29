import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisionesLComponent } from '../Modules/landingModule/viewModel/misiones-l.component';

describe('MisionesLComponent', () => {
  let component: MisionesLComponent;
  let fixture: ComponentFixture<MisionesLComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MisionesLComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisionesLComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
