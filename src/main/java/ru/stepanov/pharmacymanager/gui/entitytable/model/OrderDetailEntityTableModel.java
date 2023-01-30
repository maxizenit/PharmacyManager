package ru.stepanov.pharmacymanager.gui.entitytable.model;

import java.util.Vector;
import ru.stepanov.pharmacymanager.entity.OrderDetail;

public class OrderDetailEntityTableModel extends EntityTableModel<OrderDetail> {

  private static final String[] COLUMN_NAMES = {"Лекарство", "Количество", "Сумма"};

  public OrderDetailEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, OrderDetail entity) {
    vector.add(entity.getMedicine().getName());
    vector.add(entity.getCount());
    vector.add(entity.getAmount());
  }
}
