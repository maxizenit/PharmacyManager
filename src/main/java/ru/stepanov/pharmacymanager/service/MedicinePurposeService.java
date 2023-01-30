package ru.stepanov.pharmacymanager.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepanov.pharmacymanager.entity.Disease;
import ru.stepanov.pharmacymanager.entity.Medicine;
import ru.stepanov.pharmacymanager.entity.MedicinePurpose;
import ru.stepanov.pharmacymanager.repository.MedicinePurposeRepository;

/**
 * Сервис для работы с назначениями лекарств.
 */
@Service
public class MedicinePurposeService extends
    AbstractService<MedicinePurpose, MedicinePurposeRepository> {

  @Autowired
  public MedicinePurposeService(MedicinePurposeRepository repository) {
    super(repository);
  }

  /**
   * Возвращает все болезни, от которых лечит лекарство.
   *
   * @param medicine лекарство
   * @return список болезней, от которых лечит лекарство
   */
  public List<Disease> getAllDiseasesByMedicine(Medicine medicine) {
    return repository.getAllByMedicineId(medicine.getId()).stream().map(MedicinePurpose::getDisease)
        .toList();
  }

  /**
   * Возвращает назначение лекарства по лекарству и болезни.
   *
   * @param medicine лекарство
   * @param disease  болезнь
   * @return назначение лекарства
   */
  public MedicinePurpose getByMedicineAndDisease(Medicine medicine, Disease disease) {
    return repository.getByMedicineIdAndDiseaseId(medicine.getId(), disease.getId());
  }
}
