================================================================================
                    MED MOTORS - BACKEND SPRING BOOT
                    INF4067 - UML et Design Patterns (2025-2026)
================================================================================

Backend Java Spring Boot implementant 12 design patterns du Gang of Four (GoF)
pour une application de vente en ligne de vehicules.

================================================================================
                             TABLE DES MATIERES
================================================================================

1. STRUCTURE DU BACKEND
2. PREREQUIS
3. CONFIGURATION
4. LANCEMENT
5. DESIGN PATTERNS IMPLEMENTES
6. ARCHITECTURE DES PATTERNS
7. EXEMPLES D'UTILISATION

================================================================================
                        1. STRUCTURE DU BACKEND
================================================================================

med/
|-- src/main/java/com/example/med/
|   |-- model/                      # Entites JPA
|   |   |-- commande_et_document/   # Commande, Document, LiasseDocuments
|   |   |-- vehicule/               # Vehicule, Catalogue
|   |   |-- client/                 # Client, Societe
|   |
|   |-- outil/                      # Design Patterns
|   |   |-- Abstract_Factory/       # Pattern Abstract Factory
|   |   |-- Builder/                # Pattern Builder
|   |   |-- factory/                # Pattern Factory Method
|   |   |-- adapter/                # Pattern Adapter
|   |   |-- bridge/                 # Pattern Bridge
|   |   |-- composite/              # Pattern Composite
|   |   |-- decorator/              # Pattern Decorator
|   |   |-- observer/               # Pattern Observer
|   |   |-- Iterator/               # Pattern Iterator
|   |   |-- templateMethod/         # Pattern Template Method
|   |   |-- command/                # Pattern Command
|   |
|   |-- controller/                 # REST Controllers
|   |-- service/                    # Services metier
|   |-- repository/                 # Repositories JPA
|
|-- src/main/resources/
|   |-- application.properties      # Configuration
|
|-- pom.xml                         # Dependances Maven

================================================================================
                             2. PREREQUIS
================================================================================

- Java JDK 17 ou superieur
- Maven 3.8+
- MySQL 8.0+ (ou PostgreSQL)

================================================================================
                           3. CONFIGURATION
================================================================================

Modifier le fichier src/main/resources/application.properties :

    spring.datasource.url=jdbc:mysql://localhost:3306/med_motors
    spring.datasource.username=votre_username
    spring.datasource.password=votre_password
    spring.jpa.hibernate.ddl-auto=update

================================================================================
                            4. LANCEMENT
================================================================================

Option 1 - Maven:
    mvn spring-boot:run 

Option 2 - JAR:
    mvn clean package
    java -jar target/med-0.0.1-SNAPSHOT.jar

Le serveur demarre sur: http://localhost:8080

================================================================================
                    5. DESIGN PATTERNS IMPLEMENTES
================================================================================

+----+---------------------+----------------------------------+------------------------+
| #  | PATTERN             | DESCRIPTION                      | PACKAGE                |
+----+---------------------+----------------------------------+------------------------+
| 1  | Abstract Factory    | Creation vehicules essence/elec  | outil.Abstract_Factory |
| 2  | Builder             | Construction liasses documents   | outil.Builder          |
| 3  | Factory Method      | Creation commandes comptant/credit| outil.factory         |
| 4  | Singleton           | Liasse vierge unique             | model.commande_et_document |
| 5  | Adapter             | Export documents PDF/HTML        | outil.adapter          |
| 6  | Bridge              | Formulaires HTML/Widget          | outil.bridge           |
| 7  | Composite           | Societes meres et filiales       | outil.composite        |
| 8  | Decorator           | Promotions/Destockage vehicules  | outil.decorator        |
| 9  | Observer            | Notification changement prix     | outil.observer         |
| 10 | Iterator            | Parcours du catalogue            | outil.Iterator         |
| 11 | Template Method     | Calcul taxes par pays            | outil.templateMethod   |
| 12 | Command             | Solde vehicules avec undo        | outil.command          |
+----+---------------------+----------------------------------+------------------------+

================================================================================
                      6. ARCHITECTURE DES PATTERNS
================================================================================

ABSTRACT FACTORY
----------------
VehiculeFactory (interface)
    |-- VehiculeEssenceF --> AutomobileEssence, ScooterEssence
    |-- VehiculeElectricF --> AutomobileElectric, ScooterElectric

BUILDER
-------
DirecteurLiasse --> LiasseBuilder (abstract)
                        |-- LiasseBuilderPDF
                        |-- LiasseBuilderHTML

