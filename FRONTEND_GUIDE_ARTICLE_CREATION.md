# 📚 **GUIDE COMPLET POUR L'AJOUT D'ARTICLES - FRONTEND**

---

## 🎯 **OBJECTIF**

Ce guide montre comment exploiter l'API d'ajout d'articles avec les bons formats de données, notamment pour le champ `attributs` qui suit la logique du `DataInitializer`.

---

## 📋 **PRÉREQUIS**

### **1. Authentification**
```javascript
// Token JWT requis pour toutes les requêtes
const token = localStorage.getItem('jwtToken');
const headers = {
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`
};
```

### **2. Liste des Produits Disponibles**
```javascript
// Récupérer d'abord les produits pour sélectionner
async function getProduits() {
  try {
    const response = await fetch('/api/produits', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    return await response.json();
  } catch (error) {
    console.error('Erreur récupération produits:', error);
  }
}
```

---

## 🏗️ **STRUCTURE DES ATTRIBUTS PAR CATÉGORIE**

### **👕 Vêtements (Chemise, T-shirt, Pantalon, Jean, Robe, Jupe, Veste, Manteau)**
```javascript
const attributsVetements = {
  taille: ["XS", "S", "M", "L", "XL", "XXL", "3XL"][Math.floor(Math.random() * 7)],
  couleur: ["Blanc", "Noir", "Bleu", "Rouge", "Gris", "Vert", "Jaune", "Rose", "Bleu marine", "Beige"][Math.floor(Math.random() * 10)],
  materiau: ["Coton", "Polyester", "Laine", "Lin", "Jean", "Soie", "Synthétique"][Math.floor(Math.random() * 7)],
  marque: ["Nike", "Adidas", "Puma", "Zara", "H&M", "Uniqlo", "Gap"][Math.floor(Math.random() * 7)],
  style: "Made in Senegal"
};
```

### **👟 Chaussures**
```javascript
const attributsChaussures = {
  type: ["Sport/Casual", "Running", "Basket", "Chausson", "Bottine"][Math.floor(Math.random() * 5)],
  couleur: ["Noir", "Blanc", "Bleu", "Rouge", "Gris", "Marron"][Math.floor(Math.random() * 6)],
  matiere: ["Cuir", "Synthétique", "Toile", "Caoutchouc"][Math.floor(Math.random() * 4)],
  pointure: [36, 37, 38, 39, 40, 41, 42, 43, 44, 45][Math.floor(Math.random() * 10)],
  marque: ["Nike", "Adidas", "Puma", "New Balance", "Converse"][Math.floor(Math.random() * 5)]
};
```

### **📱 Électronique (Smartphone, Ordinateur, Tablettes, Accessoires)**
```javascript
const attributsElectronique = {
  couleur: ["Noir", "Blanc", "Argent", "Or", "Bleu", "Rouge"][Math.floor(Math.random() * 6)],
  capacite: ["64GB", "128GB", "256GB", "512GB", "1TB"][Math.floor(Math.random() * 5)],
  marque: ["Samsung", "Apple", "Xiaomi", "Huawei", "Oppo", "Vivo"][Math.floor(Math.random() * 6)],
  garantie: "1 an",
  modele: `Model-${Math.floor(Math.random() * 1000)}`
};
```

### **📚 Livres & Médias (Livres, BD, Magazines)**
```javascript
const attributsLivres = {
  format: ["Poche", "Grand format", "Ebook", "Audio"][Math.floor(Math.random() * 4)],
  langue: ["Français", "Anglais", "Wolof", "Arabe"][Math.floor(Math.random() * 4)],
  auteur: ["Auteur Sénégalais", "Auteur International", "Auteur Africain"][Math.floor(Math.random() * 3)],
  editeur: ["Nouvelles Éditions", "Éditions Sénégal", "Presses Universitaires"][Math.floor(Math.random() * 3)],
  pages: 150 + Math.floor(Math.random() * 500)
};
```

### **🏠 Maison & Jardin (Meuble, Décoration, Jardin)**
```javascript
const attributsMaison = {
  couleur: ["Blanc", "Noir", "Bois naturel", "Gris", "Beige"][Math.floor(Math.random() * 5)],
  materiau: ["Bois", "Métal", "Plastique", "Verre", "Tissu"][Math.floor(Math.random() * 5)],
  taille: ["Petit", "Moyen", "Grand", "Extra"][Math.floor(Math.random() * 4)],
  marque: ["IKEA", "Local Design", "Artisan Sénégalais"][Math.floor(Math.random() * 3)],
  style: "Moderne"
};
```

### **🚗 Automobile & Accessoires**
```javascript
const attributsAuto = {
  couleur: ["Noir", "Blanc", "Gris", "Rouge", "Bleu"][Math.floor(Math.random() * 5)],
  marque: ["Michelin", "Continental", "Goodyear", "Bridgestone"][Math.floor(Math.random() * 4)],
  modele: `Model-${Math.floor(Math.random() * 100)}`,
  type: ["Pneu", "Huile", "Batterie", "Accessoire"][Math.floor(Math.random() * 4)],
  compatibilite: ["Tous véhicules", "Voiture", "Moto", "Camion"][Math.floor(Math.random() * 4)]
};
```

### **🐾 Animaux (Alimentation, Accessoires, Soins)**
```javascript
const attributsAnimaux = {
  type_animal: ["Chien", "Chat", "Oiseau", "Poisson"][Math.floor(Math.random() * 4)],
  taille: ["Petit", "Moyen", "Grand"][Math.floor(Math.random() * 3)],
  marque: ["Royal Canin", "Whiskas", "Local Brand", "Premium"][Math.floor(Math.random() * 4)],
  age: ["Bébé", "Adulte", "Senior"][Math.floor(Math.random() * 3)],
  saveur: ["Poulet", "Bœuf", "Poisson", "Végétal"][Math.floor(Math.random() * 4)]
};
```

### **🏢 Bureau & École**
```javascript
const attributsBureau = {
  couleur: ["Noir", "Bleu", "Rouge", "Vert", "Gris"][Math.floor(Math.random() * 5)],
  materiau: ["Plastique", "Métal", "Bois", "Carton"][Math.floor(Math.random() * 4)],
  marque: ["Bic", "Staedtler", "Local Brand", "Premium"][Math.floor(Math.random() * 4)],
  type: ["Stylo", "Cahier", "Classeur", "Calculatrice"][Math.floor(Math.random() * 4)],
  usage: ["École", "Bureau", "Artistique"][Math.floor(Math.random() * 3)]
};
```

---

## 💻 **IMPLEMENTATION COMPLÈTE FRONTEND**

### **1. Page HTML Complète**
```html
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajout d'Article - Tissenza</title>
    <style>
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input, select, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        .btn { background: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
        .btn:hover { background: #0056b3; }
        .error { color: red; margin-top: 5px; }
        .success { color: green; margin-top: 5px; }
        .attributs-container { border: 1px solid #ddd; padding: 15px; border-radius: 4px; margin-top: 10px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Ajouter un Article</h1>
        <form id="article-form">
            <!-- Champs obligatoires -->
            <div class="form-group">
                <label for="produit">Produit *</label>
                <select id="produit" required>
                    <option value="">Sélectionner un produit...</option>
                </select>
            </div>

            <div class="form-group">
                <label for="prix">Prix (FCFA) *</label>
                <input type="number" id="prix" step="0.01" min="0" required>
            </div>

            <div class="form-group">
                <label for="sku">SKU *</label>
                <input type="text" id="sku" required>
            </div>

            <div class="form-group">
                <label for="stock">Stock Initial *</label>
                <input type="number" id="stock" min="0" required>
            </div>

            <div class="form-group">
                <label for="image">URL Image</label>
                <input type="text" id="image" placeholder="/uploads/produit/default-product.jpg">
            </div>

            <!-- Attributs dynamiques -->
            <div class="attributs-container">
                <h3>Attributs du Produit</h3>
                <div id="attributs-fields">
                    <!-- Les champs d'attributs seront générés dynamiquement -->
                </div>
            </div>

            <button type="submit" class="btn">Créer l'Article</button>
        </form>

        <div id="message"></div>
    </div>

    <script src="article-creation.js"></script>
</body>
</html>
```

### **2. JavaScript Complet**
```javascript
class ArticleCreationManager {
    constructor() {
        this.produits = [];
        this.currentProduit = null;
        this.init();
    }

    async init() {
        await this.loadProduits();
        this.setupEventListeners();
    }

    async loadProduits() {
        try {
            const token = localStorage.getItem('jwtToken');
            const response = await fetch('/api/produits', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            
            if (response.ok) {
                this.produits = await response.json();
                this.populateProduitSelect();
            } else {
                throw new Error('Erreur chargement produits');
            }
        } catch (error) {
            this.showMessage('Erreur: ' + error.message, 'error');
        }
    }

    populateProduitSelect() {
        const select = document.getElementById('produit');
        select.innerHTML = '<option value="">Sélectionner un produit...</option>';
        
        this.produits.forEach(produit => {
            const option = document.createElement('option');
            option.value = produit.id;
            option.textContent = `${produit.nom} - ${produit.categorieNom}`;
            select.appendChild(option);
        });
    }

    setupEventListeners() {
        document.getElementById('produit').addEventListener('change', (e) => {
            this.onProduitChange(e.target.value);
        });

        document.getElementById('article-form').addEventListener('submit', (e) => {
            this.onSubmit(e);
        });
    }

    onProduitChange(produitId) {
        if (!produitId) {
            this.clearAttributsFields();
            return;
        }

        this.currentProduit = this.produits.find(p => p.id == produitId);
        this.generateAttributsFields();
        this.generateSKU();
        this.suggestStock();
        this.suggestPrice();
    }

    generateAttributsFields() {
        const container = document.getElementById('attributs-fields');
        container.innerHTML = '';

        const sousCategorie = this.currentProduit.sousCategorieNom || '';
        const attributsConfig = this.getAttributsConfig(sousCategorie);

        Object.entries(attributsConfig).forEach(([key, config]) => {
            const fieldGroup = document.createElement('div');
            fieldGroup.className = 'form-group';

            const label = document.createElement('label');
            label.textContent = config.label;
            label.setAttribute('for', `attr_${key}`);

            let input;
            if (config.type === 'select') {
                input = document.createElement('select');
                config.options.forEach(option => {
                    const optionElement = document.createElement('option');
                    optionElement.value = option;
                    optionElement.textContent = option;
                    input.appendChild(optionElement);
                });
            } else {
                input = document.createElement('input');
                input.type = config.type || 'text';
                if (config.placeholder) {
                    input.placeholder = config.placeholder;
                }
            }

            input.id = `attr_${key}`;
            input.name = key;
            if (config.required) {
                input.required = true;
            }

            fieldGroup.appendChild(label);
            fieldGroup.appendChild(input);
            container.appendChild(fieldGroup);
        });
    }

    getAttributsConfig(sousCategorie) {
        // Configuration basée sur le DataInitializer
        if (sousCategorie.includes("Chemise") || sousCategorie.includes("T-shirt") || 
            sousCategorie.includes("Pantalon") || sousCategorie.includes("Jean") ||
            sousCategorie.includes("Robe") || sousCategorie.includes("Jupe") ||
            sousCategorie.includes("Veste") || sousCategorie.includes("Manteau")) {
            
            return {
                taille: {
                    label: 'Taille',
                    type: 'select',
                    options: ['XS', 'S', 'M', 'L', 'XL', 'XXL', '3XL'],
                    required: true
                },
                couleur: {
                    label: 'Couleur',
                    type: 'select',
                    options: ['Blanc', 'Noir', 'Bleu', 'Rouge', 'Gris', 'Vert', 'Jaune', 'Rose', 'Bleu marine', 'Beige'],
                    required: true
                },
                materiau: {
                    label: 'Matériau',
                    type: 'select',
                    options: ['Coton', 'Polyester', 'Laine', 'Lin', 'Jean', 'Soie', 'Synthétique'],
                    required: true
                },
                marque: {
                    label: 'Marque',
                    type: 'select',
                    options: ['Nike', 'Adidas', 'Puma', 'Zara', 'H&M', 'Uniqlo', 'Gap'],
                    required: true
                }
            };
        }

        if (sousCategorie.includes("Chaussures")) {
            return {
                type: {
                    label: 'Type',
                    type: 'select',
                    options: ['Sport/Casual', 'Running', 'Basket', 'Chausson', 'Bottine'],
                    required: true
                },
                couleur: {
                    label: 'Couleur',
                    type: 'select',
                    options: ['Noir', 'Blanc', 'Bleu', 'Rouge', 'Gris', 'Marron'],
                    required: true
                },
                matiere: {
                    label: 'Matériau',
                    type: 'select',
                    options: ['Cuir', 'Synthétique', 'Toile', 'Caoutchouc'],
                    required: true
                },
                pointure: {
                    label: 'Pointure',
                    type: 'select',
                    options: [36, 37, 38, 39, 40, 41, 42, 43, 44, 45],
                    required: true
                },
                marque: {
                    label: 'Marque',
                    type: 'select',
                    options: ['Nike', 'Adidas', 'Puma', 'New Balance', 'Converse'],
                    required: false
                }
            };
        }

        if (sousCategorie.includes("Smartphone") || sousCategorie.includes("Ordinateur") || 
            sousCategorie.includes("Tablette")) {
            return {
                couleur: {
                    label: 'Couleur',
                    type: 'select',
                    options: ['Noir', 'Blanc', 'Argent', 'Or', 'Bleu', 'Rouge'],
                    required: true
                },
                capacite: {
                    label: 'Capacité',
                    type: 'select',
                    options: ['64GB', '128GB', '256GB', '512GB', '1TB'],
                    required: true
                },
                marque: {
                    label: 'Marque',
                    type: 'select',
                    options: ['Samsung', 'Apple', 'Xiaomi', 'Huawei', 'Oppo', 'Vivo'],
                    required: true
                },
                modele: {
                    label: 'Modèle',
                    type: 'text',
                    placeholder: 'Ex: Galaxy S23, iPhone 15',
                    required: true
                }
            };
        }

        // Configuration par défaut pour autres catégories
        return {
            couleur: {
                label: 'Couleur',
                type: 'text',
                placeholder: 'Couleur principale',
                required: true
            },
            marque: {
                label: 'Marque',
                type: 'text',
                placeholder: 'Marque du produit',
                required: true
            },
            modele: {
                label: 'Modèle/Référence',
                type: 'text',
                placeholder: 'Modèle ou référence',
                required: false
            }
        };
    }

    generateSKU() {
        const produitId = this.currentProduit.id;
        const random = Math.floor(Math.random() * 10000);
        const sku = `ART-${produitId}-${random}`;
        document.getElementById('sku').value = sku;
    }

    suggestStock() {
        const sousCategorie = this.currentProduit.sousCategorieNom || '';
        let stock;

        if (sousCategorie.includes("Électronique") || sousCategorie.includes("Smartphone")) {
            stock = 5 + Math.floor(Math.random() * 20); // 5-25 unités
        } else if (sousCategorie.includes("Meuble") || sousCategorie.includes("Jardin")) {
            stock = 2 + Math.floor(Math.random() * 13); // 2-15 unités
        } else if (sousCategorie.includes("Livres") || sousCategorie.includes("BD")) {
            stock = 20 + Math.floor(Math.random() * 80); // 20-100 unités
        } else if (sousCategorie.includes("Animaux")) {
            stock = 15 + Math.floor(Math.random() * 85); // 15-100 unités
        } else {
            stock = 8 + Math.floor(Math.random() * 42); // 8-50 unités
        }

        document.getElementById('stock').value = stock;
    }

    suggestPrice() {
        const categorie = this.currentProduit.categorieNom || '';
        let prix;

        if (categorie.includes("Vêtements") || categorie.includes("Chaussures")) {
            prix = 5000 + Math.floor(Math.random() * 70000); // 5k-75k FCFA
        } else if (categorie.includes("Électronique")) {
            prix = 50000 + Math.floor(Math.random() * 450000); // 50k-500k FCFA
        } else if (categorie.includes("Livres")) {
            prix = 3000 + Math.floor(Math.random() * 17000); // 3k-20k FCFA
        } else if (categorie.includes("Animaux")) {
            prix = 2000 + Math.floor(Math.random() * 28000); // 2k-30k FCFA
        } else {
            prix = 5000 + Math.floor(Math.random() * 45000); // 5k-50k FCFA
        }

        document.getElementById('prix').value = prix;
    }

    collectAttributs() {
        const attributs = {};
        const container = document.getElementById('attributs-fields');
        const inputs = container.querySelectorAll('input, select');

        inputs.forEach(input => {
            if (input.value) {
                attributs[input.name] = input.value;
            }
        });

        return attributs;
    }

    async onSubmit(event) {
        event.preventDefault();

        try {
            const articleData = this.buildArticleData();
            await this.createArticle(articleData);
        } catch (error) {
            this.showMessage('Erreur: ' + error.message, 'error');
        }
    }

    buildArticleData() {
        const attributs = this.collectAttributs();

        return {
            produit_id: parseInt(document.getElementById('produit').value),
            prix: parseFloat(document.getElementById('prix').value),
            sku: document.getElementById('sku').value,
            stock_actuel: parseInt(document.getElementById('stock').value),
            image: document.getElementById('image').value || '/uploads/produit/default-product.jpg',
            attributs: attributs
        };
    }

    async createArticle(data) {
        const token = localStorage.getItem('jwtToken');
        
        try {
            const response = await fetch('/api/articles', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText);
            }

            const result = await response.json();
            this.showMessage('Article créé avec succès!', 'success');
            this.resetForm();
            
            console.log('Article créé:', result);

        } catch (error) {
            throw new Error('Échec création: ' + error.message);
        }
    }

    resetForm() {
        document.getElementById('article-form').reset();
        this.clearAttributsFields();
        this.currentProduit = null;
    }

    clearAttributsFields() {
        document.getElementById('attributs-fields').innerHTML = '';
    }

    showMessage(message, type) {
        const messageDiv = document.getElementById('message');
        messageDiv.textContent = message;
        messageDiv.className = type;
        
        setTimeout(() => {
            messageDiv.textContent = '';
            messageDiv.className = '';
        }, 5000);
    }
}

// Initialisation
document.addEventListener('DOMContentLoaded', () => {
    new ArticleCreationManager();
});
```

---

## 🧪 **EXEMPLES D'UTILISATION**

### **1. Création d'un Article de Vêtement**
```javascript
// Exemple de données envoyées
const articleVetement = {
  produit_id: 1,
  prix: 25000,
  sku: "ART-1-1234",
  stock_actuel: 25,
  image: "/uploads/produit/tshirt-bleu.jpg",
  attributs: {
    taille: "M",
    couleur: "Bleu",
    materiau: "Coton",
    marque: "Nike"
  }
};
```

### **2. Création d'un Article Électronique**
```javascript
const articleSmartphone = {
  produit_id: 2,
  prix: 150000,
  sku: "ART-2-5678",
  stock_actuel: 15,
  image: "/uploads/produit/samsung-s23.jpg",
  attributs: {
    couleur: "Noir",
    capacite: "128GB",
    marque: "Samsung",
    modele: "Galaxy S23"
  }
};
```

### **3. Création d'un Article de Chaussures (Format Exact)**
```javascript
const articleChaussures = {
  produit_id: 3,
  prix: 35000,
  sku: "ART-3-9999",
  stock_actuel: 20,
  image: "/uploads/produit/chaussures-gris.jpg",
  attributs: {
    "type": "Sport/Casual",
    "couleur": "Gris", 
    "matiere": "Cuir",
    "pointure": "36"
  }
};
```

### **4. Test API Direct**
```javascript
// Test direct avec fetch
async function testArticleCreation() {
  const testData = {
    produit_id: 1,
    prix: 10000,
    sku: "TEST-001",
    stock_actuel: 10,
    attributs: {
      couleur: "Rouge",
      marque: "Test Brand",
      modele: "Test Model"
    }
  };

  try {
    const response = await fetch('/api/articles', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer VOTRE_TOKEN'
      },
      body: JSON.stringify(testData)
    });

    console.log('Status:', response.status);
    const result = await response.json();
    console.log('Résultat:', result);

  } catch (error) {
    console.error('Erreur:', error);
  }
}
```

---

## ✅ **VALIDATION AUTOMATIQUE**

### **1. Validation Frontend**
```javascript
function validateArticleData(data) {
  // Champs obligatoires
  const required = ['produit_id', 'prix', 'sku', 'stock_actuel'];
  for (const field of required) {
    if (!data[field] || data[field] === null || data[field] === undefined) {
      throw new Error(`Le champ ${field} est obligatoire`);
    }
  }

  // Validation produit_id
  if (typeof data.produit_id !== 'number' || data.produit_id <= 0) {
    throw new Error('produit_id doit être un nombre positif');
  }

  // Validation prix
  if (typeof data.prix !== 'number' || data.prix <= 0) {
    throw new Error('Le prix doit être un nombre positif');
  }

  // Validation SKU
  if (typeof data.sku !== 'string' || data.sku.trim().length === 0) {
    throw new Error('Le SKU ne peut pas être vide');
  }

  // Validation stock
  if (typeof data.stock_actuel !== 'number' || data.stock_actuel < 0) {
    throw new Error('Le stock doit être un nombre positif ou nul');
  }

  // Validation attributs
  if (!data.attributs || typeof data.attributs !== 'object') {
    throw new Error('Les attributs doivent être un objet JSON valide');
  }

  return true;
}
```

---

## 🎯 **RÉSUMÉ D'UTILISATION**

### **1. Étapes**
1. **Sélectionner un produit** → Génère les champs d'attributs
2. **Remplir les informations** → Prix, SKU, stock
3. **Compléter les attributs** → Selon la catégorie du produit
4. **Valider et créer** → Envoi à l'API

### **2. Points Clés**
- **produit_id** : Toujours obligatoire
- **attributs** : Objet JSON selon la catégorie
- **SKU** : Généré automatiquement
- **Prix/Stock** : Suggestions selon la catégorie

### **3. Erreurs Communes à Éviter**
- ❌ `produit_id: null` ou manquant
- ❌ `attributs: "{taille: M}"` (chaîne au lieu d'objet)
- ❌ `attributs: {"value": "{...}"}` (double encapsulation)
- ✅ `attributs: {taille: "M", couleur: "Bleu"}`

**Ce guide complet permet d'exploiter pleinement l'API d'ajout d'articles avec une gestion intelligente des attributs par catégorie !** 🚀
