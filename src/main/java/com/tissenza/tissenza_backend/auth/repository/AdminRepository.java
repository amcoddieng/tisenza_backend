package com.tissenza.tissenza_backend.auth.repository;

import com.tissenza.tissenza_backend.auth.entity.admin.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrateur, Long> {
    
    Optional<Administrateur> findByEmail(String email);
    
    Optional<Administrateur> findByTelephone(String telephone);
    
    List<Administrateur> findByStatut(String statut);
    
    List<Administrateur> findByDeuxFaActive(boolean deuxFaActive);
    
    @Query("SELECT a FROM Administrateur a WHERE a.deuxFaActive = true")
    List<Administrateur> findWithTwoFactorEnabled();
    
    boolean existsByEmail(String email);
    
    boolean existsByTelephone(String telephone);
}
