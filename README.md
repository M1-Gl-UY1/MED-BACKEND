# MED Backend

Backend Spring Boot pour l'application MED Motors - Vente de vehicules.

## Technologies

- **Java 25** avec **Spring Boot 4.0.1**
- **Spring Data JPA** - Persistance des donnees
- **Spring Security** - Securite et authentification
- **JWT (JJWT 0.12.5)** - Authentification par tokens
- **PostgreSQL** - Base de donnees
- **Lombok** - Reduction du boilerplate
- **BCrypt** - Hashage des mots de passe

## Structure du Projet

```
med/src/main/java/com/example/med/
├── config/                     # Configuration Spring
│   ├── WebConfig.java         # Config ressources statiques
│   └── AdminDataInitializer.java  # Seed admin au demarrage
├── controller/
│   ├── auth/                  # Authentification
│   │   └── AuthController.java
│   ├── catalogue/             # Vehicules et catalogue
│   ├── commande/              # Gestion des commandes
│   ├── documents/             # Liasses documentaires
│   └── utilisateur/           # Clients et societes
├── dto/                       # Data Transfer Objects
├── model/
│   ├── catalogue/             # Vehicule, Stock, Options
│   ├── commande_et_document/  # Commande, Documents
│   ├── panier/               # Panier, LignePanier
│   └── utilisateur/          # Client, Societe, Admin
├── repository/               # Repositories JPA
├── security/                 # Configuration securite JWT
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   ├── UserPrincipal.java
│   └── SecurityConfig.java
├── service/                  # Logique metier
└── outil/                    # Design patterns implementes
```

## Configuration

### Base de donnees (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/med_db
    username: postgres
    password: 12345678
  jpa:
    hibernate:
      ddl-auto: create  # Utiliser 'update' en production
```

### JWT

```yaml
jwt:
  secret: "MED-Application-Secret-Key-For-JWT-Token-Generation-2024-Must-Be-At-Least-256-Bits-Long"
  expiration: 86400000      # 24h pour clients/societes
  admin-expiration: 28800000 # 8h pour admin
```

## Authentification JWT

### Endpoints d'authentification

| Endpoint | Methode | Description | Acces |
|----------|---------|-------------|-------|
| `/api/auth/login` | POST | Login Client/Societe | Public |
| `/api/auth/admin/login` | POST | Login Admin | Public |
| `/api/auth/me` | GET | Valider token et recuperer user | Authentifie |
| `/api/auth/refresh` | POST | Rafraichir le token | Authentifie |

### Format de requete login

```json
{
  "email": "user@example.com",
  "motDePasse": "password123"
}
```

### Format de reponse login

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": { ... },
  "type": "CLIENT|SOCIETE|ADMIN",
  "message": "Connexion reussie"
}
```

### Utilisation du token

Inclure le token dans le header Authorization:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

## Compte Administrateur

Au demarrage, un compte admin est cree automatiquement:

- **Email:** `admin-med@gmail.com`
- **Mot de passe:** `12345678`

## Endpoints proteges

| Endpoint | Acces |
|----------|-------|
| `GET /api/vehicules/**` | Public |
| `GET /api/catalogue/**` | Public |
| `POST /api/clients` | Public (inscription) |
| `POST /api/societes` | Public (inscription) |
| `/api/commandes/**` | Authentifie |
| `/api/panier/**` | Authentifie |
| `/api/stats/**` | Admin uniquement |
| `POST/PUT/DELETE /api/vehicules/**` | Admin uniquement |

## Demarrage

### Prerequis

1. Java 25+
2. PostgreSQL avec une base `med_db`
3. Maven

### Lancer le serveur

```bash
cd MED-BACKEND/med
mvn spring-boot:run
```

Le serveur demarre sur `http://localhost:8085`

### Verifier le demarrage

Dans les logs, vous devriez voir:
```
Compte administrateur cree: admin-med@gmail.com
```

## Design Patterns Implementes

- **Abstract Factory** - Creation de vehicules (Essence/Electrique)
- **Builder** - Construction de liasses documentaires
- **Factory Method** - Creation de commandes (Comptant/Credit)
- **Iterator** - Parcours du catalogue
- **Observer** - Notifications vehicules
- **Composite** - Structure societes/filiales
- **Decorator** - Promotions et destockage
- **Template Method** - Calcul commandes par pays
- **Command** - Actions sur les commandes
- **Bridge** - Rendu des formulaires
- **Adapter** - Export de documents

## Tests API

### Tester le login admin

```bash
curl -X POST http://localhost:8085/api/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin-med@gmail.com","motDePasse":"12345678"}'
```

### Tester un endpoint protege

```bash
curl http://localhost:8085/api/stats \
  -H "Authorization: Bearer <token>"
```
