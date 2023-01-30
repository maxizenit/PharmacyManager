package ru.stepanov.pharmacymanager.gui.entitytable.filter;

import java.math.BigDecimal;
import lombok.Setter;
import ru.stepanov.pharmacymanager.entity.Disease;
import ru.stepanov.pharmacymanager.entity.Medicine;
import ru.stepanov.pharmacymanager.service.MedicinePurposeService;

/**
 * Фильтр для лекарств.
 */
@Setter
public class MedicineScrollPaneTableFilter implements ScrollPaneTableFilter<Medicine> {

  private final MedicinePurposeService medicinePurposeService;

  /**
   * Излечиваемая болезнь.
   */
  private Disease disease;

  /**
   * Минимальная цена.
   */
  private BigDecimal priceFrom;

  /**
   * Максимальная цена.
   */
  private BigDecimal priceTo;

  public MedicineScrollPaneTableFilter(MedicinePurposeService medicinePurposeService) {
    this.medicinePurposeService = medicinePurposeService;
  }

  @Override
  public boolean isFiltered(Medicine medicine) {
    if (disease != null && !medicinePurposeService.getAllDiseasesByMedicine(medicine)
        .contains(disease)) {
      return false;
    }

    if (priceFrom != null && medicine.getPrice().compareTo(priceFrom) < 0) {
      return false;
    }

    return priceTo == null || medicine.getPrice().compareTo(priceTo) <= 0;
  }

  @Override
  public boolean isEmpty() {
    return disease == null && priceFrom == null && priceTo == null;
  }
}
