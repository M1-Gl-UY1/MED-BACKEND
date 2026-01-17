#!/bin/bash
# Script de configuration initiale du VPS pour MED Backend
# À exécuter UNE SEULE FOIS sur le VPS

set -e

DEPLOY_PATH="/opt/med-backend"

echo "========================================="
echo "  MED Backend - Configuration VPS"
echo "========================================="

# Créer le dossier de déploiement
echo "1. Création du dossier $DEPLOY_PATH..."
sudo mkdir -p $DEPLOY_PATH
sudo chown $USER:$USER $DEPLOY_PATH

# Créer les sous-dossiers
mkdir -p $DEPLOY_PATH/target
mkdir -p $DEPLOY_PATH/scripts

echo ""
echo "2. Dossier créé avec succès!"
echo ""
echo "========================================="
echo "  Configuration terminée!"
echo "========================================="
echo ""
echo "Prochaines étapes:"
echo "1. Configurez les secrets GitHub (voir README)"
echo "2. Poussez le code sur la branche main"
echo "3. Le déploiement se fera automatiquement"
echo ""
echo "Pour un déploiement manuel:"
echo "  cd $DEPLOY_PATH"
echo "  ./scripts/deploy-manual.sh"
