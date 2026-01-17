#!/bin/bash
# Script de déploiement manuel pour MED Backend
# Usage: ./deploy-manual.sh

set -e

DEPLOY_PATH="/opt/med-backend"
DOCKER_COMPOSE_FILE="docker-compose.prod.yml"

echo "========================================="
echo "  MED Backend - Déploiement Manuel"
echo "========================================="

# Vérifier qu'on est dans le bon dossier
if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
    echo "Erreur: $DOCKER_COMPOSE_FILE non trouvé!"
    echo "Assurez-vous d'être dans le dossier $DEPLOY_PATH"
    exit 1
fi

# Vérifier le fichier .env
if [ ! -f ".env" ]; then
    echo "Erreur: Fichier .env non trouvé!"
    echo "Créez-le à partir de .env.example"
    exit 1
fi

echo ""
echo "1. Arrêt des containers existants..."
docker compose -f $DOCKER_COMPOSE_FILE down || true

echo ""
echo "2. Suppression de l'ancienne image..."
docker rmi med-backend 2>/dev/null || true

echo ""
echo "3. Build et démarrage..."
docker compose -f $DOCKER_COMPOSE_FILE up -d --build

echo ""
echo "4. Attente du démarrage (60s)..."
sleep 60

echo ""
echo "5. Vérification de la santé..."
if curl -sf http://localhost:9085/actuator/health > /dev/null; then
    echo "✅ Backend démarré avec succès!"
    echo ""
    echo "API accessible sur: http://$(hostname -I | awk '{print $1}'):9085"
else
    echo "❌ Le backend ne répond pas. Vérifiez les logs:"
    docker compose -f $DOCKER_COMPOSE_FILE logs --tail=100
    exit 1
fi

echo ""
echo "6. Logs récents:"
docker compose -f $DOCKER_COMPOSE_FILE logs --tail=20

echo ""
echo "========================================="
echo "  Déploiement terminé!"
echo "========================================="
