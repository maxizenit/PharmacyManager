package ru.stepanov.pharmacymanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stepanov.pharmacymanager.entity.Disease;
import ru.stepanov.pharmacymanager.repository.DiseaseRepository;

/**
 * Сервис для работы с болезнями.
 */
@Service
public class DiseaseService extends AbstractService<Disease, DiseaseRepository> {

  @Autowired
  public DiseaseService(DiseaseRepository repository) {
    super(repository);
  }
}
