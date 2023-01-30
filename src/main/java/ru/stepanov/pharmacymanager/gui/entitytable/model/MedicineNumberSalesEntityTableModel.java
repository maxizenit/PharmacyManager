package ru.stepanov.pharmacymanager.gui.entitytable.model;

import java.util.Vector;
import ru.stepanov.pharmacymanager.dto.MedicineNumberSalesDTO;

public class MedicineNumberSalesEntityTableModel extends EntityTableModel<MedicineNumberSalesDTO> {

  private static final String[] COLUMN_NAMES = {"Лекарство", "Количество продаж"};

  public MedicineNumberSalesEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, MedicineNumberSalesDTO entity) {
    vector.add(entity.getMedicine().getName());
    vector.add(entity.getCount());
  }
}
