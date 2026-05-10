# 🔓 **GUIDE D'ACCÈS AUX APIS - SÉCURITÉ CONFIGURÉE**

---

## ✅ **Configuration de Sécurité Modifiée**

### **🔧 Changements Appliqués**
La configuration Spring Security a été modifiée pour permettre l'accès à toutes les APIs de développement sans authentification :

```java
// APIs de développement - accès sans authentification
.requestMatchers("/api/personnes/**").permitAll()
.requestMatchers("/api/categories/**").permitAll()
.requestMatchers("/api/boutiques/**").permitAll()
.requestMatchers("/api/produits/**").permitAll()
.requestMatchers("/api/articles/**").permitAll()
.requestMatchers("/api/paniers/**").permitAll()
.requestMatchers("/api/commandes/**").permitAll()
```

---

## 🚀 **Tests Immédiatement Possibles**

### **🛒 Créer un Panier**
```bash
# Pour Mariam Kane (personne ID 10)
curl -X GET "http://localhost:8080/api/paniers/client/10/actif"

# Pour Abdou Sow (personne ID 9)
curl -X GET "http://localhost:8080/api/paniers/client/9/actif"

# Pour tous les clients (IDs 9-16)
curl -X GET "http://localhost:8080/api/paniers/client/11/actif"  # Baba Cisse
curl -X GET "http://localhost:8080/api/paniers/client/12/actif"  # Aicha Diallo
curl -X GET "http://localhost:8080/api/paniers/client/13/actif"  # Mamadou Ly
curl -X GET "http://localhost:8080/api/paniers/client/14/actif"  # Khadija Gueye
curl -X GET "http://localhost:8080/api/paniers/client/15/actif"  # Cheikh Seck
curl -X GET "http://localhost:8080/api/paniers/client/16/actif"  # Rokhaya Lo
```

### **📦 Lister les Articles Disponibles**
```bash
# Voir tous les articles disponibles
curl -X GET "http://localhost:8080/api/articles"

# Voir les articles d'une boutique spécifique
curl -X GET "http://localhost:8080/api/articles/boutique/1"
```

### **🛍️ Ajouter des Articles au Panier**
```bash
# Ajouter l'article ID 1 au panier du client 10
curl -X POST "http://localhost:8080/api/paniers/client/10/ajouter" \
  -H "Content-Type: application/json" \
  -d '{"articleId": 1, "quantite": 2}'

# Ajouter l'article ID 2 au panier du client 10
curl -X POST "http://localhost:8080/api/paniers/client/10/ajouter" \
  -H "Content-Type: application/json" \
  -d '{"articleId": 2, "quantite": 1}'
```

### **📋 Voir le Panier**
```bash
# Voir le panier du client 10
curl -X GET "http://localhost:8080/api/paniers/client/10/actif"
```

### **✅ Valider le Panier**
```bash
# Valider le panier (remplacer {panierId} par l'ID du panier)
curl -X POST "http://localhost:8080/api/paniers/{panierId}/valider"
```

### **📦 Créer une Commande**
```bash
# Créer une commande depuis le panier validé
curl -X POST "http://localhost:8080/api/commandes/creer/{panierId}"
```

### **📊 Lister les Commandes**
```bash
# Voir toutes les commandes
curl -X GET "http://localhost:8080/api/commandes"

# Voir les commandes par boutique
curl -X GET "http://localhost:8080/api/commandes/boutique/1"

# Voir les commandes par client
curl -X GET "http://localhost:8080/api/commandes/client/10"
```

---

## 📋 **Clients Disponibles (Personne IDs)**

| ID | Nom | Prénom | Email | Rôle |
|---|---|---|---|---|
| **9** | Sow | Abdou | client1@tissenza.com | CLIENT |
| **10** | Kane | Mariam | client2@tissenza.com | CLIENT |
| **11** | Cisse | Baba | client3@tissenza.com | CLIENT |
| **12** | Diallo | Aicha | client4@tissenza.com | CLIENT |
| **13** | Ly | Mamadou | client5@tissenza.com | CLIENT |
| **14** | Gueye | Khadija | client6@tissenza.com | CLIENT |
| **15** | Seck | Cheikh | client7@tissenza.com | CLIENT |
| **16** | Lo | Rokhaya | client8@tissenza.com | CLIENT |

---

## 🎯 **Workflow Complet de Test**

### **Étape 1: Créer un panier**
```bash
curl -X GET "http://localhost:8080/api/paniers/client/10/actif"
```

### **Étape 2: Ajouter des articles**
```bash
# D'abord vérifier les articles disponibles
curl -X GET "http://localhost:8080/api/articles"

# Ajouter des articles au panier
curl -X POST "http://localhost:8080/api/paniers/client/10/ajouter" \
  -H "Content-Type: application/json" \
  -d '{"articleId": 1, "quantite": 2}'
```

### **Étape 3: Valider le panier**
```bash
curl -X POST "http://localhost:8080/api/paniers/{panierId}/valider"
```

### **Étape 4: Créer une commande**
```bash
curl -X POST "http://localhost:8080/api/commandes/creer/{panierId}"
```

### **Étape 5: Voir les commandes**
```bash
curl -X GET "http://localhost:8080/api/commandes"
```

---

## 🔍 **Débogage**

### **Si vous rencontrez des erreurs :**

1. **Vérifiez que l'application est démarrée**
   ```bash
   # L'application doit afficher "Started TissenzaBackendApplication"
   ```

2. **Vérifiez les IDs disponibles**
   ```bash
   # Voir toutes les personnes
   curl -X GET "http://localhost:8080/api/personnes"
   ```

3. **Vérifiez les articles disponibles**
   ```bash
   # Voir tous les articles
   curl -X GET "http://localhost:8080/api/articles"
   ```

4. **Vérifiez les logs de l'application**
   ```bash
   # Les erreurs s'affichent dans la console de l'application
   ```

---

## 🏆 **État Actuel**

### **✅ Configuration Terminée**
- **Sécurité** : APIs accessibles sans authentification
- **Application** : En cours d'exécution
- **Données** : 16 personnes + articles disponibles
- **Tests** : Prêts à être exécutés

### **🚀 Prochaine Étape**
Essayez maintenant la commande :
```bash
curl -X GET "http://localhost:8080/api/paniers/client/10/actif"
```

**L'erreur 403 Forbidden ne devrait plus apparaître !** 🎉

---

*Guide d'accès aux APIs avec sécurité configurée*  
*État : Configuration terminée | Tests prêts*
