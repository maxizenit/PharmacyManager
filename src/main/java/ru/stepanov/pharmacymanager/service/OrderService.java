package ru.stepanov.pharmacymanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepanov.pharmacymanager.entity.Order;
import ru.stepanov.pharmacymanager.repository.OrderRepository;

/**
 * Сервис для работы с заказами.
 */
@Service
public class OrderService extends AbstractService<Order, OrderRepository> {

  @Autowired
  public OrderService(OrderRepository repository) {
    super(repository);
  }
}
