package ru.stepanov.pharmacymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepanov.pharmacymanager.entity.Disease;

/**
 * Репозиторий для болезней.
 */
@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Integer> {

}
