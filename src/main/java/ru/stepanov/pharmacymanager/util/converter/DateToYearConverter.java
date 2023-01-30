package ru.stepanov.pharmacymanager.util.converter;

import java.time.ZoneId;
import java.util.Date;

/**
 * Конвертер даты в год.
 */
public class DateToYearConverter {

  /**
   * Возвращает номер года в дате.
   *
   * @param date дата
   * @return номер года
   */
  public static Integer convert(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
  }
}
