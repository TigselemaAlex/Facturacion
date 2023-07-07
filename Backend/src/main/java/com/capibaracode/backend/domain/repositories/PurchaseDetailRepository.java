package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Product;
import com.capibaracode.backend.domain.entities.Purchase;
import com.capibaracode.backend.domain.entities.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, UUID> {

    boolean existsByProductAndPurchase(Product product, Purchase purchase);

    List<PurchaseDetail> findByPurchase(Purchase purchase);

}
