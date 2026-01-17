# Déploiement MED Backend sur VPS

## Architecture

```
VPS (84.247.183.206)
├── Docker
│   ├── med-postgres (PostgreSQL 15)
│   └── med-backend (Spring Boot)
└── Port exposé: 9085
```

## Configuration GitHub Secrets

Dans votre repo GitHub → Settings → Secrets and variables → Actions → New repository secret

| Secret | Valeur |
|--------|--------|
| `VPS_HOST` | `84.247.183.206` |
| `VPS_USER` | `softengine` |
| `VPS_PASSWORD` | Votre mot de passe SSH |
| `POSTGRES_PASSWORD` | Un mot de passe fort pour PostgreSQL |
| `JWT_SECRET` | Une clé secrète longue (min 32 caractères) |
| `MAIL_PASSWORD` | App password Gmail |
| `CLOUDINARY_CLOUD_NAME` | Votre cloud name Cloudinary |
| `CLOUDINARY_API_KEY` | Votre API key Cloudinary |
| `CLOUDINARY_API_SECRET` | Votre API secret Cloudinary |

## Première installation sur le VPS

```bash
# Se connecter au VPS
ssh softengine@84.247.183.206

# Créer le dossier
sudo mkdir -p /opt/med-backend
sudo chown softengine:softengine /opt/med-backend
```

## Déploiement automatique (CI/CD)

Le déploiement se fait automatiquement à chaque push sur `main`:

1. GitHub Actions build le projet
2. Copie les fichiers sur le VPS
3. Build l'image Docker
4. Démarre les containers
5. Vérifie la santé de l'API

## Déploiement manuel

```bash
# Sur le VPS
cd /opt/med-backend

# Créer le .env à partir de l'exemple
cp .env.example .env
nano .env  # Modifier les valeurs

# Déployer
chmod +x scripts/deploy-manual.sh
./scripts/deploy-manual.sh
```

## Commandes utiles

```bash
# Voir les logs
docker compose -f docker-compose.prod.yml logs -f

# Voir les logs du backend seulement
docker compose -f docker-compose.prod.yml logs -f backend

# Redémarrer
docker compose -f docker-compose.prod.yml restart

# Arrêter
docker compose -f docker-compose.prod.yml down

# Voir l'état
docker compose -f docker-compose.prod.yml ps
```

## Accès à l'API

- **URL**: `http://84.247.183.206:9085`
- **Health check**: `http://84.247.183.206:9085/actuator/health`
- **Swagger UI**: `http://84.247.183.206:9085/swagger-ui.html`

## Mise à jour des frontends

Après le déploiement, mettez à jour l'URL de l'API dans:

### MED-ADMIN
```env
# .env ou .env.production
VITE_API_BASE_URL=http://84.247.183.206:9085
```

### MED-FRONTEND-WEB
```env
# .env ou .env.production
VITE_API_BASE_URL=http://84.247.183.206:9085
```

## Dépannage

### Le backend ne démarre pas
```bash
docker compose -f docker-compose.prod.yml logs backend
```

### Problème de base de données
```bash
docker compose -f docker-compose.prod.yml logs postgres
docker compose -f docker-compose.prod.yml exec postgres psql -U med_user -d med_db
```

### Redémarrer depuis zéro
```bash
docker compose -f docker-compose.prod.yml down -v  # Supprime aussi les volumes!
docker compose -f docker-compose.prod.yml up -d --build
```
