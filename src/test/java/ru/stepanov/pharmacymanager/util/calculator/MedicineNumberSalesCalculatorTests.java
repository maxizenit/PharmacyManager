package ru.stepanov.pharmacymanager.util.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.stepanov.pharmacymanager.dto.MedicineNumberSalesDTO;
import ru.stepanov.pharmacymanager.entity.Medicine;
import ru.stepanov.pharmacymanager.entity.OrderDetail;

public class MedicineNumberSalesCalculatorTests {

  @Test
  public void calculateTests() {
    Medicine medicine1 = new Medicine();
    medicine1.setId(1);

    Medicine medicine2 = new Medicine();
    medicine2.setId(2);

    List<OrderDetail> ods = new ArrayList<>();

    OrderDetail od = new OrderDetail();
    od.setMedicine(medicine1);
    od.setCount(2);
    ods.add(od);

    od = new OrderDetail();
    od.setMedicine(medicine1);
    od.setCount(5);
    ods.add(od);

    od = new OrderDetail();
    od.setMedicine(medicine2);
    od.setCount(4);
    ods.add(od);

    List<MedicineNumberSalesDTO> expected = new ArrayList<>();

    MedicineNumberSalesDTO mns = new MedicineNumberSalesDTO();
    mns.setMedicine(medicine1);
    mns.setCount(7);
    expected.add(mns);

    mns = new MedicineNumberSalesDTO();
    mns.setMedicine(medicine2);
    mns.setCount(4);
    expected.add(mns);

    List<MedicineNumberSalesDTO> actual = MedicineNumberSalesCalculator.calculate(ods);

    assertEquals(expected, actual);
  }
}
