package ru.stepanov.pharmacymanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepanov.pharmacymanager.entity.OrderDetail;

/**
 * Репозиторий для детальной информации о заказе.
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

  List<OrderDetail> getAllByOrderId(Integer orderId);
}
