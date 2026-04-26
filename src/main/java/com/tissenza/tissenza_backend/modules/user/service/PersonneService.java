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
                    if (personneDetails.getNom() != null) {
                        personne.setNom(personneDetails.getNom());
                    }
                    if (personneDetails.getPrenom() != null) {
                        personne.setPrenom(personneDetails.getPrenom());
                    }
                    if (personneDetails.getAdresse() != null) {
                        personne.setAdresse(personneDetails.getAdresse());
                    }
                    if (personneDetails.getPhotoProfil() != null) {
                        personne.setPhotoProfil(personneDetails.getPhotoProfil());
                    }
                    if (personneDetails.getVille() != null) {
                        personne.setVille(personneDetails.getVille());
                    }
                    return personneRepository.save(personne);
                })
                .orElseThrow(() -> new RuntimeException("Personne not found with id: " + id));
    }

    public Personne updatePhotoProfil(Long id, String photoUrl) {
        return personneRepository.findById(id)
                .map(personne -> {
                    personne.setPhotoProfil(photoUrl);
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
