package ru.stepanov.pharmacymanager.util.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.stepanov.pharmacymanager.dto.AmountByMonthDTO;
import ru.stepanov.pharmacymanager.entity.OrderDetail;
import ru.stepanov.pharmacymanager.util.converter.DateToMonthConverter;
import ru.stepanov.pharmacymanager.util.converter.DateToYearConverter;

/**
 * Калькулятор для подсчёта сумм продаж по месяцам.
 */
public class AmountByMonthCalculator {

  /**
   * Подсчитывает сумму продаж по месяцам.
   *
   * @param orderDetails детальная информация обо всех заказах
   * @return список объектов, каждый из которых хранит в себе информацию о месяце, годе и сумме
   * продаж
   */
  public static List<AmountByMonthDTO> calculateAmountByMonths(List<OrderDetail> orderDetails) {
    List<AmountByMonthDTO> result = new ArrayList<>();

    orderDetails.forEach(od -> {
      Integer month = DateToMonthConverter.convert(od.getOrder().getDate());
      Integer year = DateToYearConverter.convert(od.getOrder().getDate());
      Optional<AmountByMonthDTO> abmOptional = result.stream()
          .filter(a -> a.getMonth().equals(month) && a.getYear().equals(year)).findFirst();

      if (abmOptional.isPresent()) {
        abmOptional.get().setAmount(abmOptional.get().getAmount().add(od.getAmount()));
      } else {
        AmountByMonthDTO abm = new AmountByMonthDTO();
        abm.setAmount(od.getAmount());
        abm.setMonth(month);
        abm.setYear(year);
        result.add(abm);
      }
    });

    return result;
  }
}
