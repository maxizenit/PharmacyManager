package ru.stepanov.pharmacymanager.gui.entitytable.model;

import java.util.Vector;
import ru.stepanov.pharmacymanager.entity.Disease;

public class DiseaseEntityTableModel extends EntityTableModel<Disease> {

  private static final String[] COLUMN_NAMES = {"Название"};

  public DiseaseEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Disease entity) {
    vector.add(entity.getName());
  }
}
