package ru.stepanov.pharmacymanager.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code Medicine} представляет лекарство.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Medicine {

  /**
   * Идентификатор.
   */
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Название.
   */
  @NotNull
  private String name;

  /**
   * Цена.
   */
  @NotNull
  @Min(0)
  private BigDecimal price;

  /**
   * Количество.
   */
  @NotNull
  @Min(0)
  private Integer count;
}
