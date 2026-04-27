package com.tissenza.tissenza_backend.modules.produit.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertisseur pour gérer les champs JSON dans PostgreSQL
 * Convertit automatiquement les objets Java en JSON et vice-versa
 */
@Converter
public class JsonStringConverter implements AttributeConverter<String, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        
        try {
            // Si c'est déjà une chaîne JSON valide, la retourner directement
            if (attribute.trim().startsWith("{") && attribute.trim().endsWith("}")) {
                return attribute;
            }
            
            // Sinon, créer un objet JSON simple
            return "{\"value\":\"" + attribute + "\"}";
        } catch (Exception e) {
            // En cas d'erreur, retourner une valeur par défaut
            return "{}";
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        return dbData;
    }
}
