package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findAllByActiveTrue();
}
