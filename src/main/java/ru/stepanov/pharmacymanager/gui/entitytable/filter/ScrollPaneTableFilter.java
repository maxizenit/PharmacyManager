package ru.stepanov.pharmacymanager.gui.entitytable.filter;

/**
 * Интерфейс фильтра сущностей для {@code ScrollPaneTable}.
 *
 * @param <T> класс сущности
 */
public interface ScrollPaneTableFilter<T> {

  /**
   * Возвращает признак фильтрации сущности.
   *
   * @param t сущность
   * @return {@code true}, если сущность удовлетворяет фильтру
   */
  boolean isFiltered(T t);

  /**
   * Возвращает признак пустоты фильтра.
   *
   * @return {@code true}, если все поля фильтра пустые
   */
  boolean isEmpty();
}
