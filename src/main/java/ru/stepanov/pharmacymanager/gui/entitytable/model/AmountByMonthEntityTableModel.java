package ru.stepanov.pharmacymanager.gui.entitytable.model;

import java.util.Vector;
import ru.stepanov.pharmacymanager.dto.AmountByMonthDTO;

public class AmountByMonthEntityTableModel extends EntityTableModel<AmountByMonthDTO> {

  private static final String[] COLUMN_NAMES = {"Месяц", "Год", "Сумма продаж"};

  public AmountByMonthEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, AmountByMonthDTO entity) {
    vector.add(entity.getMonth());
    vector.add(entity.getYear());
    vector.add(entity.getAmount());
  }
}
