package ru.stepanov.pharmacymanager.entity;

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
 * Класс {@code MedicinePurpose} представляет назначение лекарства.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class MedicinePurpose {

  /**
   * Идентификатор.
   */
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Лекарство.
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "medicine_id")
  private Medicine medicine;

  /**
   * Болезнь, от которой лечит лекарство.
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "disease_id")
  private Disease disease;
}
