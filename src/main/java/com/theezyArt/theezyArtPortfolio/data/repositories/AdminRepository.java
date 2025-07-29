package com.theezyArt.theezyArtPortfolio.data.repositories;

import com.theezyArt.theezyArtPortfolio.data.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findAdminByEmail(String email);
    boolean existsByEmail(String email);
}