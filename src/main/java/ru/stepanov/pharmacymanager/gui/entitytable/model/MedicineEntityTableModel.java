package ru.stepanov.pharmacymanager.gui.entitytable.model;

import java.util.Vector;
import ru.stepanov.pharmacymanager.entity.Medicine;

public class MedicineEntityTableModel extends EntityTableModel<Medicine> {

  private static final String[] COLUMN_NAMES = {"Название", "Цена", "В наличии"};

  public MedicineEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Medicine entity) {
    vector.add(entity.getName());
    vector.add(entity.getPrice());
    vector.add(entity.getCount());
  }
}
