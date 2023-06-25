package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    boolean existsByIdentification(String identification);

}
