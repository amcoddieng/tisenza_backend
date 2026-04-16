# Gestion des Erreurs - Exemples et Tests

## Structure de Gestion des Erreurs

### 1. Classes d'Exception Personnalisées

- **`BusinessException`** - Erreurs métier (email déjà utilisé, etc.)
- **`ResourceNotFoundException`** - Ressource non trouvée
- **`ValidationException`** - Erreurs de validation des données

### 2. ApiResponse Wrapper

```json
{
  "success": true,
  "message": "Opération réussie",
  "data": { ... },
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes"
}
```

### 3. GlobalExceptionHandler

Gère toutes les exceptions de manière centralisée avec des réponses HTTP appropriées.

---

## Exemples de Tests d'Erreurs

### 1. Test de Création de Compte avec Email Existant

```bash
# Créer un premier compte
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{
    "personneId": 1,
    "email": "test@example.com",
    "motDePasse": "password123",
    "telephone": "+221771234567",
    "role": "CLIENT"
  }'

# Tenter de créer un compte avec le même email (doit échouer)
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{
    "personneId": 2,
    "email": "test@example.com",
    "motDePasse": "password456",
    "telephone": "+221777654321",
    "role": "CLIENT"
  }'
```

**Réponse attendue (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email déjà utilisé: test@example.com",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes"
}
```

### 2. Test de Recherche de Compte Inexistant

```bash
curl -X GET http://localhost:8081/api/comptes/99999
```

**Réponse attendue (404 Not Found):**
```json
{
  "success": false,
  "message": "Compte non trouvé(e) avec l'identifiant: 99999",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes/99999"
}
```

### 3. Test de Validation des Données

```bash
# Créer un compte avec des données invalides
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{
    "personneId": null,
    "email": "email-invalide",
    "motDePasse": "",
    "telephone": "123",
    "role": "ROLE_INEXISTANT"
  }'
```

**Réponse attendue (400 Bad Request):**
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

### 4. Test de Violation de Contrainte de Base de Données

```bash
# Tenter de créer un compte avec un email qui existe déjà en base
# (si la validation métier est contournée)
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{
    "personneId": 3,
    "email": "test@example.com",
    "motDePasse": "password789",
    "telephone": "+221779876543",
    "role": "CLIENT"
  }'
```

**Réponse attendue (409 Conflict):**
```json
{
  "success": false,
  "message": "Cet email est déjà utilisé",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/comptes"
}
```

---

## Codes HTTP Utilisés

| Code | Type | Description |
|------|------|-------------|
| **200** | Success | Opération réussie |
| **201** | Success | Ressource créée |
| **400** | Client Error | Erreur de validation / Business Exception |
| **404** | Client Error | Ressource non trouvée |
| **409** | Client Error | Conflit de données (unicité) |
| **500** | Server Error | Erreur technique interne |

---

## Structure des Réponses d'Erreur

### Erreur Simple
```json
{
  "success": false,
  "message": "Message d'erreur descriptif",
  "data": null,
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/endpoint"
}
```

### Erreur avec Détails
```json
{
  "success": false,
  "message": "Erreurs de validation des données d'entrée",
  "data": {
    "fieldErrors": {
      "champ1": "message d'erreur 1",
      "champ2": "message d'erreur 2"
    }
  },
  "timestamp": "2026-04-13T11:30:00",
  "path": "/api/endpoint"
}
```

---

## Logging des Erreurs

Le système journalise automatiquement:
- **WARN**: Erreurs métier et de validation
- **ERROR**: Erreurs techniques et inattendues

Exemple de log:
```
2026-04-13 11:30:00 WARN  --- [http-nio-8081-exec-1] c.t.t.e.GlobalExceptionHandler : Business error: EMAIL_ALREADY_EXISTS - Email déjà utilisé: test@example.com
```

---

## Bonnes Pratiques

1. **Messages clairs**: Utiliser des messages d'erreur explicites en français
2. **Codes d'erreur**: Utiliser des codes d'erreur spécifiques pour les erreurs métier
3. **Validation**: Valider les données en entrée avant traitement
4. **Logging**: Journaliser les erreurs pour le débogage
5. **Consistance**: Utiliser la structure ApiResponse pour toutes les réponses

---

## Tests Automatisés

Pour tester automatiquement la gestion des erreurs:

```bash
# Script de test des erreurs
#!/bin/bash

echo "Test 1: Email déjà utilisé"
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{"personneId":1,"email":"test@example.com","motDePasse":"pass","telephone":"+221771234567","role":"CLIENT"}' \
  -w "\nHTTP Status: %{http_code}\n"

echo "Test 2: Compte inexistant"
curl -X GET http://localhost:8081/api/comptes/99999 \
  -w "\nHTTP Status: %{http_code}\n"

echo "Test 3: Données invalides"
curl -X POST http://localhost:8081/api/comptes \
  -H "Content-Type: application/json" \
  -d '{"personneId":null,"email":"","motDePasse":"","telephone":"","role":""}' \
  -w "\nHTTP Status: %{http_code}\n"
```

---

*Dernière mise à jour: 13 Avril 2026*
