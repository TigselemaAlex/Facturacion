package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    Optional<Promotion> findByDescription(String description);

    boolean existsByDescription(String descriptionName);

    boolean existsByDescriptionAndIdNot(String newDescriptionName, UUID id);

}
