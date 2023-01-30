package ru.stepanov.pharmacymanager.service;

import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepanov.pharmacymanager.entity.Medicine;
import ru.stepanov.pharmacymanager.entity.Order;
import ru.stepanov.pharmacymanager.entity.OrderDetail;
import ru.stepanov.pharmacymanager.repository.OrderDetailRepository;

/**
 * Сервис для работы с детальной информацией о заказе.
 */
@Service
public class OrderDetailService extends AbstractService<OrderDetail, OrderDetailRepository> {

  private final MedicineService medicineService;

  @Autowired
  public OrderDetailService(OrderDetailRepository repository, MedicineService medicineService) {
    super(repository);
    this.medicineService = medicineService;
  }

  @Override
  public void save(@NonNull OrderDetail entity) {
    super.save(entity);
    Medicine medicine = entity.getMedicine();
    medicine.setCount(medicine.getCount() - entity.getCount());
    medicineService.save(medicine);
  }

  /**
   * Возвращает список детальной информации о заказе по заказу.
   *
   * @param order заказ
   * @return список детальной информации о заказе
   */
  public List<OrderDetail> getOrderDetailsByOrder(Order order) {
    return repository.getAllByOrderId(order.getId());
  }
}