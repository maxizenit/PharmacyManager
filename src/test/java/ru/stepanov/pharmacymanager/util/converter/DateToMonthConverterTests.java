package ru.stepanov.pharmacymanager.util.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class DateToMonthConverterTests {

  @Test
  public void convertTests() {
    assertEquals(1, DateToMonthConverter.convert(new Date(2022 - 1900, Calendar.JANUARY, 1)));
    assertEquals(11, DateToMonthConverter.convert(new Date(2022 - 1900, Calendar.NOVEMBER, 1)));
    assertEquals(5, DateToMonthConverter.convert(new Date(2022 - 1900, Calendar.MAY, 1)));
  }
}
