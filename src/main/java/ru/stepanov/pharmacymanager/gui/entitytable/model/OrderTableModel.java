package ru.stepanov.pharmacymanager.gui.entitytable.model;

import java.text.SimpleDateFormat;
import java.util.Vector;
import ru.stepanov.pharmacymanager.entity.Order;

public class OrderTableModel extends EntityTableModel<Order> {

  private static final String[] COLUMN_NAMES = {"№", "Дата"};

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

  public OrderTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Order entity) {
    vector.add(entity.getId());
    vector.add(DATE_FORMAT.format(entity.getDate()));
  }
}
