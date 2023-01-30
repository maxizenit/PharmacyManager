package ru.stepanov.pharmacymanager.util.converter;

import java.time.ZoneId;
import java.util.Date;

/**
 * Конвертер даты в месяц.
 */
public class DateToMonthConverter {

  /**
   * Возвращает номер месяца в дате.
   *
   * @param date дата
   * @return номер месяца
   */
  public static Integer convert(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
  }
}
