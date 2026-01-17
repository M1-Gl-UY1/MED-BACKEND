package com.example.med.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Service de stockage de fichiers sur Cloudinary
 * Implémente StorageService pour permettre le stockage cloud des images
 */
@Service
@Primary
public class CloudinaryStorageService implements StorageService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.folder:med-vehicules}")
    private String folder;

    private Cloudinary cloudinary;

    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ));
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            // Valider le type de fichier
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Le fichier doit être une image");
            }

            // Générer un identifiant unique
            String publicId = folder + "/" + UUID.randomUUID().toString();

            // Upload vers Cloudinary avec optimisation automatique
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId,
                "resource_type", "image",
                "quality", "auto",
                "fetch_format", "auto"
            ));

            // Retourner l'URL sécurisée
            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload vers Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            // Extraire le public_id de l'URL Cloudinary
            String publicId = extractPublicId(fileUrl);
            if (publicId == null) {
                return false;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));

        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getFileUrl(String fileIdentifier) {
        // Si c'est déjà une URL complète, la retourner telle quelle
        if (fileIdentifier.startsWith("http")) {
            return fileIdentifier;
        }
        // Sinon, construire l'URL Cloudinary
        return cloudinary.url().secure(true).generate(fileIdentifier);
    }

    /**
     * Extrait le public_id d'une URL Cloudinary
     * Format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{public_id}.{format}
     */
    private String extractPublicId(String url) {
        if (url == null || !url.contains("cloudinary.com")) {
            return null;
        }
        try {
            // Trouver la partie après /upload/
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) {
                return null;
            }
            String afterUpload = url.substring(uploadIndex + 8);

            // Ignorer la version (v123456789/)
            if (afterUpload.startsWith("v")) {
                int slashIndex = afterUpload.indexOf("/");
                if (slashIndex != -1) {
                    afterUpload = afterUpload.substring(slashIndex + 1);
                }
            }

            // Retirer l'extension
            int lastDot = afterUpload.lastIndexOf(".");
            if (lastDot != -1) {
                afterUpload = afterUpload.substring(0, lastDot);
            }

            return afterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
