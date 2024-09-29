import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesAxCComponent } from './detalles-ax-c.component';

describe('DetallesAxCComponent', () => {
  let component: DetallesAxCComponent;
  let fixture: ComponentFixture<DetallesAxCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetallesAxCComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetallesAxCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
