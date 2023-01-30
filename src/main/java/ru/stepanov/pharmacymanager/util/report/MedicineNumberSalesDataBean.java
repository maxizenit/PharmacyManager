package ru.stepanov.pharmacymanager.util.report;

import lombok.Getter;
import lombok.Setter;

/**
 * Датабин для отчёта количества продаж лекарств.
 */
@Getter
@Setter
public class MedicineNumberSalesDataBean {

  /**
   * Название лекарства.
   */
  private String name;

  /**
   * Количество продаж.
   */
  private String count;
}
