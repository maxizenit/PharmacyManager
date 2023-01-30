package ru.stepanov.pharmacymanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepanov.pharmacymanager.entity.MedicinePurpose;

/**
 * Репозиторий для назначений лекарств.
 */
@Repository
public interface MedicinePurposeRepository extends JpaRepository<MedicinePurpose, Integer> {

  List<MedicinePurpose> getAllByMedicineId(Integer medicineId);

  MedicinePurpose getByMedicineIdAndDiseaseId(Integer medicineId, Integer diseaseId);
}
