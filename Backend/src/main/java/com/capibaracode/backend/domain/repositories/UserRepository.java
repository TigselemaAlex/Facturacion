package com.capibaracode.backend.domain.repositories;

import com.capibaracode.backend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
