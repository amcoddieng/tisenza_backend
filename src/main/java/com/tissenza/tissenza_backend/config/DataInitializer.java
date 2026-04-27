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
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeCategoriesAndSubCategories();
        initializeBoutiquesForVendors();
        initializeProductsForBoutiques();
        initializeArticlesForProducts();
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
            createUser("admin", "admin123", "admin@tissenza.com", "Admin", "Tissenza", "ADMIN", "22112345678"),
            createUser("vendeur1", "vendeur123", "vendeur1@tissenza.com", "Vendeur", "Un", "VENDEUR", "22112345679"),
            createUser("vendeur2", "vendeur123", "vendeur2@tissenza.com", "Vendeur", "Deux", "VENDEUR", "22112345680"),
            createUser("client1", "client123", "client1@tissenza.com", "Client", "Un", "CLIENT", "22112345681"),
            createUser("client2", "client123", "client2@tissenza.com", "Client", "Deux", "CLIENT", "22112345682")
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
            createCategorie("Vêtements Hommes", "Collection complète de vêtements pour hommes"),
            createCategorie("Vêtements Femmes", "Mode féminine tendance et élégante"),
            createCategorie("Vêtements Enfants", "Habillages confortables pour enfants"),
            createCategorie("Chaussures", "Toutes sortes de chaussures pour toute la famille"),
            createCategorie("Accessoires", "Accessoires de mode et utilitaires"),
            createCategorie("Électronique", "Appareils électroniques et gadgets"),
            createCategorie("Maison & Décoration", "Articles pour la maison et décoration"),
            createCategorie("Sports & Loisirs", "Équipements sportifs et articles de loisirs"),
            createCategorie("Beauté & Santé", "Produits de beauté et bien-être"),
            createCategorie("Bagagerie", "Sacs, valises et accessoires de voyage")
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
            createSubCategorie("Maroquinerie", "Portefeuilles et petits articles en cuir", categories.get(9))
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
        boutique.setVendeur(vendeur); // Le vendeur est de type Compte, pas Personne
        boutique.setNom("Boutique de " + vendeur.getPersonne().getNom());
        boutique.setDescription("Boutique spécialisée pour " + vendeur.getPersonne().getNom());
        boutique.setAddresse("Adresse par défaut, Dakar");
        boutique.setStatut(Boutique.Statut.VALIDE); // Valider automatiquement pour les tests
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
        produit.setNom("Produit " + (index + 1) + " - " + sousCategorie.getNom());
        produit.setDescription("Description du produit " + (index + 1) + " pour " + boutique.getNom());
        produit.setImage("https://example.com/product" + (index + 1) + ".jpg");
        produit.setStatut(Produit.Statut.ACTIF);
        produit.setCreatedAt(LocalDateTime.now());
        return produit;
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
        article.setPrix(java.math.BigDecimal.valueOf(1000.0 + (Math.random() * 50000.0))); // Prix entre 1000 et 51000
        article.setStockActuel(10 + (int)(Math.random() * 90)); // 10 à 100 unités
        
        // Créer des attributs JSON valides avec taille et couleur
        String attributsJson = String.format("{\"taille\":\"%s\",\"couleur\":\"%s\"}", getRandomTaille(), getRandomCouleur());
        article.setAttributs(attributsJson);
        
        article.setImage("https://example.com/article" + (index + 1) + ".jpg");
        return article;
    }

    private String getRandomTaille() {
        String[] tailles = {"S", "M", "L", "XL", "XXL"};
        return tailles[(int)(Math.random() * tailles.length)];
    }

    private String getRandomCouleur() {
        String[] couleurs = {"Rouge", "Bleu", "Vert", "Noir", "Blanc", "Gris", "Jaune"};
        return couleurs[(int)(Math.random() * couleurs.length)];
    }

}
