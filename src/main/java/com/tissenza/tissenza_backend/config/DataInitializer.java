package com.tissenza.tissenza_backend.config;

import com.tissenza.tissenza_backend.modules.produit.entity.Categorie;
import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.repository.CategorieRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.SousCategorieRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.ProduitRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.ArticleRepository;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.repository.CompteRepository;
import com.tissenza.tissenza_backend.modules.user.repository.PersonneRepository;
import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.modules.boutique.repository.BoutiqueRepository;
import com.tissenza.tissenza_backend.modules.panier.entity.Panier;
import com.tissenza.tissenza_backend.modules.panier.entity.PanierItem;
import com.tissenza.tissenza_backend.modules.panier.repository.PanierRepository;
import com.tissenza.tissenza_backend.modules.panier.repository.PanierItemRepository;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.entity.DetailCommande;
import com.tissenza.tissenza_backend.modules.commande.repository.CommandeRepository;
import com.tissenza.tissenza_backend.modules.commande.repository.DetailCommandeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategorieRepository categorieRepository;
    private final SousCategorieRepository sousCategorieRepository;
    private final ProduitRepository produitRepository;
    private final ArticleRepository articleRepository;
    private final BoutiqueRepository boutiqueRepository;
    private final PersonneRepository personneRepository;
    private final CompteRepository compteRepository;
    private final PanierRepository panierRepository;
    private final PanierItemRepository panierItemRepository;
    private final CommandeRepository commandeRepository;
    private final DetailCommandeRepository detailCommandeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeCategoriesAndSubCategories();
        initializeBoutiquesForVendors();
        initializeProductsForBoutiques();
        initializeArticlesForProducts();
        initializePaniersForClients();
        initializeCommandesFromPaniers();
    }

    private void initializeUsers() {
        log.info("Initialisation des utilisateurs par défaut...");

        // Vérifier si les utilisateurs existent déjà
        if (compteRepository.count() > 0) {
            log.info("Les utilisateurs existent déjà, initialisation ignorée.");
            return;
        }

        // Créer les utilisateurs par défaut
        List<Compte> defaultUsers = createDefaultUsers();
        List<Compte> savedUsers = compteRepository.saveAll(defaultUsers);
        log.info("{} utilisateurs par défaut créés", savedUsers.size());

        log.info("Initialisation des utilisateurs terminée !");
    }

    private List<Compte> createDefaultUsers() {
        return Arrays.asList(
            // Administrateurs
            createUser("admin", "admin123", "admin@tissenza.com", "Admin", "Principal", "ADMIN", "22112345678"),
            createUser("admin2", "admin123", "admin2@tissenza.com", "Admin", "Secondaire", "ADMIN", "22112345679"),
            createUser("superadmin", "admin123", "superadmin@tissenza.com", "Super", "Admin", "ADMIN", "22112345680"),
            
            // Vendeurs
            createUser("vendeur1", "vendeur123", "vendeur1@tissenza.com", "Mohamed", "Sall", "VENDEUR", "22112345681"),
            createUser("vendeur2", "vendeur123", "vendeur2@tissenza.com", "Fatou", "Diop", "VENDEUR", "22112345682"),
            createUser("vendeur3", "vendeur123", "vendeur3@tissenza.com", "Ibrahim", "Ba", "VENDEUR", "22112345683"),
            createUser("vendeur4", "vendeur123", "vendeur4@tissenza.com", "Aminata", "Fall", "VENDEUR", "22112345684"),
            createUser("vendeur5", "vendeur123", "vendeur5@tissenza.com", "Omar", "Ndiaye", "VENDEUR", "22112345685"),
            
            // Clients
            createUser("client1", "client123", "client1@tissenza.com", "Abdou", "Sow", "CLIENT", "22112345686"),
            createUser("client2", "client123", "client2@tissenza.com", "Mariam", "Kane", "CLIENT", "22112345687"),
            createUser("client3", "client123", "client3@tissenza.com", "Baba", "Cisse", "CLIENT", "22112345688"),
            createUser("client4", "client123", "client4@tissenza.com", "Aicha", "Diallo", "CLIENT", "22112345689"),
            createUser("client5", "client123", "client5@tissenza.com", "Mamadou", "Ly", "CLIENT", "22112345690"),
            createUser("client6", "client123", "client6@tissenza.com", "Khadija", "Gueye", "CLIENT", "22112345691"),
            createUser("client7", "client123", "client7@tissenza.com", "Cheikh", "Seck", "CLIENT", "22112345692"),
            createUser("client8", "client123", "client8@tissenza.com", "Rokhaya", "Lo", "CLIENT", "22112345693")
        );
    }

    private Compte createUser(String username, String password, String email, String prenom, String nom, String role, String telephone) {
        // Créer la personne
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setAdresse("Adresse par défaut");
        personne.setVille("Dakar");
        personne.setCreatedAt(LocalDateTime.now());

        // Sauvegarder la personne d'abord
        Personne savedPersonne = personneRepository.save(personne);

        // Créer le compte
        Compte compte = new Compte();
        compte.setEmail(email);
        compte.setMotDePasse(passwordEncoder.encode(password));
        compte.setRole(Compte.Role.valueOf(role));
        compte.setStatut(Compte.Statut.ACTIF);
        setIsVerified(compte, true);
        compte.setTelephone(telephone);
        compte.setPersonne(savedPersonne);
        compte.setCreatedAt(LocalDateTime.now());
        compte.setLastLogin(LocalDateTime.now());

        return compte;
    }

    private void setIsVerified(Compte compte, boolean verified) {
        // Utiliser réflexion pour accéder au champ isVerified s'il existe
        try {
            compte.getClass().getMethod("setIsVerified", boolean.class).invoke(compte, verified);
        } catch (Exception e) {
            // Ignorer si le champ n'existe pas
            log.debug("Champ isVerified non trouvé, utilisation de la valeur par défaut");
        }
    }

    private void initializeCategoriesAndSubCategories() {
        log.info("Initialisation des catégories et sous-catégories...");

        // Vérifier si les données existent déjà
        if (categorieRepository.count() > 0) {
            log.info("Les catégories existent déjà, initialisation ignorée.");
            return;
        }

        // Créer les catégories principales
        List<Categorie> categories = createCategories();
        List<Categorie> savedCategories = categorieRepository.saveAll(categories);
        log.info("{} catégories créées", savedCategories.size());

        // Créer les sous-catégories
        List<SousCategorie> subCategories = createSubCategories(savedCategories);
        List<SousCategorie> savedSubCategories = sousCategorieRepository.saveAll(subCategories);
        log.info("{} sous-catégories créées", savedSubCategories.size());

        log.info("Initialisation des données terminée avec succès !");
    }

    private List<Categorie> createCategories() {
        return Arrays.asList(
            createCategorie("Vêtements Hommes", "Collection complète de vêtements pour hommes - du casual au formel, en passant par le sport"),
            createCategorie("Vêtements Femmes", "Mode féminine tendance et élégante - robes, jupes, tops et ensembles pour toutes occasions"),
            createCategorie("Vêtements Enfants", "Habillages confortables pour enfants - bébés, enfants et ados, pratiques et stylés"),
            createCategorie("Chaussures", "Toutes sortes de chaussures pour toute la famille - sport, ville, soirée et détente"),
            createCategorie("Accessoires", "Accessoires de mode et utilitaires - montres, bijoux, ceintures et compléments de style"),
            createCategorie("Électronique", "Appareils électroniques et gadgets - smartphones, ordinateurs, audio et photo"),
            createCategorie("Maison & Décoration", "Articles pour la maison et décoration - meubles, textiles et objets déco"),
            createCategorie("Sports & Loisirs", "Équipements sportifs et articles de loisirs - fitness, extérieur et jeux"),
            createCategorie("Beauté & Santé", "Produits de beauté et bien-être - soins visage, corps, maquillage et parfums"),
            createCategorie("Bagagerie", "Sacs, valises et accessoires de voyage - bagages, sacs à dos et organisation"),
            createCategorie("Livres & Médias", "Livres, musique et films - papier, numérique et streaming"),
            createCategorie("Jardin & Extérieur", "Jardinage et aménagement extérieur - outils, plantes et mobilier"),
            createCategorie("Automobile", "Accessoires auto et entretien véhicule - intérieur, extérieur et sécurité"),
            createCategorie("Animaux", "Produits pour animaux - alimentation, accessoires et soin"),
            createCategorie("Bureau & École", "Fournitures de bureau et matériel scolaire - papeterie et organisation")
        );
    }

    private List<SousCategorie> createSubCategories(List<Categorie> categories) {
        return Arrays.asList(
            // Vêtements Hommes (ID: 1)
            createSubCategorie("Chemises", "Chemises formelles et décontractées pour hommes", categories.get(0)),
            createSubCategorie("T-shirts", "T-shirts et débardeurs pour hommes", categories.get(0)),
            createSubCategorie("Pantalons", "Pantalons, jeans et pantalons chinos", categories.get(0)),
            createSubCategorie("Vestes", "Vestes et manteaux pour hommes", categories.get(0)),
            createSubCategorie("Costumes", "Costumes complets pour occasions spéciales", categories.get(0)),

            // Vêtements Femmes (ID: 2)
            createSubCategorie("Robes", "Robes pour toutes occasions", categories.get(1)),
            createSubCategorie("Jupes", "Jupes de différentes longueurs et styles", categories.get(1)),
            createSubCategorie("Tops", "Tops, décolletés et chemisiers féminins", categories.get(1)),
            createSubCategorie("Pantalons Femmes", "Pantalons et leggings pour femmes", categories.get(1)),
            createSubCategorie("Lingerie", "Sous-vêtements et lingerie féminine", categories.get(1)),

            // Vêtements Enfants (ID: 3)
            createSubCategorie("Vêtements Bébés", "Habillages pour bébés (0-2 ans)", categories.get(2)),
            createSubCategorie("Vêtements Filles", "Vêtements pour filles (3-12 ans)", categories.get(2)),
            createSubCategorie("Vêtements Garçons", "Vêtements pour garçons (3-12 ans)", categories.get(2)),
            createSubCategorie("Ados", "Mode pour adolescents (13-18 ans)", categories.get(2)),
            createSubCategorie("Pyjamas", "Pyjamas et vêtements de nuit pour enfants", categories.get(2)),

            // Chaussures (ID: 4)
            createSubCategorie("Chaussures Hommes", "Chaussures formelles et sportives pour hommes", categories.get(3)),
            createSubCategorie("Chaussures Femmes", "Escarpins, baskets et bottes pour femmes", categories.get(3)),
            createSubCategorie("Chaussures Enfants", "Chaussures confortables pour enfants", categories.get(3)),
            createSubCategorie("Chaussures Sport", "Chaussures de sport et running", categories.get(3)),
            createSubCategorie("Sandales & Tongs", "Sandales et tongs pour toute la famille", categories.get(3)),

            // Accessoires (ID: 5)
            createSubCategorie("Montres", "Montres pour hommes et femmes", categories.get(4)),
            createSubCategorie("Ceintures", "Ceintures en cuir et synthétiques", categories.get(4)),
            createSubCategorie("Lunettes", "Lunettes de soleil et vue", categories.get(4)),
            createSubCategorie("Bijoux", "Bijoux fantaisie et précieux", categories.get(4)),
            createSubCategorie("Sacs & Portefeuilles", "Sacs à main et portefeuilles", categories.get(4)),

            // Électronique (ID: 6)
            createSubCategorie("Téléphones", "Smartphones et accessoires téléphoniques", categories.get(5)),
            createSubCategorie("Ordinateurs", "Ordinateurs portables et de bureau", categories.get(5)),
            createSubCategorie("Télévisions", "TV et équipements home cinéma", categories.get(5)),
            createSubCategorie("Audio", "Casques, enceintes et équipements audio", categories.get(5)),
            createSubCategorie("Gadgets", "Gadgets électroniques innovants", categories.get(5)),

            // Maison & Décoration (ID: 7)
            createSubCategorie("Meubles", "Meubles pour salon, chambre et cuisine", categories.get(6)),
            createSubCategorie("Décoration", "Articles décoratifs pour intérieur", categories.get(6)),
            createSubCategorie("Cuisine", "Ustensiles et électroménager", categories.get(6)),
            createSubCategorie("Literie", "Draps, couettes et oreillers", categories.get(6)),
            createSubCategorie("Jardin", "Mobilier et articles de jardin", categories.get(6)),

            // Sports & Loisirs (ID: 8)
            createSubCategorie("Fitness", "Équipements de fitness et musculation", categories.get(7)),
            createSubCategorie("Sports d'équipe", "Équipements pour football, basketball, etc.", categories.get(7)),
            createSubCategorie("Sports extrêmes", "Matériel pour sports extrêmes", categories.get(7)),
            createSubCategorie("Jeux & Jouets", "Jeux de société et jouets", categories.get(7)),
            createSubCategorie("Musique", "Instruments de musique et accessoires", categories.get(7)),

            // Beauté & Santé (ID: 9)
            createSubCategorie("Soins du visage", "Crèmes, lotions et soins visage", categories.get(8)),
            createSubCategorie("Maquillage", "Produits de maquillage professionnels", categories.get(8)),
            createSubCategorie("Parfums", "Parfums pour hommes et femmes", categories.get(8)),
            createSubCategorie("Soins corporels", "Gels douche, huiles et soins corps", categories.get(8)),
            createSubCategorie("Compléments alimentaires", "Vitamines et compléments santé", categories.get(8)),

            // Bagagerie (ID: 10)
            createSubCategorie("Valises", "Valises rigides et souples", categories.get(9)),
            createSubCategorie("Sacs à dos", "Sacs à dos pour école et voyage", categories.get(9)),
            createSubCategorie("Sacs de voyage", "Sacs de voyage et cabines", categories.get(9)),
            createSubCategorie("Accessoires voyage", "Pochettes, étuis et organisateurs", categories.get(9)),
            createSubCategorie("Maroquinerie", "Portefeuilles et petits articles en cuir", categories.get(9)),

            // Livres & Médias (ID: 11)
            createSubCategorie("Romans", "Romans français et étrangers", categories.get(10)),
            createSubCategorie("BD & Manga", "Bandes dessinées et mangas", categories.get(10)),
            createSubCategorie("Musique", "CDs, vinyles et streaming", categories.get(10)),
            createSubCategorie("Films & Séries", "DVDs, Blu-rays et VOD", categories.get(10)),
            createSubCategorie("Livres pratiques", "Guides et livres de développement personnel", categories.get(10)),

            // Jardin & Extérieur (ID: 12)
            createSubCategorie("Outils de jardin", "Bêches, râteaux et arrosoirs", categories.get(11)),
            createSubCategorie("Plantes & Graines", "Plantes vivaces, annuelles et graines", categories.get(11)),
            createSubCategorie("Mobilier jardin", "Tables, chaises et transats", categories.get(11)),
            createSubCategorie("Barbecue", "Grills et accessoires BBQ", categories.get(11)),
            createSubCategorie("Déco extérieure", "Luminaires et objets déco jardin", categories.get(11)),

            // Automobile (ID: 13)
            createSubCategorie("Entretien voiture", "Huiles, liquides et produits nettoyants", categories.get(12)),
            createSubCategorie("Accessoires intérieur", "Tapis de sol, housses et organisateurs", categories.get(12)),
            createSubCategorie("Accessoires extérieur", "Porte-vélos, coffres de toit", categories.get(12)),
            createSubCategorie("Sécurité", "Alarmes, caméras et GPS", categories.get(12)),
            createSubCategorie("Électronique auto", "Autoradios, GPS et chargeurs", categories.get(12)),

            // Animaux (ID: 14)
            createSubCategorie("Alimentation chiens", "Croquettes et nourriture pour chiens", categories.get(13)),
            createSubCategorie("Alimentation chats", "Croquettes et nourriture pour chats", categories.get(13)),
            createSubCategorie("Accessoires chiens", "Laisses, colliers et jouets pour chiens", categories.get(13)),
            createSubCategorie("Accessoires chats", "Litières, griffoirs et jouets pour chats", categories.get(13)),
            createSubCategorie("Soin animaux", "Shampoings, brosses et produits de toilette", categories.get(13)),

            // Bureau & École (ID: 15)
            createSubCategorie("Papeterie", "Stylos, cahiers et classeurs", categories.get(14)),
            createSubCategorie("Fournitures bureau", "Trombones, agrafeuses et calculatrices", categories.get(14)),
            createSubCategorie("Scolaire", "Cartables, trousses et matériel scolaire", categories.get(14)),
            createSubCategorie("Organisation", "Classeurs, dossiers et boîtes de rangement", categories.get(14)),
            createSubCategorie("Arts & Loisirs", "Feuilles à dessiner, peinture et matériel créatif", categories.get(14))
        );
    }

    private Categorie createCategorie(String nom, String description) {
        Categorie categorie = new Categorie();
        categorie.setNom(nom);
        categorie.setDescription(description);
        categorie.setCreatedAt(LocalDateTime.now());
        return categorie;
    }

    private SousCategorie createSubCategorie(String nom, String description, Categorie categorie) {
        SousCategorie sousCategorie = new SousCategorie();
        sousCategorie.setNom(nom);
        sousCategorie.setDescription(description);
        sousCategorie.setCategorie(categorie);
        sousCategorie.setCreatedAt(LocalDateTime.now());
        return sousCategorie;
    }

    private void initializeBoutiquesForVendors() {
        log.info("Initialisation des boutiques pour chaque vendeur...");

        // Vérifier si les boutiques existent déjà
        if (boutiqueRepository.count() > 0) {
            log.info("Les boutiques existent déjà, initialisation ignorée.");
            return;
        }

        // Récupérer tous les vendeurs
        List<Compte> vendeurs = compteRepository.findByRole(Compte.Role.VENDEUR);
        
        for (Compte vendeur : vendeurs) {
            Boutique boutique = createBoutiqueForVendor(vendeur);
            boutiqueRepository.save(boutique);
            log.info("Boutique créée pour le vendeur: {}", vendeur.getEmail());
        }
    }

    private Boutique createBoutiqueForVendor(Compte vendeur) {
        Boutique boutique = new Boutique();
        boutique.setVendeur(vendeur);
        
        // Noms de boutiques plus variés selon le vendeur
        String[] nomsBoutiques = {
            "Fashion Style", "Mode Élégante", "Vêtements Chic", "Style Dakar", 
            "Boutique Moderne", "Tendance Mode", "Fashion House", "Style Urbain"
        };
        String[] descriptions = {
            "Spécialisée dans la mode masculine et féminine",
            "Vêtements tendance pour toute la famille",
            "Mode africaine contemporaine",
            "Collections exclusives et pièces uniques",
            "Style urbain et streetwear",
            "Élégance et raffinement pour toutes occasions"
        };
        String[] adresses = {
            "Plateau, Dakar", "Almadies, Dakar", "Mermoz, Dakar", 
            "Sacré-Cœur, Dakar", "Ouakam, Dakar", "Yoff, Dakar"
        };
        
        int index = vendeur.getPersonne().getNom().hashCode() % nomsBoutiques.length;
        if (index < 0) index = -index;
        
        boutique.setNom(nomsBoutiques[index] + " - " + vendeur.getPersonne().getNom());
        boutique.setDescription(descriptions[index]);
        boutique.setAddresse(adresses[index]);
        boutique.setStatut(Boutique.Statut.VALIDE);
        boutique.setNote((float) (4.0 + (Math.random() * 1.0))); // Note entre 4.0 et 5.0
        boutique.setCreatedAt(LocalDateTime.now());
        return boutique;
    }

    private void initializeProductsForBoutiques() {
        log.info("Initialisation des produits pour chaque boutique...");

        // Vérifier si les produits existent déjà
        if (produitRepository.count() > 0) {
            log.info("Les produits existent déjà, initialisation ignorée.");
            return;
        }

        // Récupérer toutes les boutiques et sous-catégories
        List<Boutique> boutiques = boutiqueRepository.findAll();
        List<SousCategorie> sousCategories = sousCategorieRepository.findAll();

        for (Boutique boutique : boutiques) {
            // Créer 3-5 produits par boutique
            int productsCount = 3 + (int)(Math.random() * 3); // 3 à 5 produits
            
            for (int i = 0; i < productsCount; i++) {
                SousCategorie randomSousCategorie = sousCategories.get((int)(Math.random() * sousCategories.size()));
                Produit produit = createProductForBoutique(boutique, randomSousCategorie, i);
                produitRepository.save(produit);
            }
            
            log.info("Produits créés pour la boutique: {}", boutique.getNom());
        }
    }

    private Produit createProductForBoutique(Boutique boutique, SousCategorie sousCategorie, int index) {
        Produit produit = new Produit();
        produit.setBoutique(boutique);
        produit.setSousCategorie(sousCategorie);
        
        // Noms de produits plus réalistes selon la sous-catégorie
        String nomProduit = generateProductName(sousCategorie.getNom(), index);
        String description = generateProductDescription(sousCategorie.getNom(), nomProduit);
        
        produit.setNom(nomProduit);
        produit.setDescription(description);
        produit.setImage("/uploads/produit/default-product.jpg"); // Image locale par défaut
        produit.setStatut(Produit.Statut.ACTIF);
        produit.setCreatedAt(LocalDateTime.now());
        return produit;
    }
    
    private String generateProductName(String sousCategorie, int index) {
        String[][] productNames = {
            {"Chemise", "T-shirt", "Polo", "Chemisier", "Débardeur"},
            {"Pantalon", "Jean", "Short", "Jupe", "Legging"},
            {"Robe", "Tenue", "Combinaison", "Ensemble", "Tailleur"},
            {"Veste", "Manteau", "Blouson", "Gilet", "Cardigan"},
            {"Basket", "Chaussure", "Bottine", "Sandale", "Espadrille"},
            {"Montre", "Bracelet", "Collier", "Bague", "Boucle d'oreille"},
            {"Sac", "Portefeuille", "Ceinture", "Lunettes", "Écharpe"},
            {"Téléphone", "Ordinateur", "Tablette", "Écouteurs", "Chargeur"},
            {"Meuble", "Décoration", "Lampe", "Coussin", "Tapis"},
            {"Ballon", "Raquette", "Vélo", "Tapis de yoga", "Haltères"}
        };
        
        String categoryKey = getCategoryKey(sousCategorie);
        String[] names = getProductNamesByCategory(categoryKey, productNames);
        
        if (names != null && names.length > 0) {
            String baseName = names[index % names.length];
            String[] adjectives = {"Élégant", "Moderne", "Classique", "Tendance", "Sportif", "Chic", "Urbain", "Décontracté"};
            String adjective = adjectives[(index + baseName.hashCode()) % adjectives.length];
            return adjective + " " + baseName + " " + (index + 1);
        }
        
        return "Produit " + (index + 1) + " - " + sousCategorie;
    }
    
    private String getCategoryKey(String sousCategorie) {
        if (sousCategorie.contains("Chemise") || sousCategorie.contains("T-shirt")) return "tops";
        if (sousCategorie.contains("Pantalon") || sousCategorie.contains("Jean")) return "bottoms";
        if (sousCategorie.contains("Robe") || sousCategorie.contains("Tenue")) return "dresses";
        if (sousCategorie.contains("Veste") || sousCategorie.contains("Manteau")) return "outerwear";
        if (sousCategorie.contains("Chaussure") || sousCategorie.contains("Basket")) return "shoes";
        if (sousCategorie.contains("Montre") || sousCategorie.contains("Bijoux")) return "accessories";
        if (sousCategorie.contains("Sac") || sousCategorie.contains("Portefeuille")) return "bags";
        if (sousCategorie.contains("Téléphone") || sousCategorie.contains("Ordinateur")) return "electronics";
        if (sousCategorie.contains("Meuble") || sousCategorie.contains("Décoration")) return "home";
        if (sousCategorie.contains("Ballon") || sousCategorie.contains("Sport")) return "sports";
        return "default";
    }
    
    private String[] getProductNamesByCategory(String categoryKey, String[][] productNames) {
        switch (categoryKey) {
            case "tops": return productNames[0];
            case "bottoms": return productNames[1];
            case "dresses": return productNames[2];
            case "outerwear": return productNames[3];
            case "shoes": return productNames[4];
            case "accessories": return productNames[5];
            case "bags": return productNames[6];
            case "electronics": return productNames[7];
            case "home": return productNames[8];
            case "sports": return productNames[9];
            default: return null;
        }
    }
    
    private String generateProductDescription(String sousCategorie, String nomProduit) {
        String[] descriptions = {
            "Produit de haute qualité fabriqué avec des matériaux premium",
            "Design moderne et élégant parfait pour toutes occasions",
            "Confortable et durable, idéal pour un usage quotidien",
            "Tendance et stylé, un must-have pour votre garde-robe",
            "Fabriqué avec soin pour garantir satisfaction et longévité"
        };
        
        int index = (nomProduit.hashCode() + sousCategorie.hashCode()) % descriptions.length;
        if (index < 0) index = -index;
        
        return descriptions[index] + ". Parfait pour la catégorie " + sousCategorie + ".";
    }

    private void initializeArticlesForProducts() {
        log.info("Initialisation des articles pour chaque produit...");

        // Vérifier si les articles existent déjà
        if (articleRepository.count() > 0) {
            log.info("Les articles existent déjà, initialisation ignorée.");
            return;
        }

        // Récupérer tous les produits
        List<Produit> produits = produitRepository.findAll();

        for (Produit produit : produits) {
            // Créer 2-4 articles par produit (différentes tailles/couleurs)
            int articlesCount = 2 + (int)(Math.random() * 3); // 2 à 4 articles
            
            for (int i = 0; i < articlesCount; i++) {
                Article article = createArticleForProduct(produit, i);
                articleRepository.save(article);
            }
            
            log.info("Articles créés pour le produit: {}", produit.getNom());
        }
    }

    private Article createArticleForProduct(Produit produit, int index) {
        Article article = new Article();
        article.setProduit(produit);
        article.setSku("SKU-" + produit.getId() + "-" + (index + 1));
        
        // Prix plus réalistes selon la catégorie de produit
        double prix = generateRealisticPrice(produit.getSousCategorie().getNom());
        article.setPrix(java.math.BigDecimal.valueOf(prix));
        
        // Stock variable selon le type de produit
        article.setStockActuel(generateStockByCategory(produit.getSousCategorie().getNom()));
        
        // Attributs plus variés
        String attributsJson = generateAttributesByCategory(produit.getSousCategorie().getNom(), index);
        article.setAttributs(attributsJson);
        
        article.setImage("/uploads/produit/article-" + produit.getId() + "-" + (index + 1) + ".jpg");
        return article;
    }
    
    private double generateRealisticPrice(String sousCategorie) {
        // Prix selon la catégorie en FCFA
        if (sousCategorie.contains("Chemise") || sousCategorie.contains("T-shirt")) {
            return 5000 + (Math.random() * 25000); // 5000-30000 FCFA
        } else if (sousCategorie.contains("Pantalon") || sousCategorie.contains("Jean")) {
            return 8000 + (Math.random() * 32000); // 8000-40000 FCFA
        } else if (sousCategorie.contains("Robe") || sousCategorie.contains("Jupe")) {
            return 10000 + (Math.random() * 40000); // 10000-50000 FCFA
        } else if (sousCategorie.contains("Veste") || sousCategorie.contains("Manteau")) {
            return 15000 + (Math.random() * 60000); // 15000-75000 FCFA
        } else if (sousCategorie.contains("Chaussure")) {
            return 12000 + (Math.random() * 48000); // 12000-60000 FCFA
        } else if (sousCategorie.contains("Montre") || sousCategorie.contains("Bijoux")) {
            return 8000 + (Math.random() * 72000); // 8000-80000 FCFA
        } else if (sousCategorie.contains("Sac") || sousCategorie.contains("Portefeuille")) {
            return 6000 + (Math.random() * 44000); // 6000-50000 FCFA
        } else if (sousCategorie.contains("Téléphone") || sousCategorie.contains("Ordinateur")) {
            return 50000 + (Math.random() * 450000); // 50000-500000 FCFA
        } else if (sousCategorie.contains("Meuble") || sousCategorie.contains("Décoration")) {
            return 15000 + (Math.random() * 185000); // 15000-200000 FCFA
        } else if (sousCategorie.contains("Ballon") || sousCategorie.contains("Sport")) {
            return 5000 + (Math.random() * 45000); // 5000-50000 FCFA
        } else if (sousCategorie.contains("Livres") || sousCategorie.contains("BD")) {
            return 3000 + (Math.random() * 17000); // 3000-20000 FCFA
        } else if (sousCategorie.contains("Jardin") || sousCategorie.contains("Outils")) {
            return 4000 + (Math.random() * 36000); // 4000-40000 FCFA
        } else if (sousCategorie.contains("Auto") || sousCategorie.contains("Voiture")) {
            return 8000 + (Math.random() * 72000); // 8000-80000 FCFA
        } else if (sousCategorie.contains("Animaux") || sousCategorie.contains("Chiens") || sousCategorie.contains("Chats")) {
            return 2000 + (Math.random() * 28000); // 2000-30000 FCFA
        } else if (sousCategorie.contains("Bureau") || sousCategorie.contains("École")) {
            return 1000 + (Math.random() * 19000); // 1000-20000 FCFA
        } else {
            return 5000 + (Math.random() * 45000); // Par défaut: 5000-50000 FCFA
        }
    }
    
    private int generateStockByCategory(String sousCategorie) {
        // Stock selon la catégorie
        if (sousCategorie.contains("Téléphone") || sousCategorie.contains("Ordinateur")) {
            return 5 + (int)(Math.random() * 20); // 5-25 unités (produits chers)
        } else if (sousCategorie.contains("Meuble") || sousCategorie.contains("Jardin")) {
            return 2 + (int)(Math.random() * 13); // 2-15 unités (gros objets)
        } else if (sousCategorie.contains("Livres") || sousCategorie.contains("BD")) {
            return 20 + (int)(Math.random() * 80); // 20-100 unités (beaucoup de stock)
        } else if (sousCategorie.contains("Animaux")) {
            return 15 + (int)(Math.random() * 85); // 15-100 unités
        } else {
            return 8 + (int)(Math.random() * 42); // 8-50 unités (standard)
        }
    }
    
    private String generateAttributesByCategory(String sousCategorie, int index) {
        if (sousCategorie.contains("Chemise") || sousCategorie.contains("T-shirt") || 
            sousCategorie.contains("Pantalon") || sousCategorie.contains("Jean") ||
            sousCategorie.contains("Robe") || sousCategorie.contains("Jupe") ||
            sousCategorie.contains("Veste") || sousCategorie.contains("Manteau")) {
            
            String[] taillesVetements = {"XS", "S", "M", "L", "XL", "XXL", "3XL"};
            String[] couleurs = {"Blanc", "Noir", "Bleu", "Rouge", "Gris", "Vert", "Jaune", "Rose", "Bleu marine", "Beige"};
            String[] matieres = {"Coton", "Polyester", "Laine", "Lin", "Jean", "Soie", "Synthétique"};
            
            String taille = taillesVetements[index % taillesVetements.length];
            String couleur = couleurs[(index + taille.hashCode()) % couleurs.length];
            String matiere = matieres[(index + couleur.hashCode()) % matieres.length];
            
            return String.format("{\"taille\":\"%s\",\"couleur\":\"%s\",\"matiere\":\"%s\",\"origine\":\"Made in Senegal\"}", 
                               taille, couleur, matiere);
            
        } else if (sousCategorie.contains("Chaussure") || sousCategorie.contains("Basket") ||
                   sousCategorie.contains("Bottine") || sousCategorie.contains("Sandale")) {
            
            String[] pointures = {"36", "37", "38", "39", "40", "41", "42", "43", "44", "45"};
            String[] couleurs = {"Noir", "Blanc", "Bleu", "Rouge", "Gris", "Marron", "Vert"};
            String[] matieres = {"Cuir", "Synthétique", "Textile", "Caoutchouc", "Mesh"};
            
            String pointure = pointures[index % pointures.length];
            String couleur = couleurs[(index + pointure.hashCode()) % couleurs.length];
            String matiere = matieres[(index + couleur.hashCode()) % matieres.length];
            
            return String.format("{\"pointure\":\"%s\",\"couleur\":\"%s\",\"matiere\":\"%s\",\"type\":\"Sport/Casual\"}", 
                               pointure, couleur, matiere);
            
        } else if (sousCategorie.contains("Montre") || sousCategorie.contains("Bijoux") ||
                   sousCategorie.contains("Bracelet") || sousCategorie.contains("Collier")) {
            
            String[] styles = {"Classique", "Moderne", "Sport", "Luxe", "Vintage"};
            String[] materiaux = {"Acier", "Or", "Argent", "Titane", "Cuir", "Plastique"};
            String[] couleurs = {"Argent", "Or", "Noir", "Bleu", "Rose", "Bronze"};
            
            String style = styles[index % styles.length];
            String materiau = materiaux[(index + style.hashCode()) % materiaux.length];
            String couleur = couleurs[(index + materiau.hashCode()) % couleurs.length];
            
            return String.format("{\"style\":\"%s\",\"materiau\":\"%s\",\"couleur\":\"%s\",\"etanche\":true}", 
                               style, materiau, couleur);
            
        } else if (sousCategorie.contains("Téléphone") || sousCategorie.contains("Ordinateur") ||
                   sousCategorie.contains("Tablette") || sousCategorie.contains("Écouteurs")) {
            
            String[] couleurs = {"Noir", "Blanc", "Bleu", "Rouge", "Gris", "Or", "Rose"};
            String[] capacites = {"64GB", "128GB", "256GB", "512GB", "1TB"};
            String[] marques = {"Samsung", "Apple", "Xiaomi", "Huawei", "Oppo", "Nokia"};
            
            String couleur = couleurs[index % couleurs.length];
            String capacite = capacites[(index + couleur.hashCode()) % capacites.length];
            String marque = marques[(index + capacite.hashCode()) % marques.length];
            
            return String.format("{\"couleur\":\"%s\",\"capacite\":\"%s\",\"marque\":\"%s\",\"garantie\":\"2 ans\"}", 
                               couleur, capacite, marque);
            
        } else {
            // Attributs génériques pour les autres catégories
            String[] couleurs = {"Rouge", "Bleu", "Vert", "Noir", "Blanc", "Gris", "Jaune", "Rose", "Marron"};
            String[] tailles = {"S", "M", "L", "XL", "Unique"};
            String[] marques = {"Premium", "Eco", "Pro", "Standard", "Deluxe"};
            
            String couleur = couleurs[index % couleurs.length];
            String taille = tailles[(index + couleur.hashCode()) % tailles.length];
            String marque = marques[(index + taille.hashCode()) % marques.length];
            
            return String.format("{\"couleur\":\"%s\",\"taille\":\"%s\",\"marque\":\"%s\",\"qualité\":\"Haute\"}", 
                               couleur, taille, marque);
        }
    }

    private String getRandomTaille() {
        String[] tailles = {"S", "M", "L", "XL", "XXL"};
        return tailles[(int)(Math.random() * tailles.length)];
    }

    private String getRandomCouleur() {
        String[] couleurs = {"Rouge", "Bleu", "Vert", "Noir", "Blanc", "Gris", "Jaune"};
        return couleurs[(int)(Math.random() * couleurs.length)];
    }

    private void initializePaniersForClients() {
        log.info("Initialisation des paniers pour chaque client...");

        // Vérifier si les paniers existent déjà
        if (panierRepository.count() > 0) {
            log.info("Les paniers existent déjà, initialisation ignorée.");
            return;
        }

        // Récupérer tous les clients
        List<Compte> clients = compteRepository.findByRole(Compte.Role.CLIENT);
        List<Article> articles = articleRepository.findAll();

        for (Compte client : clients) {
            // Créer 1-3 paniers par client avec différents statuts
            int paniersCount = 1 + (int)(Math.random() * 3); // 1 à 3 paniers
            
            for (int i = 0; i < paniersCount; i++) {
                Panier panier = createPanierForClient(client, i, articles);
                panierRepository.save(panier);
                
                // Ajouter quelques articles au panier
                addRandomArticlesToPanier(panier, articles);
                
                log.info("Panier créé pour le client {}: {}", client.getEmail(), panier.getStatus());
            }
        }
    }

    private Panier createPanierForClient(Compte client, int index, List<Article> articles) {
        Panier panier = new Panier();
        panier.setClient(client.getPersonne());
        
        // Varier les statuts pour les tests
        Panier.PanierStatus[] statuses = {
            Panier.PanierStatus.EN_ATTENTE,
            Panier.PanierStatus.VALIDE,
            Panier.PanierStatus.ANNULE
        };
        
        // Le premier panier est toujours EN_ATTENTE, les autres peuvent avoir différents statuts
        if (index == 0) {
            panier.setStatus(Panier.PanierStatus.EN_ATTENTE);
        } else {
            panier.setStatus(statuses[index % statuses.length]);
        }
        
        panier.setTotal(java.math.BigDecimal.ZERO);
        return panier;
    }

    private void addRandomArticlesToPanier(Panier panier, List<Article> articles) {
        // Ajouter 2-5 articles par panier
        int itemsCount = 2 + (int)(Math.random() * 4); // 2 à 5 articles
        
        for (int i = 0; i < itemsCount && i < articles.size(); i++) {
            Article randomArticle = articles.get((int)(Math.random() * articles.size()));
            
            // Vérifier si l'article n'est pas déjà dans le panier
            boolean alreadyInPanier = panier.getItems() != null && 
                panier.getItems().stream().anyMatch(item -> item.getArticle().getId().equals(randomArticle.getId()));
            
            if (!alreadyInPanier) {
                PanierItem item = createPanierItem(panier, randomArticle);
                panierItemRepository.save(item);
            }
        }
        
        // Recalculer le total du panier
        java.math.BigDecimal total = panierItemRepository.calculateTotalByPanierId(panier.getId());
        panier.setTotal(total != null ? total : java.math.BigDecimal.ZERO);
        panierRepository.save(panier);
    }

    private PanierItem createPanierItem(Panier panier, Article article) {
        PanierItem item = new PanierItem();
        item.setPanier(panier);
        item.setArticle(article);
        item.setQuantite(1 + (int)(Math.random() * 5)); // 1 à 5 unités
        item.setPrixUnitaire(article.getPrix());
        return item;
    }

    private void initializeCommandesFromPaniers() {
        log.info("Initialisation des commandes à partir des paniers validés...");

        // Vérifier si les commandes existent déjà
        if (commandeRepository.count() > 0) {
            log.info("Les commandes existent déjà, initialisation ignorée.");
            return;
        }

        // Récupérer les paniers validés
        List<Panier> paniersValides = panierRepository.findByStatus(Panier.PanierStatus.VALIDE);
        
        for (Panier panier : paniersValides) {
            // Créer une commande pour chaque panier validé
            Commande commande = createCommandeFromPanier(panier);
            commandeRepository.save(commande);
            
            // Créer les détails de commande
            createDetailsForCommande(commande, panier);
            
            log.info("Commande créée pour le panier: {}", panier.getId());
        }
    }

    private Commande createCommandeFromPanier(Panier panier) {
        Commande commande = new Commande();
        commande.setClient(panier.getClient());
        commande.setPanier(panier);
        commande.setTotal(panier.getTotal());
        
        // Varier les statuts pour les tests
        Commande.CommandeStatus[] statuses = {
            Commande.CommandeStatus.EN_ATTENTE,
            Commande.CommandeStatus.CONFIRMEE,
            Commande.CommandeStatus.EN_PREPARATION,
            Commande.CommandeStatus.ENVOYEE,
            Commande.CommandeStatus.LIVREE
        };
        
        Commande.StatusPaiement[] paiementStatuses = {
            Commande.StatusPaiement.EN_ATTENTE,
            Commande.StatusPaiement.PAYEE,
            Commande.StatusPaiement.REMBOURSEE
        };
        
        commande.setStatus(statuses[(int)(Math.random() * statuses.length)]);
        commande.setStatusPaiement(paiementStatuses[(int)(Math.random() * paiementStatuses.length)]);
        
        return commande;
    }

    private void createDetailsForCommande(Commande commande, Panier panier) {
        if (panier.getItems() != null) {
            for (PanierItem panierItem : panier.getItems()) {
                DetailCommande detail = new DetailCommande();
                detail.setCommande(commande);
                detail.setArticle(panierItem.getArticle());
                detail.setQuantite(panierItem.getQuantite());
                detail.setPrixUnitaire(panierItem.getPrixUnitaire());
                // Le sous-total sera calculé automatiquement dans @PrePersist
                
                detailCommandeRepository.save(detail);
            }
        }
    }

}
