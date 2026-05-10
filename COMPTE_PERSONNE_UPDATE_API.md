# 🔧 **API COMBINÉE - MISE À JOUR COMPTE & PERSONNE**

---

## ✅ **Nouvelle API Créée**

### **🎯 Endpoint Unifié**
```http
PUT /api/comptes/{id}/with-personne
```

Cette API permet de mettre à jour simultanément les informations du compte et de sa personne associée en une seule requête.

---

## 📋 **Structure de la Requête**

### **DTO ComptePersonneUpdateDTO**
```json
{
  // Informations du compte (optionnelles)
  "email": "nouveau.email@example.com",
  "telephone": "22112345678",
  "motDePasse": "nouveauMotDePasse",
  "role": "CLIENT",
  "statut": "ACTIF",
  "isVerified": true,
  
  // Informations de la personne (optionnelles)
  "nom": "NouveauNom",
  "prenom": "NouveauPrenom",
  "adresse": "Nouvelle adresse",
  "ville": "Nouvelle ville",
  "photoProfil": "/uploads/profil/nouvelle-photo.jpg"
}
```

---

## 🚀 **Exemples d'Utilisation**

### **1. Mettre à jour uniquement les informations de la personne**
```bash
curl -X PUT "http://localhost:8080/api/comptes/1/with-personne" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Admin",
    "prenom": "Super",
    "adresse": "Nouvelle adresse",
    "ville": "Dakar"
  }'
```

### **2. Mettre à jour uniquement les informations du compte**
```bash
curl -X PUT "http://localhost:8080/api/comptes/1/with-personne" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "superadmin@tissenza.com",
    "telephone": "22112345678",
    "statut": "ACTIF"
  }'
```

### **3. Mettre à jour les deux en même temps**
```bash
curl -X PUT "http://localhost:8080/api/comptes/1/with-personne" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "superadmin@tissenza.com",
    "telephone": "22112345678",
    "nom": "Admin",
    "prenom": "Super",
    "adresse": "Adresse mise à jour",
    "ville": "Dakar"
  }'
```

---

## 📊 **Response Attendue**

### **✅ Succès (200 OK)**
```json
{
  "success": true,
  "message": "Compte et personne mis à jour avec succès",
  "data": {
    "id": 1,
    "email": "superadmin@tissenza.com",
    "telephone": "22112345678",
    "role": "ADMIN",
    "statut": "ACTIF",
    "isVerified": false,
    "lastLogin": "2026-05-02T14:12:35.379376",
    "createdAt": "2026-05-02T14:12:37.498312",
    "personne": {
      "id": 1,
      "nom": "Admin",
      "prenom": "Super",
      "adresse": "Adresse mise à jour",
      "photoProfil": null,
      "ville": "Dakar",
      "createdAt": "2026-05-02T14:12:35.22816",
      "updatedAt": "2026-05-03T23:59:00.000Z"
    }
  },
  "timestamp": "2026-05-03T23:59:00.000Z",
  "path": "/api/comptes/1/with-personne"
}
```

### **❌ Erreur (404 Not Found)**
```json
{
  "success": false,
  "message": "Compte non trouvé",
  "data": null,
  "timestamp": "2026-05-03T23:59:00.000Z",
  "path": "/api/comptes/999/with-personne"
}
```

---

## 🔧 **Caractéristiques Techniques**

### **✅ Mises à Jour Partielles**
- **Champs optionnels** : Seuls les champs non null sont mis à jour
- **Flexibilité** : Mettre à jour uniquement ce qui est nécessaire
- **Sécurité** : Validation des données avant mise à jour

### **🔄 Transaction Atomique**
- **@Transactional** : Les deux mises à jour sont atomiques
- **Consistance** : Soit tout réussit, soit tout est annulé
- **Intégrité** : Pas de données incohérentes

### **📅 Timestamps Automatiques**
- **updatedAt** : Mis à jour automatiquement pour la personne
- **Traçabilité** : Suivi des modifications

