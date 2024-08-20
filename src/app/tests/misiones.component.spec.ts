import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisionesComponent } from '../Modules/landingModule/viewModel/misiones.component';

describe('MisionesComponent', () => {
  let component: MisionesComponent;
  let fixture: ComponentFixture<MisionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MisionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
