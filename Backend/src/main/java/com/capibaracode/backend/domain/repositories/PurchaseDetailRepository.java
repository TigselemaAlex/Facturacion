package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, UUID> {

}
