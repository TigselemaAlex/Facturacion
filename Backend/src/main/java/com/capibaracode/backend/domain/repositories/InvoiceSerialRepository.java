package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.InvoiceSerial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceSerialRepository extends JpaRepository<InvoiceSerial, Long> {

}
