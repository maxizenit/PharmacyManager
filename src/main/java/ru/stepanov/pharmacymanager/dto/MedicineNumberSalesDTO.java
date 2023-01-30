package ru.stepanov.pharmacymanager.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.stepanov.pharmacymanager.entity.Medicine;

/**
 * Класс {@code MedicineNumberSalesDTO} представляет запись в таблице количества продаж конкретных
 * лекарств.
 */
@Getter
@Setter
@EqualsAndHashCode
public class MedicineNumberSalesDTO {

  /**
   * Лекарство.
   */
  private Medicine medicine;

  /**
   * Количество продаж.
   */
  private Integer count;
}
