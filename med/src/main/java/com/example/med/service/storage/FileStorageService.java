package com.example.med.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service de stockage de fichiers sur le serveur local
 */
@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads/images}")
    private String uploadDir;

    private Path uploadPath;

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier d'upload: " + uploadPath, e);
        }
    }

    /**
     * Stocke un fichier et retourne son nom unique
     */
    public String storeFile(MultipartFile file) {
        // Nettoyer le nom du fichier
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Vérifier les extensions autorisées
        String extension = getFileExtension(originalFileName);
        if (!isImageExtension(extension)) {
            throw new RuntimeException("Type de fichier non autorisé: " + extension);
        }

        // Générer un nom unique pour éviter les collisions
        String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

        try {
            // Vérifier les caractères invalides
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Nom de fichier invalide: " + originalFileName);
            }

            // Copier le fichier vers le dossier d'upload
            Path targetLocation = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage du fichier: " + originalFileName, e);
        }
    }

    /**
     * Supprime un fichier
     */
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = uploadPath.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Retourne le chemin complet d'un fichier
     */
    public Path getFilePath(String fileName) {
        return uploadPath.resolve(fileName).normalize();
    }

    /**
     * Génère l'URL publique pour accéder au fichier
     */
    public String getFileUrl(String fileName, String baseUrl) {
        return baseUrl + "/uploads/images/" + fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg")
            || extension.equals("png") || extension.equals("gif")
            || extension.equals("webp");
    }
}
