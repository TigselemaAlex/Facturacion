package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByCategory(String category);

}
