import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditMComponent } from '../Modules/Misiones/viewModel/edit-m.component';

describe('EditMComponent', () => {
  let component: EditMComponent;
  let fixture: ComponentFixture<EditMComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditMComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
