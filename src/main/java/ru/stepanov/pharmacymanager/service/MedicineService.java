package ru.stepanov.pharmacymanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepanov.pharmacymanager.entity.Medicine;
import ru.stepanov.pharmacymanager.repository.MedicineRepository;

/**
 * Сервис для работы с лекарствами.
 */
@Service
public class MedicineService extends AbstractService<Medicine, MedicineRepository> {

  @Autowired
  public MedicineService(MedicineRepository repository) {
    super(repository);
  }
}
