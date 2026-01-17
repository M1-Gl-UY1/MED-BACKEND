package com.example.med.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface de stockage de fichiers (Pattern Strategy)
 * Permet de switcher facilement entre stockage local et cloud
 */
public interface StorageService {

    /**
     * Stocke un fichier et retourne son URL publique
     */
    String storeFile(MultipartFile file);

    /**
     * Supprime un fichier par son identifiant/URL
     */
    boolean deleteFile(String fileIdentifier);

    /**
     * Retourne l'URL publique d'un fichier
     */
    String getFileUrl(String fileIdentifier);
}
