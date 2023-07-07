package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String newName, UUID id);

}
