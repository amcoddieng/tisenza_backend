package com.tissenza.tissenza_backend.modules.boutique.repository;

import com.tissenza.tissenza_backend.modules.boutique.entity.Document;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByPersonne(Personne personne);

    List<Document> findByPersonneId(Long personneId);

    Optional<Document> findByPersonneAndType(Personne personne, Document.Type type);

    List<Document> findByType(Document.Type type);

    List<Document> findByValidated(Boolean validated);

    List<Document> findByPersonneAndValidated(Personne personne, Boolean validated);

    @Query("SELECT d FROM Document d WHERE d.url LIKE %:keyword%")
    List<Document> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.type = :type")
    long countByType(@Param("type") Document.Type type);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.validated = :validated")
    long countByValidated(@Param("validated") Boolean validated);

    @Query("SELECT d.type, COUNT(d) FROM Document d GROUP BY d.type")
    List<Object[]> getStatisticsByType();

    @Query("SELECT d.validated, COUNT(d) FROM Document d GROUP BY d.validated")
    List<Object[]> getStatisticsByValidation();

    boolean existsByPersonneAndType(Personne personne, Document.Type type);

    @Query("SELECT d FROM Document d WHERE d.personne.id = :personneId AND d.type = :type")
    Optional<Document> findByPersonneIdAndType(@Param("personneId") Long personneId, @Param("type") Document.Type type);
}
