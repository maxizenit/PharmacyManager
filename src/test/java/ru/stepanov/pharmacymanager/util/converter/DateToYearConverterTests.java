package ru.stepanov.pharmacymanager.util.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class DateToYearConverterTests {

  @Test
  public void convertTests() {
    assertEquals(2022, DateToYearConverter.convert(new Date(2022 - 1900, Calendar.JANUARY, 1)));
    assertEquals(2000, DateToYearConverter.convert(new Date(2000 - 1900, Calendar.JANUARY, 1)));
    assertEquals(2035, DateToYearConverter.convert(new Date(2035 - 1900, Calendar.JANUARY, 1)));
  }
}
