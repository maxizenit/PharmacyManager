package ru.stepanov.pharmacymanager.dto;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code AmountByMonthDTO} представляет запись в таблице сумм продаж по месяцам.
 */
@Getter
@Setter
@EqualsAndHashCode
public class AmountByMonthDTO {

  /**
   * Месяц.
   */
  private Integer month;

  /**
   * Год.
   */
  private Integer year;

  /**
   * Сумма продаж.
   */
  private BigDecimal amount;
}
