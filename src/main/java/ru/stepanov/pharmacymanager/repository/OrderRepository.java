package ru.stepanov.pharmacymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepanov.pharmacymanager.entity.Order;

/**
 * Репозиторий для заказов.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
