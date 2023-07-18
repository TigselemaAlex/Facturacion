package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Product;
import com.capibaracode.backend.domain.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String newName, UUID id);

    List<Product> findAllBySupplier(Supplier supplier);

}