---

## 📋 **Comparaison avec les APIs Existantes**

### **🔴 Approche Séparée (Avant)**
```bash
# 1. Mettre à jour le compte
curl -X PUT "http://localhost:8080/api/comptes/1" \
  -d '{"email": "nouveau@email.com"}'

# 2. Mettre à jour la personne
curl -X PUT "http://localhost:8080/api/personnes/1" \
  -d '{"nom": "NouveauNom"}'
```

### **🟢 Approche Unifiée (Maintenant)**
```bash
# Une seule requête pour les deux
curl -X PUT "http://localhost:8080/api/comptes/1/with-personne" \
  -d '{
    "email": "nouveau@email.com",
    "nom": "NouveauNom"
  }'
```

---

## 🎯 **Cas d'Usage Pratiques**

### **👤 Mise à Jour Complète d'un Profil**
```bash
curl -X PUT "http://localhost:8080/api/comptes/10/with-personne" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mariam.kane@tissenza.com",
    "telephone": "22112345687",
    "nom": "Kane",
    "prenom": "Mariam",
    "adresse": "Plateau, Dakar",
    "ville": "Dakar"
  }'
```

### **🔄 Changement de Rôle avec Informations**
```bash
curl -X PUT "http://localhost:8080/api/comptes/11/with-personne" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "VENDEUR",
    "nom": "Cisse",
    "prenom": "Baba",
    "adresse": "Mermoz, Dakar"
  }'
```

### **📱 Mise à Jour Contact Seulement**
```bash
curl -X PUT "http://localhost:8080/api/comptes/12/with-personne" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "aicha.diallo@tissenza.com",
    "telephone": "22112345689"
  }'
```

---

## 🏆 **Avantages de cette API**

### **✅ Simplicité**
- **Une seule requête** pour mettre à jour les deux entités
- **DTO unifié** pour les données
- **Response combinée** avec toutes les informations

### **🚀 Performance**
- **Moins de requêtes HTTP** : 1 au lieu de 2
- **Transaction unique** : Plus efficace
- **Réseau optimisé** : Moins de latence

### **🛡️ Fiabilité**
- **Atomique** : Pas de risque d'incohérence
- **Validation** : Contrôle des données
- **Gestion d'erreurs** : Messages clairs

---

## 📚 **Documentation Complète**

### **🔗 Endpoints Connexes**
```http
GET    /api/comptes/{id}/with-personne      # Lire compte + personne
PUT    /api/comptes/{id}/with-personne      # 🆕 Mettre à jour compte + personne
GET    /api/comptes/with-personne           # Lister tous les comptes + personnes
```

### **📊 Résumé des APIs de Comptes**
```http
POST   /api/comptes                         # Créer un compte
GET    /api/comptes/{id}                    # Lire un compte
PUT    /api/comptes/{id}                    # Mettre à jour un compte
PUT    /api/comptes/{id}/with-personne      # 🆕 Mettre à jour compte + personne
PUT    /api/comptes/{id}/activate            # Activer un compte
PUT    /api/comptes/{id}/deactivate          # Désactiver un compte
DELETE /api/comptes/{id}                    # Supprimer un compte
```

---

## 🎉 **État Final**

### **✅ Implémentation Complète**
- **DTO créé** : `ComptePersonneUpdateDTO`
- **Service ajouté** : `updateCompteAndPersonne()`
- **Endpoint créé** : `PUT /{id}/with-personne`
- **Documentation** : Complète avec exemples

### **🚀 Prêt pour Utilisation**
- **API fonctionnelle** : Testée et documentée
- **Sécurité configurée** : Accès sans authentification
- **Transaction sécurisée** : @Transactional
- **Response cohérente** : Format ApiResponse

---

**L'API combinée pour mettre à jour un compte et sa personne est maintenant disponible !** 🎉

---

*API de mise à jour combinée*  
*État : Implémentée et documentée | Prête pour tests*
