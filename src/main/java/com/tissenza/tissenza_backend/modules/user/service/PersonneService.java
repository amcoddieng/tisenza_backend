package com.tissenza.tissenza_backend.modules.user.service;

import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.repository.PersonneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonneService {

    private final PersonneRepository personneRepository;

    public Personne createPersonne(Personne personne) {
        return personneRepository.save(personne);
    }

    public Optional<Personne> getPersonneById(Long id) {
        return personneRepository.findById(id);
    }

    public List<Personne> getAllPersonnes() {
        return personneRepository.findAll();
    }

    public Personne updatePersonne(Long id, Personne personneDetails) {
        return personneRepository.findById(id)
                .map(personne -> {
                    personne.setNom(personneDetails.getNom());
                    personne.setPrenom(personneDetails.getPrenom());
                    personne.setAdresse(personneDetails.getAdresse());
                    personne.setPhotoProfil(personneDetails.getPhotoProfil());
                    personne.setVille(personneDetails.getVille());
                    return personneRepository.save(personne);
                })
                .orElseThrow(() -> new RuntimeException("Personne not found with id: " + id));
    }

    public void deletePersonne(Long id) {
        personneRepository.deleteById(id);
    }

    public List<Personne> searchByNom(String nom) {
        return personneRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Personne> searchByPrenom(String prenom) {
        return personneRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    public List<Personne> searchByVille(String ville) {
        return personneRepository.findByVilleIgnoreCase(ville);
    }

    public List<Personne> searchByKeyword(String keyword) {
        return personneRepository.searchByKeyword(keyword);
    }

    public long countByVille(String ville) {
        return personneRepository.countByVille(ville);
    }

    public boolean existsById(Long id) {
        return personneRepository.existsById(id);
    }
}
