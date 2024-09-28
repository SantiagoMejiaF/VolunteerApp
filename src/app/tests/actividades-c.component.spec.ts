import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadesCComponent } from './actividades-c.component';

describe('ActividadesCComponent', () => {
  let component: ActividadesCComponent;
  let fixture: ComponentFixture<ActividadesCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActividadesCComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadesCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
