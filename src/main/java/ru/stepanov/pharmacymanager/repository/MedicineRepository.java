package ru.stepanov.pharmacymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepanov.pharmacymanager.entity.Medicine;

/**
 * Репозиторий для лекарств.
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {

}
