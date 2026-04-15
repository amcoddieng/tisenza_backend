# Format des Réponses API - ApiResponse

## Structure Standardisée

Toutes les réponses API utilisent maintenant le format `ApiResponse`:

```json
{
  "success": true,
  "message": "Message descriptif",
  "data": { ... },
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/endpoint"
}
```

## Exemples de Réponses

### Succès - Création
```http
POST /api/comptes
Content-Type: application/json

{
  "personneId": 1,
  "email": "test@example.com",
  "motDePasse": "password123",
  "telephone": "+221771234567",
  "role": "CLIENT"
}
```

**Réponse (201 Created):**
```json
{
  "success": true,
  "message": "Compte créé avec succès",
  "data": {
    "id": 1,
    "personneId": 1,
    "email": "test@example.com",
    "telephone": "+221771234567",
    "role": "CLIENT",
    "statut": "ACTIF",
    "isVerified": false,
    "createdAt": "2026-04-13T11:30:00"
  },
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes"
}
```

### Succès - Lecture
```http
GET /api/comptes/1
```

**Réponse (200 OK):**
```json
{
  "success": true,
  "message": "Compte trouvé",
  "data": {
    "id": 1,
    "personneId": 1,
    "email": "test@example.com",
    "telephone": "+221771234567",
    "role": "CLIENT",
    "statut": "ACTIF",
    "isVerified": false,
    "createdAt": "2026-04-13T11:30:00"
  },
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes/1"
}
```

### Succès - Liste
```http
GET /api/personnes
```

**Réponse (200 OK):**
```json
{
  "success": true,
  "message": "Liste des personnes récupérée",
  "data": [
    {
      "id": 1,
      "nom": "Doe",
      "prenom": "John",
      "email": "john.doe@example.com",
      "telephone": "+221771234567",
      "dateNaissance": "1990-05-15",
      "adresse": "Dakar, Sénégal",
      "createdAt": "2026-04-13T11:30:00"
    }
  ],
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/personnes"
}
```

### Succès - Recherche
```http
GET /api/personnes/search/nom?nom=Doe
```

**Réponse (200 OK):**
```json
{
  "success": true,
  "message": "Résultats de recherche pour: Doe",
  "data": [
    {
      "id": 1,
      "nom": "Doe",
      "prenom": "John",
      "email": "john.doe@example.com",
      "telephone": "+221771234567",
      "dateNaissance": "1990-05-15",
      "adresse": "Dakar, Sénégal",
      "createdAt": "2026-04-13T11:30:00"
    }
  ],
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/personnes/search/nom"
}
```

### Succès - Suppression
```http
DELETE /api/personnes/1
```

**Réponse (200 OK):**
```json
{
  "success": true,
  "message": "Personne supprimée avec succès",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/personnes/1"
}
```

### Erreur - Ressource Non Trouvée
```http
GET /api/comptes/99999
```

**Réponse (404 Not Found):**
```json
{
  "success": false,
  "message": "Compte non trouvé(e) avec l'identifiant: 99999",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes/99999"
}
```

### Erreur - Validation
```http
POST /api/comptes
Content-Type: application/json

{
  "personneId": null,
  "email": "email-invalide",
  "motDePasse": "",
  "telephone": "123",
  "role": "ROLE_INEXISTANT"
}
```

**Réponse (400 Bad Request):**
```json
{
  "success": false,
  "message": "Erreurs de validation des données d'entrée",
  "data": {
    "fieldErrors": {
      "personneId": "ne doit pas être nul",
      "email": "doit être une adresse email valide",
      "motDePasse": "ne doit pas être vide",
      "role": "valeur invalide"
    }
  },
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes"
}
```

### Erreur - Conflit
```http
POST /api/comptes
Content-Type: application/json

{
  "personneId": 2,
  "email": "test@example.com",
  "motDePasse": "password456",
  "telephone": "+221777654321",
  "role": "CLIENT"
}
```

**Réponse (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email déjà utilisé: test@example.com",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes"
}
```

## Messages Personnalisés par Module

### User Management
- **Création**: "Compte créé avec succès"
- **Lecture**: "Compte trouvé"
- **Mise à jour**: "Compte mis à jour avec succès"
- **Suppression**: "Compte supprimé avec succès"
- **Recherche**: "Résultats de recherche pour: {keyword}"

### Boutique Management
- **Création**: "Boutique créée avec succès"
- **Lecture**: "Boutique trouvée"
- **Validation**: "Boutique validée avec succès"
- **Refus**: "Boutique refusée"

### Product Management
- **Création**: "Produit créé avec succès"
- **Lecture**: "Produit trouvé"
- **Activation**: "Produit activé avec succès"
- **Désactivation**: "Produit désactivé avec succès"

### Stock Management
- **Ajout stock**: "Stock ajouté avec succès"
- **Retrait stock**: "Stock retiré avec succès"
- **Mouvement**: "Mouvement de stock enregistré"

## Avantages du Format ApiResponse

1. **Consistance**: Toutes les réponses ont la même structure
2. **Clarté**: `success: true/false` indique immédiatement le résultat
3. **Informations**: Timestamp et path pour le débogage
4. **Messages**: Messages descriptifs en français
5. **Flexibilité**: `data` peut contenir n'importe quel type d'objet

---

*Dernière mise à jour: 13 Avril 2026*
