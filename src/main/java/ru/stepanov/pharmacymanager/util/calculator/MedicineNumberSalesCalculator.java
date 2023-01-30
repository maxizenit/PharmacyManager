package ru.stepanov.pharmacymanager.util.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.stepanov.pharmacymanager.dto.MedicineNumberSalesDTO;
import ru.stepanov.pharmacymanager.entity.OrderDetail;

/**
 * Калькулятор для подсчёта количества продаж различных лекарств.
 */
public class MedicineNumberSalesCalculator {

  /**
   * Подсчитывает количество продаж различных лекарств.
   *
   * @param orderDetails детальная информация обо всех заказах
   * @return список объектов, каждый из которых хранит в себе лекарство и количество его продаж
   */
  public static List<MedicineNumberSalesDTO> calculate(List<OrderDetail> orderDetails) {
    List<MedicineNumberSalesDTO> result = new ArrayList<>();

    orderDetails.forEach(od -> {
      Optional<MedicineNumberSalesDTO> mnsOptional = result.stream()
          .filter(m -> m.getMedicine().equals(od.getMedicine())).findFirst();

      if (mnsOptional.isPresent()) {
        mnsOptional.get().setCount(mnsOptional.get().getCount() + od.getCount());
      } else {
        MedicineNumberSalesDTO mns = new MedicineNumberSalesDTO();
        mns.setMedicine(od.getMedicine());
        mns.setCount(od.getCount());
        result.add(mns);
      }
    });

    return result;
  }
}
