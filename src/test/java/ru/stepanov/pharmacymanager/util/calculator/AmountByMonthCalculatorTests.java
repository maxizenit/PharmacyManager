package ru.stepanov.pharmacymanager.util.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.stepanov.pharmacymanager.dto.AmountByMonthDTO;
import ru.stepanov.pharmacymanager.entity.Order;
import ru.stepanov.pharmacymanager.entity.OrderDetail;

public class AmountByMonthCalculatorTests {

  @Test
  public void calculateAmountByMonthsTests() {
    Order firstOrder = new Order();
    firstOrder.setDate(new Date(2022 - 1900, Calendar.JANUARY, 1));

    Order secondOrder = new Order();
    secondOrder.setDate(new Date(2022 - 1900, Calendar.FEBRUARY, 1));

    List<OrderDetail> ods = new ArrayList<>();

    OrderDetail od = new OrderDetail();
    od.setOrder(firstOrder);
    od.setAmount(BigDecimal.valueOf(2));
    ods.add(od);

    od = new OrderDetail();
    od.setOrder(firstOrder);
    od.setAmount(BigDecimal.valueOf(3));
    ods.add(od);

    od = new OrderDetail();
    od.setOrder(secondOrder);
    od.setAmount(BigDecimal.valueOf(6));
    ods.add(od);

    List<AmountByMonthDTO> expected = new ArrayList<>();

    AmountByMonthDTO abm = new AmountByMonthDTO();
    abm.setMonth(1);
    abm.setYear(2022);
    abm.setAmount(BigDecimal.valueOf(5));
    expected.add(abm);

    abm = new AmountByMonthDTO();
    abm.setMonth(2);
    abm.setYear(2022);
    abm.setAmount(BigDecimal.valueOf(6));
    expected.add(abm);

    List<AmountByMonthDTO> actual = AmountByMonthCalculator.calculateAmountByMonths(ods);

    assertEquals(expected, actual);
  }
}
