package ru.stepanov.pharmacymanager.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс {@code OrderDetail} представляет детальную информацию о заказе.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class OrderDetail {

  /**
   * Идентификатор.
   */
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Заказ, которому принадлежит детальная информация.
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  /**
   * Лекарство.
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "medicine_id")
  private Medicine medicine;

  /**
   * Количество единиц купленного лекарства.
   */
  @NotNull
  private Integer count;

  /**
   * Сумма покупки за лекарство.
   */
  @NotNull
  private BigDecimal amount;
}