FACTORY METHOD
--------------
CreateurCommande (abstract)
    |-- CreateurCommandeComptant --> CommandeComptant
    |-- CreateurCommandeCredit --> CommandeCredit

SINGLETON
---------
LiasseDocuments
    - instance static
    - constructeur protected
    - getInstance() synchronized

ADAPTER
-------
DocumentExport (Target interface)
    |-- DocumentPDFExporter --> PDFLIBRARY (Adaptee)
    |-- HTMLExporter

BRIDGE
------
Form (abstraction) <>--> FormRender (implementor)
    |-- LoginForm              |-- HtmlRender
    |-- RegisterForm           |-- WidgetRender

COMPOSITE
---------
Societe (component)
    |-- Filiale (leaf)
    |-- SocieteMere (composite) --> List<Societe>

DECORATOR
---------
VehiculeComposant (interface)
    |-- VehiculeDeBase (concrete component)
    |-- DecorateurVehicule (abstract decorator)
            |-- DecorateurPromo (-10%)
            |-- DecorateurDestock (-20%)

OBSERVER
--------
Sujet (interface) <-- Vehicule
VehiculeObserver (interface) <-- ConcretVehiculeObserver

ITERATOR
--------
Catalogue (interface) --> VehiculeIterator (interface)
    |-- CatalogueVehicule       |-- CatalogueIterator

TEMPLATE METHOD
---------------
CalculCommande (abstract)
    + calculerMontant() [final]
    + calculerTaxes() [abstract]
        |-- CalculCommandeFrance (TVA 20%)
        |-- CalculCommandeCameroun (TVA 19.25%)

COMMAND
-------
GestionnairesCommandes (invoker) --> CommandeAction (interface)
                                         |-- CommandeSoldeVehicule

================================================================================
                       7. EXEMPLES D'UTILISATION
================================================================================

// ABSTRACT FACTORY
VehiculeFactory factory = new VehiculeEssenceF();
Automobile auto = factory.createAutomobile();
Scooter scooter = factory.createScooter();

// BUILDER
LiasseBuilder builder = new LiasseBuilderPDF();
DirecteurLiasse directeur = new DirecteurLiasse(builder);
directeur.construireLiasse(commande);
LiasseDocuments liasse = builder.getLiasse();

// FACTORY METHOD
CreateurCommande createur = new CreateurCommandeComptant();
Commande cmd = createur.creerCommande();

// SINGLETON
LiasseDocuments liasse1 = LiasseDocuments.getInstance();
LiasseDocuments liasse2 = LiasseDocuments.getInstance();
// liasse1 == liasse2

// ADAPTER
DocumentExport exporter = new DocumentPDFExporter();
exporter.exporter(document);

// BRIDGE
Form loginHtml = new LoginForm(new HtmlRender());
Form loginWidget = new LoginForm(new WidgetRender());

// COMPOSITE
SocieteMere groupe = new SocieteMere();
groupe.ajouterFiliale(new Filiale());
groupe.ajouterFiliale(new SocieteMere());

// DECORATOR (chainage)
Vehicule v = new Vehicule();
v.setPrixBase(10000);
VehiculeComposant decorated = new DecorateurPromo(
    new DecorateurDestock(
        new VehiculeDeBase(v)
    )
);
System.out.println(decorated.getPrix()); // 7200

// OBSERVER
Vehicule v = new Vehicule();
v.ajouterObserver(new ConcretVehiculeObserver("Alerte"));
v.setPrix(25000); // Notifie automatiquement

// ITERATOR
Catalogue catalogue = new CatalogueVehicule();
catalogue.addVehicule(vehicule1);
VehiculeIterator it = catalogue.getIterator();
while (it.hasNext()) {
    Vehicule v = (Vehicule) it.next();
}

// TEMPLATE METHOD
CalculCommande calcFR = new CalculCommandeFrance();
CalculCommande calcCM = new CalculCommandeCameroun();
double totalFR = calcFR.calculerMontant(commande); // +20% TVA
double totalCM = calcCM.calculerMontant(commande); // +19.25% TVA

// COMMAND (avec undo)
GestionnairesCommandes gestionnaire = new GestionnairesCommandes();
CommandeAction cmd = new CommandeSoldeVehicule(vehicule, 0.20);
gestionnaire.executerCommande(cmd);
gestionnaire.annulerDerniereCommande(); // undo

================================================================================
